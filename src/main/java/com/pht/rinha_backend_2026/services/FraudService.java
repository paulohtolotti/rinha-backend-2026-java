package com.pht.rinha_backend_2026.services;

import com.pht.rinha_backend_2026.constants.Constants;
import com.pht.rinha_backend_2026.dto.FraudRequest;
import com.pht.rinha_backend_2026.dto.FraudResponse;
import com.pht.rinha_backend_2026.dto.VectorizedTransaction;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class FraudService {

//    private final SearchService searchService;

//    public FraudService(SearchService searchService) {
//        this.searchService = searchService;
//    }

    public FraudResponse fraudScore(FraudRequest request) {
        double[] arr = vectorize(request);

        return new FraudResponse(false, 1.0);
    }

    /**
     * Vetoriza uma requisição usando as regras do desafio.
     * A classe Constants contém as constantes de normalização
     * Referência das regras de normalização https://github.com/zanfranceschi/rinha-de-backend-2026/blob/main/docs/br/REGRAS_DE_DETECCAO.md
     * @param request Objeto da requisição
     * @return vetor de 14 posições normalizado.
     */
    private double[] vectorize(FraudRequest request) {
        Constants constants = new Constants();
        double[] arr = new double[14];

        // amount
        arr[0] = limit(request.transaction().amount(), constants.max_amount);
        // installments
        arr[1] = limit(request.transaction().installments(), constants.max_installments);
        //amount_vs_avg
        arr[2] = limit(request.transaction().amount() / request.customer().avg_amount(),
                constants.amount_vs_avg_ratio);
        arr[3] = hour(request.transaction().requested_at());

        if(request.last_transaction() == null) {
            arr[5] = -1.0;
            arr[6] = -1.0;
        }
        else {

        }
        return arr;
    }

    private double calculateFraudScore(List<VectorizedTransaction> kneighbour) {
        double fraudCounter = 0;
        for(VectorizedTransaction k : kneighbour) {
            if (k.label().equals("fraud")) fraudCounter++;
        }

        return fraudCounter / 5;
    }

    private double limit(int value, int limit) {

        double res = (double) value / limit;

        if(res < 0) {
            return 0;
        }

        if(res > 1) {
            return 1;
        }

        return res;
    }

    private double limit(double value, double limit) {
        double res = value / limit;

        if(res < 0) {
            return 0;
        }

        if(res > 1) {
            return 1;
        }

        return res;
    }

    private int hour(Instant time) {
        String isoString = time.toString();
        int day = Integer.parseInt(
                isoString.split("-")[2].split("T")[0]
        );
        return day / 23;
    }
}
