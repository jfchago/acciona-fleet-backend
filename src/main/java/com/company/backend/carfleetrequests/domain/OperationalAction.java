package com.company.backend.carfleetrequests.domain;

/** Actions available from the request context. External effects require a configured adapter. */
public enum OperationalAction {
    DUPLICATE,
    RETIRE,
    REINSTATE,
    FILE_ACCESS,
    FILE_UPLOAD,
    EMAIL,
    EXPORT,
    DRIVER,
    EXPENSE,
    RENTING,
    SUBSTITUTION,
    SOLRED,
    CARD
}
