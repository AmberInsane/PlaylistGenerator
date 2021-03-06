package com.amber.insane.enums;

import com.amber.insane.service.sorters.ISorter;
import com.amber.insane.service.sorters.RandomSorter;
import com.amber.insane.service.sorters.SmartSorter;

public enum OrderStrategy implements Item {
    RANDOMLY("Randomly") {
        @Override
        public ISorter getSorter() {
            return randomSorter;
        }
    },
    SMART_ORDER("Smart Sorter") {
        @Override
        public ISorter getSorter() {
            return smartSorter;
        }
    };

    String name;
    RandomSorter randomSorter = new RandomSorter();
    SmartSorter smartSorter = new SmartSorter();

    OrderStrategy(String name) {
        this.name = name;
    }

    public abstract ISorter getSorter();

    @Override
    public String getName() {
        return this.name;
    }
}
