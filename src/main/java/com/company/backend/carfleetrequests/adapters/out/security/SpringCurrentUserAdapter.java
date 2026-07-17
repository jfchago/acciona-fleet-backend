package com.company.backend.carfleetrequests.adapters.out.security;

import com.company.backend.carfleetrequests.application.port.out.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
class SpringCurrentUserAdapter implements CurrentUserPort {
    private static final Set<String> LOCAL_AUTHORITIES = Set.of("Access", "CARFLEET_REQUESTS_UPDATE", "CARFLEET_REQUESTS_RETIRE", "CARFLEET_REQUESTS_REINSTATE", "CARFLEET_REQUESTS_DUPLICATE", "CARFLEET_REQUESTS_OPERATIONAL_ACTION");

    @Value("${app.security.enabled:false}")
    private boolean securityEnabled;

    public User current() {
        var a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || a instanceof AnonymousAuthenticationToken) {
            return securityEnabled ? new User("anonymous", Set.of()) : new User("local-user", LOCAL_AUTHORITIES);
        }
        return new User(a.getName(), a.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toUnmodifiableSet()));
    }
}

