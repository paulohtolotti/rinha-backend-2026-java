package com.pht.rinha_backend_2026.services;

import org.springframework.stereotype.Service;


@Service
public class SearchService {

    public SearchService() {

    }
    /**
     * Realiza o cálculo de similaridade entre os vetores e retorna o nº de fraudes
     * @param incomingTransaction Transação vinda do endpoint vetorizada
     * @return
     */
    public int numberOfFrauds(double[] incomingTransaction) {
        return 1;
    }
}


