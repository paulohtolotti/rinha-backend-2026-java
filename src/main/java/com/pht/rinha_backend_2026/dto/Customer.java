package com.pht.rinha_backend_2026.dto;

public record Customer(float avg_amount, int tx_count_24h, String[] known_merchants) {
}
