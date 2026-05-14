package com.pht.rinha_backend_2026.services;

import com.pht.rinha_backend_2026.constants.DatasetConfig;
import com.pht.rinha_backend_2026.dto.Entry;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.PriorityQueue;


@Service
public class SearchService {

    private ByteBuffer vector;
    private ByteBuffer labels;

    public SearchService() throws IOException {
        // Local vector = Files.readAllBytes(Path.of("src/main/resources/static/vectors.u8"));
        // LOcal labels = Files.readAllBytes(Path.of("src/main/resources/static/labels.u8"));
        FileChannel vectorChannel = FileChannel.open(Path.of("/app/data/vectors.u8"));
        vector = vectorChannel.map(FileChannel.MapMode.READ_ONLY, 0, vectorChannel.size());
        FileChannel labelsChannel = FileChannel.open(Path.of("/app/data/labels.u8"));

        labels = labelsChannel.map(FileChannel.MapMode.READ_ONLY, 0, labelsChannel.size());
    }
    /**
     * Realiza o cálculo de similaridade entre os vetores e retorna o nº de fraudes
     * @param incomingTransaction Transação vinda do endpoint vetorizada
     * @return
     */
    public int numberOfFrauds(float[] incomingTransaction) {
        int manhattanDistance = 0;

        PriorityQueue<Entry> neigbhours = new PriorityQueue<>();

        for(int i = 0; i < vector.limit(); i += 14) {
            manhattanDistance = 0;
            for(int j = 0; j < 14; j++) {
                manhattanDistance += Math.abs( (int) (127 * incomingTransaction[j]) - vector.get(i + j));
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
            if (labels.get(n.index()) == DatasetConfig.LABEL_FRAUD) frauds++;
        }

        return frauds;
    }


}


