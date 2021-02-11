package com.amber.insane.sorters;

import com.amber.insane.Item;

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
    public Item[] getValues() {
        return values();
    }

    @Override
    public String getName() {
        return this.name;
    }
}
