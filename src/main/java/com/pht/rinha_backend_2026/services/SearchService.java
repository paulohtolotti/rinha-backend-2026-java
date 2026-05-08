package com.pht.rinha_backend_2026.services;

import com.pht.rinha_backend_2026.constants.DatasetConfig;
import com.pht.rinha_backend_2026.dto.Entry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.PriorityQueue;


@Service
public class SearchService {

    private byte[] vector;
    private byte[] labels;

    public SearchService() throws IOException {
        vector = Files.readAllBytes(Path.of("src/main/resources/static/vectors.u8"));
        labels = Files.readAllBytes(Path.of("src/main/resources/static/labels.u8"));
    }
    /**
     * Realiza o cálculo de similaridade entre os vetores e retorna o nº de fraudes
     * @param incomingTransaction Transação vinda do endpoint vetorizada
     * @return
     */
    public int numberOfFrauds(float[] incomingTransaction) {
        int manhattanDistance = 0;

        PriorityQueue<Entry> neigbhours = new PriorityQueue<>();

        for(int i = 0; i < vector.length; i += 14) {
            manhattanDistance = 0;
            for(int j = 0; j < 14; j++) {
                manhattanDistance += Math.abs( (int) (127 * incomingTransaction[j]) - vector[i + j]);
            }


            if(neigbhours.size() < 5) {
                neigbhours.add(new Entry(i / 14, manhattanDistance));
                continue;
            }

            if(neigbhours.size() == 5 && neigbhours.peek().distance() > manhattanDistance) {
                neigbhours.poll();
                neigbhours.add(new Entry(i / 14, manhattanDistance));
            }


        }

        int frauds = 0;
        for(var n : neigbhours) {
            if (labels[n.index()] == DatasetConfig.LABEL_FRAUD) frauds++;

            System.out.println("Distance: " + n.distance());
            System.out.println("Index: " + n.index());
            System.out.println("Label: " + labels[n.index()]);
        }
        System.out.println("Frauds: " + frauds);

        System.out.println("===DEBUG===");
        int totalFrauds = 0;
        int totalLegits = 0;

        for(byte b : labels) {
            if(b == 0) totalLegits++;
            else totalFrauds++;
        }

        System.out.println("Legits: " + totalLegits);
        System.out.println("Frauds: " + totalFrauds);

        System.out.println("vector length" + vector.length);
        System.out.println("label length" + labels.length);
        return frauds;
    }


}


