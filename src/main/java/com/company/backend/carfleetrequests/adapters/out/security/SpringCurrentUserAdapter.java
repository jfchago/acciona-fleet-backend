package com.company.backend.carfleetrequests.adapters.out.security;

import com.company.backend.carfleetrequests.application.port.out.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
class SpringCurrentUserAdapter implements CurrentUserPort {
    public User current() { var a=SecurityContextHolder.getContext().getAuthentication(); if(a==null||a instanceof AnonymousAuthenticationToken) return new User("local-user",Set.of("Access")); return new User(a.getName(),a.getAuthorities().stream().map(x->x.getAuthority()).collect(Collectors.toUnmodifiableSet())); }
}

