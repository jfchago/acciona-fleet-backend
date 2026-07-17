package com.company.backend.flotaviva.application.port.in;

import java.util.List;

public interface ExportFlotaVivaUseCase {
    Export export(String format, String sort, String country, String filter);

    record Export(byte[] content, String contentType, String filename) { }
}
