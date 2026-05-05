package com.pht.rinha_backend_2026.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class FraudController {

    @GetMapping(value = "/ready")
    public ResponseEntity<Void> teste2() {
        return ResponseEntity.noContent().build();
    }
}
