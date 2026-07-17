package com.company.backend.carfleetrequests.application.port.in;

import com.company.backend.carfleetrequests.domain.OperationalAction;

public record OperationalActionResult(OperationalAction action, Long requestId, String status, String message) { }
