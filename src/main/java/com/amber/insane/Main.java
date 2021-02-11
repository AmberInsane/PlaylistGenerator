package com.amber.insane;

import com.amber.insane.menu.MenuController;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

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

    private static boolean isValidPath(String pathName) {
        try {
            Path path = Paths.get(pathName);
            return path.getRoot() != null;
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }

    }
}
