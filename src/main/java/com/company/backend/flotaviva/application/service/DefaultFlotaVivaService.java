package com.company.backend.flotaviva.application.service;

import com.company.backend.flotaviva.application.port.in.ExportFlotaVivaUseCase;
import com.company.backend.flotaviva.application.port.in.GetFlotaVivaUseCase;
import com.company.backend.flotaviva.application.port.out.FlotaVivaPersistencePort;
import com.company.backend.flotaviva.domain.FlotaVivaRow;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.transaction.annotation.Transactional;

public class DefaultFlotaVivaService implements GetFlotaVivaUseCase, ExportFlotaVivaUseCase {
    private static final java.util.Set<String> SORTS = java.util.Set.of("matricula", "id", "petitionDate", "sociedad", "marca", "modelo");
    private static final String CSV = "text/csv; charset=UTF-8";
    private static final String XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final FlotaVivaPersistencePort persistence;
    private final Clock clock;

    public DefaultFlotaVivaService(FlotaVivaPersistencePort persistence) {
        this(persistence, Clock.systemUTC());
    }

    public DefaultFlotaVivaService(FlotaVivaPersistencePort persistence, Clock clock) {
        this.persistence = persistence;
        this.clock = clock;
    }

    @Override
    @Transactional(readOnly = true)
    public Result get(int page, int size, String sort, String country, String filter) {
        if (page < 0 || size < 1 || size > 500) throw new IllegalArgumentException("Invalid pagination");
        validateSort(sort);
        validateFilter(filter);
        var result = persistence.findPage(page, size, sort, defaultCountry(country), normalize(filter));
        int totalPages = (int) Math.ceil((double) result.totalElements() / size);
        return new Result(result.items(), page, size, result.totalElements(), totalPages,
                page + 1 < totalPages, freshness());
    }

    @Override
    @Transactional(readOnly = true)
    public Export export(String format, String sort, String country, String filter) {
        String normalizedFormat = format == null ? "" : format.toLowerCase(Locale.ROOT);
        if (!normalizedFormat.equals("csv") && !normalizedFormat.equals("xlsx")) {
            throw new IllegalArgumentException("format must be csv or xlsx");
        }
        validateSort(sort);
        validateFilter(filter);
        List<FlotaVivaRow> rows = persistence.findAll(sort, defaultCountry(country), normalize(filter));
        return switch (normalizedFormat) {
            case "csv" -> new Export(csv(rows), CSV, "flota-viva.csv");
            case "xlsx" -> new Export(xlsx(rows), XLSX, "flota-viva.xlsx");
            default -> throw new IllegalStateException("Unsupported export format");
        };
    }

    private GetFlotaVivaUseCase.Freshness freshness() {
        return new GetFlotaVivaUseCase.Freshness(OffsetDateTime.now(clock).withOffsetSameInstant(ZoneOffset.UTC), "CURRENT");
    }

    private static String defaultCountry(String country) { return country == null || country.isBlank() ? "ES" : country.trim().toUpperCase(Locale.ROOT); }
    private static String normalize(String filter) { return filter == null ? "" : filter.trim(); }
    private static void validateSort(String sort) {
        if (sort != null && !SORTS.contains(sort)) throw new IllegalArgumentException("Unsupported sort");
    }
    private static void validateFilter(String filter) {
        if (filter != null && filter.length() > 100) throw new IllegalArgumentException("filter must not exceed 100 characters");
    }

    private static byte[] csv(List<FlotaVivaRow> rows) {
        var out = new StringBuilder("\uFEFF").append(String.join(",", FlotaVivaRow.HEADERS)).append('\n');
        for (var row : rows) out.append(row.values().stream().map(DefaultFlotaVivaService::csvValue).collect(java.util.stream.Collectors.joining(","))).append('\n');
        return out.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static String csvValue(Object value) {
        String text = value == null ? "" : String.valueOf(value);
        return '"' + text.replace("\"", "\"\"") + '"';
    }

    private static byte[] xlsx(List<FlotaVivaRow> rows) {
        try {
            var out = new ByteArrayOutputStream();
            try (var zip = new ZipOutputStream(out)) {
                entry(zip, "[Content_Types].xml", "<?xml version=\"1.0\"?><Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\"><Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/><Default Extension=\"xml\" ContentType=\"application/xml\"/><Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/><Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/></Types>");
                entry(zip, "_rels/.rels", "<?xml version=\"1.0\"?><Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\"><Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/></Relationships>");
                entry(zip, "xl/workbook.xml", "<?xml version=\"1.0\"?><workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"><sheets><sheet name=\"Flota Viva\" sheetId=\"1\" r:id=\"rId1\"/></sheets></workbook>");
                entry(zip, "xl/_rels/workbook.xml.rels", "<?xml version=\"1.0\"?><Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\"><Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/></Relationships>");
                var xml = new StringBuilder("<?xml version=\"1.0\"?><worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\"><sheetData>");
                xml.append("<row>"); for (String h : FlotaVivaRow.HEADERS) xml.append(cell(h)); xml.append("</row>");
                for (var row : rows) { xml.append("<row>"); for (Object value : row.values()) xml.append(cell(value == null ? "" : value)); xml.append("</row>"); }
                xml.append("</sheetData></worksheet>"); entry(zip, "xl/worksheets/sheet1.xml", xml.toString());
            }
            return out.toByteArray();
        } catch (IOException ex) { throw new IllegalStateException("Could not create XLSX export", ex); }
    }

    private static String cell(Object value) { return "<c t=\"inlineStr\"><is><t>" + xml(String.valueOf(value)) + "</t></is></c>"; }
    private static String xml(String value) { return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&apos;"); }
    private static void entry(ZipOutputStream zip, String name, String content) throws IOException { zip.putNextEntry(new ZipEntry(name)); zip.write(content.getBytes(StandardCharsets.UTF_8)); zip.closeEntry(); }
}
