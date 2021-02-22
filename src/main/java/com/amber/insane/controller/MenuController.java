package com.amber.insane.controller;

import com.amber.insane.enums.Item;
import com.amber.insane.enums.MenuItem;
import com.amber.insane.enums.OrderStrategy;
import com.amber.insane.service.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.amber.insane.enums.MenuItem.*;

public class MenuController {
    private static final String ERROR_MESSAGE = "Incorrect number of command, try again";

    private FileService fileService;
    private Scanner scanner = new Scanner(System.in);

    private final Logger logger = LogManager.getLogger(MenuController.class);

    public MenuController(String rootFolderPath) {
        logger.info(String.format("Creating file manager with the folder path %s", rootFolderPath));
        this.fileService = new FileService(rootFolderPath);
    }

    /**
     * Starts menu controller with preselected "Show menu" option
     */
    public void start() {
        MenuItem menuItem = SHOW_MENU;

        System.out.println("Let's do something");
        showMenu();
        while (menuItem != EXIT) {
            menuItem = (MenuItem) getItem(MenuItem.values(), false);
            commandAction(menuItem);
        }
    }

    /**
     * Creates string of acceptable items from enum
     *
     * @param items - enum of menu items
     * @return generated text of the menu
     */
    private String buildItemsString(Item[] items) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < items.length; i++) {
            sb.append(i).append(" ").append(items[i].getName()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Gives the selected item by the input number
     *
     * @param items - array of values of the items
     * @return selected item
     */
    private Item getItem(Item[] items, boolean toShowList) {
        if (toShowList) {
            System.out.println(buildItemsString(items));
        }

        Item tempItem = null;

        while (tempItem == null) {
            try {
                System.out.println("Put in command number, please");
                int commandNumber = scanner.nextInt();
                tempItem = items[commandNumber];

                logger.info(String.format("Selected command is %s", tempItem.getName()));
            } catch (Exception e) {
                System.out.println(ERROR_MESSAGE);
                scanner = new Scanner(System.in);
            }
        }
        return tempItem;
    }

    /**
     * Executes an action by chosen menu item
     *
     * @param menuItem - chosen menu item
     */
    private void commandAction(MenuItem menuItem) {
        try {
            switch (menuItem) {
                case SHOW_MENU:
                    showMenu();
                    break;
                case SORT_FOLDERS:
                    sortFolders();
                    break;
                case SORT_PLAYLIST:
                    sortPlaylist();
                    break;
                case EXIT:
                    System.out.println("Bye-bye");
            }
        } catch (RuntimeException e) {
            System.out.println("The next error was detected: " + e.getMessage());
        }
    }

    /**
     * Prints menu
     */
    private void showMenu() {
        System.out.println(buildItemsString(MenuItem.values()));
    }

    /**
     * Sort files to folders by categories using text file
     */
    private void sortFolders() {
        fileService.sortFolders();
        System.out.println("Files were sorted successfully");
    }

    /**
     * Creates ordered playlist from categories folders
     */
    private void sortPlaylist() {
        OrderStrategy strategy = (OrderStrategy) getItem(OrderStrategy.values(), true);
        fileService.sortPlaylist(strategy);
        System.out.println("Playlist was generated successfully");
    }
}
