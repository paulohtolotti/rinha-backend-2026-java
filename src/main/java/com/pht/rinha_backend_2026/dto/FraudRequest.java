package com.pht.rinha_backend_2026.dto;

public record FraudRequest(
        String id,
        Transaction transaction,
        Customer customer,
        Merchant merchant,
        Terminal terminal,
        LastTransaction last_transaction
) {
}
