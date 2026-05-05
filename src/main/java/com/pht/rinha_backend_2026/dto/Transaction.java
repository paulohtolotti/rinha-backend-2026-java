package com.pht.rinha_backend_2026.dto;

import java.time.Instant;

public record Transaction(
        String id,
        double amount,
        int installments,
        Instant requested_at,
        Customer customer,
        Merchant merchant,
        Terminal terminal,
        Instant timestamp,
        double km_from_current
) {
}
