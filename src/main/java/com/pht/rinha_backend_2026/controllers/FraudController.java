package com.pht.rinha_backend_2026.controllers;

import com.pht.rinha_backend_2026.dto.FraudResponse;
import com.pht.rinha_backend_2026.dto.FraudRequest;
import com.pht.rinha_backend_2026.services.FraudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class FraudController {

    private final FraudService service;

    public FraudController(FraudService service) {
        this.service = service;
    }

    @GetMapping(value = "/ready")
    public ResponseEntity<Void> ready() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/fraud-score")
    public ResponseEntity<FraudResponse> fraudScore(@RequestBody FraudRequest fraudRequest) {

        System.out.println(fraudRequest.last_transaction());
        // Implementação de teste
        return ResponseEntity.ok(service.fraudScore(fraudRequest));
    }

    // Teste da normalização dos vetores
    @PostMapping(value = "/fraud-score/normalized")
    public ResponseEntity<String> normalizedVector(@RequestBody FraudRequest fraudRequest) {

        float[] arr = service.vectorize(fraudRequest);
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for(float a : arr) {
            sb.append(a).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");

        return ResponseEntity.ok(sb.toString());
    }
}
