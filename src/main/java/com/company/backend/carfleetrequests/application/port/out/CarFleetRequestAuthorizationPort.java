package com.company.backend.carfleetrequests.application.port.out;
public interface CarFleetRequestAuthorizationPort { boolean allowed(CurrentUserPort.User user, Action action, Long requestId); enum Action { READ, UPDATE, RETIRE, REINSTATE, DUPLICATE } }
