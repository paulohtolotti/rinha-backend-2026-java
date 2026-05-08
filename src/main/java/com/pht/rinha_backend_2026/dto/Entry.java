package com.pht.rinha_backend_2026.dto;

public record Entry(int index, int distance) implements Comparable<Entry>{

    @Override
    public int compareTo(Entry o) {
        if(o.distance() > this.distance()) return 1;

        if(o.distance() == this.distance()) return 0;

        return -1;
    }
}
