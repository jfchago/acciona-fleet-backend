package com.company.backend.carfleetrequests.adapters.out.security;

import static org.assertj.core.api.Assertions.assertThat;
import com.company.backend.carfleetrequests.application.port.out.CarFleetRequestAuthorizationPort.Action;
import com.company.backend.carfleetrequests.application.port.out.CurrentUserPort.User;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CarFleetRequestAuthorizationAdapterTest {
    private final CarFleetRequestAuthorizationAdapter authorization = new CarFleetRequestAuthorizationAdapter();

    @Test
    void accessAuthorityOnlyAllowsRead() {
        var user = new User("alice", Set.of("Access"));

        assertThat(authorization.allowed(user, Action.READ, 7L)).isTrue();
        assertThat(authorization.allowed(user, Action.UPDATE, 7L)).isFalse();
        assertThat(authorization.allowed(user, Action.RETIRE, 7L)).isFalse();
    }

    @Test
    void configuredMutationAuthorityRemainsSupported() {
        var user = new User("alice", Set.of("Access", "CARFLEET_REQUESTS_UPDATE"));

        assertThat(authorization.allowed(user, Action.UPDATE, 7L)).isTrue();
    }
}
