package com.company.backend.carfleetrequests.application.port.out;
import java.util.*;
public interface CarFleetRequestAuditPort { void append(UUID requestId, String action, String actor, Map<String,Object> changes); }
