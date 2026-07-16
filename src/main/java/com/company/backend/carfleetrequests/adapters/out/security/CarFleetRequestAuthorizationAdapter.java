package com.company.backend.carfleetrequests.adapters.out.security;

import com.company.backend.carfleetrequests.application.port.out.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
class CarFleetRequestAuthorizationAdapter implements CarFleetRequestAuthorizationPort {
    public boolean allowed(CurrentUserPort.User u,Action action,Long id) {
        if (u == null || u.authorities() == null) return false;
        if (action == Action.READ && u.authorities().contains("Access")) return true;
        String verb = action.name();
        return u.authorities().stream().anyMatch(x -> x.equals("CARFLEET_REQUESTS_" + verb)
                || x.equals("carfleet-requests:" + id + ":" + verb.toLowerCase(Locale.ROOT))
                || x.equals("carfleet-requests:*:" + verb.toLowerCase(Locale.ROOT)));
    }
}
