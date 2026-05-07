package com.pht.rinha_backend_2026.dto;

import java.time.Instant;

public record Transaction(float amount, int installments, Instant requested_at) {
}
