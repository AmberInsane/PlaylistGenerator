package com.amber.insane.enums;

public enum MenuItem implements Item {
    SHOW_MENU("Show menu"),
    SORT_PLAYLIST("Create playlist from sored folders"),
    SORT_FOLDERS("Sort music files to music type folders"),
    EXIT("Exit");

    String name;

    MenuItem(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
