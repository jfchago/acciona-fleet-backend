package com.company.backend.shared.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DefaultGetServiceStatusServiceTest {

    @Test
    void getStatus_returnsUpStatus() {
        // Arrange
        DefaultGetServiceStatusService service = new DefaultGetServiceStatusService();

        // Act
        var result = service.getStatus();

        // Assert
        assertThat(result.status()).isEqualTo("UP");
    }
}
