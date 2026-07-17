package com.company.backend.flotaviva.adapters.in.rest.response;

import com.company.backend.flotaviva.domain.FlotaVivaRow;

public record FlotaVivaRowResponse(
        Object id, Object petitionDate, Object divisionFiscalNumber, Object sociedad,
        Object nombreSociedad, Object matricula, Object fleetSegmentation, Object marca,
        Object modelo, Object descripcionVehiculo, Object motorizacion, Object etiqueta,
        Object co2, Object cuota, Object estadoVehiculo, Object fechaInicio,
        Object fechaFin, Object fechaExtension, Object proveedor, Object clasificacion,
        Object renewableFuel, Object costCenter, Object codeElement, Object driverName,
        Object driverMail, Object driverAdditionalMail, Object responsableVehicle,
        Object emailResponsableVehicle, Object country) {

    public static FlotaVivaRowResponse from(FlotaVivaRow row) {
        return new FlotaVivaRowResponse(row.id(), row.petitionDate(), row.divisionFiscalNumber(), row.sociedad(),
                row.nombreSociedad(), row.matricula(), row.fleetSegmentation(), row.marca(), row.modelo(),
                row.descripcionVehiculo(), row.motorizacion(), row.etiqueta(), row.co2(), row.cuota(),
                row.estadoVehiculo(), row.fechaInicio(), row.fechaFin(), row.fechaExtension(), row.proveedor(),
                row.clasificacion(), row.renewableFuel(), row.costCenter(), row.codeElement(), row.driverName(),
                row.driverMail(), row.driverAdditionalMail(), row.responsableVehicle(),
                row.emailResponsableVehicle(), row.country());
    }
}
