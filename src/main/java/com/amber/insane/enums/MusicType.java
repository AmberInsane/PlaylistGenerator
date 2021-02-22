package com.amber.insane.enums;

public enum MusicType {
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
    },
    MEDIUM ("/Medium") {
        @Override
        MusicType getNext() {
            return RELAX;
        }
    },
    RELAX("/Relax") {
        @Override
        MusicType getNext() {
            return JOURNEY;
        }
    };

    public final String folderPath;

    MusicType(String folderPath) {
        this.folderPath = folderPath;
    }
    abstract MusicType getNext();
}
