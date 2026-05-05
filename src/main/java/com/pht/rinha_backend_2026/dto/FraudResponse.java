package com.pht.rinha_backend_2026.dto;

public record FraudResponse(boolean approved, double fraud_score) {
}
