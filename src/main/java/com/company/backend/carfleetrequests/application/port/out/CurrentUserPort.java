package com.company.backend.carfleetrequests.application.port.out;
import java.util.Set;
public interface CurrentUserPort { User current(); record User(String id, Set<String> authorities) { } }
