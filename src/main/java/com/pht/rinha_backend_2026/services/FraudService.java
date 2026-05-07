package com.pht.rinha_backend_2026.services;

import com.pht.rinha_backend_2026.constants.Constants;
import com.pht.rinha_backend_2026.dto.FraudRequest;
import com.pht.rinha_backend_2026.dto.FraudResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Hashtable;
import java.util.Map;

@Service
public class FraudService {

    private final SearchService searchService;

    public FraudService(SearchService searchService) {
        this.searchService = searchService;
    }

    public FraudResponse fraudScore(FraudRequest request) {
        float[] arr = vectorize(request);
        int i = searchService.numberOfFrauds(arr);
        return new FraudResponse(false, 1.0f);
    }

    /**
     * Vetoriza uma requisição usando as regras do desafio.
     * A classe Constants contém as constantes de normalização
     * Referência das regras de normalização https://github.com/zanfranceschi/rinha-de-backend-2026/blob/main/docs/br/REGRAS_DE_DETECCAO.md
     * @param request Objeto da requisição
     * @return vetor de 14 posições normalizado.
     */
    public float[] vectorize(FraudRequest request) {
        Constants constants = new Constants();
        float[] arr = new float[14];

        // amount
        arr[0] = limit(request.transaction().amount(), constants.max_amount);
        // installments
        arr[1] = limit(request.transaction().installments(), constants.max_installments);
        //amount_vs_avg
        arr[2] = limit(request.transaction().amount() / request.customer().avg_amount(),
                constants.amount_vs_avg_ratio);
        // Hour of day
        arr[3] = hour(request.transaction().requested_at());
        // Day of week
        arr[4] = day(request.transaction().requested_at());
        // Bloco de last transaction
        if(request.last_transaction() == null) {
            arr[5] = -1.0f;
            arr[6] = -1.0f;
        }
        else {
            var minutes = ChronoUnit.MINUTES.between(request.transaction().requested_at(),
                    request.last_transaction().timestamp());
            arr[5] = limit(minutes, constants.max_minutes);
            arr[6] = limit(request.last_transaction().km_from_current(), constants.max_km);
        }

        arr[7] = limit(request.terminal().km_from_home(), constants.max_km);
        arr[8] = limit(request.customer().tx_count_24h(), constants.max_tx_count_24h);
        arr[9] = request.terminal().is_online() ? 1.0f : 0.0f;
        arr[10] = request.terminal().card_present() ? 1.0f : 0.0f;
        arr[11] = (float) containsMerchant(request.customer().known_merchants(),
                request.merchant().id());
        arr[12] = mccRisk(request.merchant().mcc());
        arr[13] = limit(request.merchant().avg_amount(), constants.max_merchant_avg_amount);
        return arr;
    }

    private float limit(int value, int limit) {

        float res = (float) value / limit;

        if(res < 0) {
            return 0.0f;
        }

        if(res > 1) {
            return 1.0f;
        }

        return res;
    }

    private float limit(float value, float limit) {
        float res = value / limit;

        if(res < 0) {
            return 0.0f;
        }

        if(res > 1) {
            return 1.0f;
        }

        return res;
    }

    private float hour(Instant time) {
        String isoString = time.toString();

        String hourString = isoString.split("-")[2].split("T")[1].split(":")[0];
        return Float.parseFloat(hourString) / 23;

    }

    private float day(Instant time) {
        return (float) time.atOffset(ZoneOffset.UTC).getDayOfWeek().ordinal() / 6;
    }

    private float containsMerchant(String[] merchants, String merchantId) {
        for(String merchant : merchants) {
            if(merchant.equals(merchantId)) {
                return 0.0f;
            }
        }
        return 1.0f;
    }

    private float mccRisk(String merchantID) {

        Map<String, Float> riskMap = new Hashtable<>(Map.of(
                "5411", 0.15f,
                "5812", 0.30f,
                "5912", 0.20f,
                "5944", 0.45f,
                "7801", 0.80f,
                "7802", 0.75f,
                "7995", 0.85f,
                "4511", 0.35f,
                "5311", 0.25f,
                "5999", 0.50f
        ));

        return riskMap.getOrDefault(merchantID, 0.5f);
    }
}
