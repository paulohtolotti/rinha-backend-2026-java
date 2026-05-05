package com.pht.rinha_backend_2026.dto;

import java.time.Instant;

public record LastTransaction(Instant timestamp, double km_from_current) {
}
