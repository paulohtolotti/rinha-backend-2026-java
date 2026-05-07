package com.pht.rinha_backend_2026.constants;

public final class DatasetConfig {

    // Nº de dimensões do vetor
    public static final int DIMENSIONS = 14;
    public static final int RECORDS = 3_000_000;

    // Bytes usados para transformar a flag de String para Byte
    public static final byte LABEL_LEGIT = 0;
    public static final byte LABEL_FRAUD = 1;

    private DatasetConfig() {

    }
}
