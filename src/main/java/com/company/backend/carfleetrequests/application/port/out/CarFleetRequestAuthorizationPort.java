package com.company.backend.carfleetrequests.application.port.out;
import java.util.UUID;
public interface CarFleetRequestAuthorizationPort { boolean allowed(CurrentUserPort.User user, Action action, UUID requestId); enum Action { READ, UPDATE, RETIRE, REINSTATE } }
