package com.amber.insane;

public enum MusicType {
    RELAX("/Relax") {
        @Override
        MusicType getNext() {
            return JOURNEY;
        }
    },
    MEDIUM ("/Medium") {
        @Override
        MusicType getNext() {
            return RELAX;
        }
    },
    JOURNEY("/Journey") {
        @Override
        MusicType getNext() {
            return HARD;
        }
    },
    HARD("/Hard") {
        @Override
        MusicType getNext() {
            return MEDIUM;
        }
    };

    public final String folderPath;

    MusicType(String folderPath) {
        this.folderPath = folderPath;
    }
    abstract MusicType getNext();
}
