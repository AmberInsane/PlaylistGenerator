package com.amber.insane;

import com.amber.insane.controller.MenuController;

import java.util.Scanner;

import static com.amber.insane.utils.Utils.isValidPath;

public class Main {

    public static void main(String[] args) {
        String folderPath = args[0];

        if (isValidPath(folderPath)) {
            MenuController menuController = new MenuController(folderPath);
            menuController.start();
        } else {
            System.err.println("Incorrect folder path");
        }
    }
}
