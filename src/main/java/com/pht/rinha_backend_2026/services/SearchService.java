package com.pht.rinha_backend_2026.services;

import com.pht.rinha_backend_2026.constants.DatasetConfig;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;


@Service
public class SearchService {

    private final byte[] vectors;
    private final ByteBuffer labels;

    public SearchService() throws IOException {
        // Local: Path.of("src/main/resources/static/vectors.u8")
        vectors = Files.readAllBytes(Path.of("/app/data/vectors.u8"));
        FileChannel labelsChannel = FileChannel.open(Path.of("/app/data/labels.u8"));
        labels = labelsChannel.map(FileChannel.MapMode.READ_ONLY, 0, labelsChannel.size());
    }

    /**
     * Realiza o cálculo de similaridade entre os vetores e retorna o nº de fraudes
     * @param incomingTransaction Transação vinda do endpoint vetorizada
     * @return nº de fraudes entre os 5 vizinhos mais próximos
     */
    public int numberOfFrauds(float[] incomingTransaction) {
        final int[] q = new int[DatasetConfig.DIMENSIONS];
        for (int j = 0; j < DatasetConfig.DIMENSIONS; j++) {
            q[j] = (int) (127 * incomingTransaction[j]);
        }

        final byte[] v = vectors;
        final int len = v.length;

        final int[] nnIdx = new int[5];
        final int[] nnDist = new int[5];
        int count = 0;
        int worst = Integer.MAX_VALUE;
        int worstSlot = 0;

        for (int i = 0; i < len; i += DatasetConfig.DIMENSIONS) {
            int d = 0;
            for (int j = 0; j < DatasetConfig.DIMENSIONS; j++) {
                int diff = q[j] - v[i + j];
                d += diff * diff;
                if (d >= worst) {
                    d = -1;
                    break;
                }
            }
            if (d < 0) continue;

            if (count < 5) {
                nnIdx[count] = i / DatasetConfig.DIMENSIONS;
                nnDist[count] = d;
                count++;
                if (count == 5) {
                    worst = nnDist[0];
                    worstSlot = 0;
                    for (int k = 1; k < 5; k++) {
                        if (nnDist[k] > worst) {
                            worst = nnDist[k];
                            worstSlot = k;
                        }
                    }
                }
            } else {
                nnIdx[worstSlot] = i / DatasetConfig.DIMENSIONS;
                nnDist[worstSlot] = d;
                worst = nnDist[0];
                worstSlot = 0;
                for (int k = 1; k < 5; k++) {
                    if (nnDist[k] > worst) {
                        worst = nnDist[k];
                        worstSlot = k;
                    }
                }
            }
        }

        int frauds = 0;
        for (int k = 0; k < count; k++) {
            if (labels.get(nnIdx[k]) == DatasetConfig.LABEL_FRAUD) frauds++;
        }

        return frauds;
    }

}
