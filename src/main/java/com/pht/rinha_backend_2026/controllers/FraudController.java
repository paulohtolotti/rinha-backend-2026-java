package com.pht.rinha_backend_2026.controllers;

import com.pht.rinha_backend_2026.dto.FraudResponse;
import com.pht.rinha_backend_2026.dto.FraudRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class FraudController {

    @GetMapping(value = "/ready")
    public ResponseEntity<Void> ready() {
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/fraud-score")
    public ResponseEntity<FraudResponse> fraudScore(@RequestBody FraudRequest fraudRequest) {
        // Implementação de teste
        boolean approved = fraudRequest.transaction().amount() > 500 ? false : true;
        return ResponseEntity.ok(new FraudResponse(approved, 0.51));
    }
}
