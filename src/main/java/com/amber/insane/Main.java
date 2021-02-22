package com.amber.insane;

import com.amber.insane.controller.MenuController;

import java.util.Scanner;

import static com.amber.insane.utils.Utils.isValidPath;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello! Put in path to the folder with Music folder, please");

        Scanner in = new Scanner(System.in);
        String folderPath = in.nextLine();

        if (isValidPath(folderPath)) {
            MenuController menuController = new MenuController(folderPath);
            menuController.start();
        } else {
            System.err.println("Incorrect path");
        }
    }
}
