package com.company.backend.flotaviva.domain;

import java.util.List;

public record FlotaVivaRow(
        Object id, Object petitionDate, Object divisionFiscalNumber, Object sociedad,
        Object nombreSociedad, Object matricula, Object fleetSegmentation, Object marca,
        Object modelo, Object descripcionVehiculo, Object motorizacion, Object etiqueta,
        Object co2, Object cuota, Object estadoVehiculo, Object fechaInicio,
        Object fechaFin, Object fechaExtension, Object proveedor, Object clasificacion,
        Object renewableFuel, Object costCenter, Object codeElement, Object driverName,
        Object driverMail, Object driverAdditionalMail, Object responsableVehicle,
        Object emailResponsableVehicle, Object country) {

    public static final List<String> HEADERS = List.of(
            "id", "PetitionDate", "DivisionFiscalNumber", "Sociedad", "Nombre Sociedad",
            "Matricula", "FleetSegmentation", "Marca", "Modelo", "Descripcion Vehiculo",
            "Motorización", "Etiqueta", "CO2", "Cuota", "Estado Vehiculo", "Fecha Inicio",
            "Fecha Fin", "Fecha Extension", "Proveedor", "Clasificacion", "RenewableFuel",
            "CostCenter", "CodeElement", "DriverName", "DriverMail", "DriverAdditionalMail",
            "ResponsableVehicle", "eMailResponsableVehicle", "Country");

    public List<Object> values() {
        return java.util.Arrays.asList(id, petitionDate, divisionFiscalNumber, sociedad, nombreSociedad, matricula,
                fleetSegmentation, marca, modelo, descripcionVehiculo, motorizacion, etiqueta,
                co2, cuota, estadoVehiculo, fechaInicio, fechaFin, fechaExtension, proveedor,
                clasificacion, renewableFuel, costCenter, codeElement, driverName, driverMail,
                driverAdditionalMail, responsableVehicle, emailResponsableVehicle, country);
    }
}
