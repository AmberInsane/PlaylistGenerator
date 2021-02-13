package com.amber.insane.menu;

import com.amber.insane.FilesManager;
import com.amber.insane.Item;
import com.amber.insane.sorters.OrderStrategy;

import java.util.*;

import static com.amber.insane.menu.MenuItem.*;

public class MenuController {
    private static final String ERROR_MESSAGE = "Incorrect number of command, try again";

    private FilesManager filesManager;
    private Scanner scanner = new Scanner(System.in);


    public MenuController(String rootFolderPath) {
        this.filesManager = new FilesManager(rootFolderPath);
    }

    public void start() {
        MenuItem menuItem = SHOW_MENU;

        System.out.println("Let's do something");
        while (menuItem != EXIT) {
            menuItem = (MenuItem) getItem(MenuItem.values());
            commandAction(menuItem);
        }
    }

    private String buildItemsString(Item[] items) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < items.length; i++) {
            sb.append(i).append(" ").append(items[i].getName()).append("\n");
        }
        return sb.toString();
    }

    private Item getItem(Item[] items) {
        System.out.println(buildItemsString(items));
        Item tempItem = null;

        while (tempItem == null) {
            try {
                System.out.println("Put in command number, please");
                int commandNumber = scanner.nextInt();
                tempItem = items[commandNumber];
            } catch (Exception e) {
                System.out.println(ERROR_MESSAGE);
                scanner = new Scanner(System.in);
            }
        }
        return tempItem;
    }

    private void commandAction(MenuItem menuItem) {
        switch (menuItem) {
            case SHOW_MENU:
                showMenu();
                break;
            case SORT_FOLDERS:
                try {
                    sortFolders();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SORT_PLAYLIST:
                try {
                    sortPlaylist();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case EXIT:
                System.out.println("Bye-bye");
        }
    }

    private void showMenu() {
        System.out.println(buildItemsString(MenuItem.values()));
    }

    private void sortFolders() throws Exception {
        filesManager.sortFolders();
    }

    private void sortPlaylist() throws Exception {
        OrderStrategy strategy = (OrderStrategy) getItem(OrderStrategy.values());
        filesManager.sortPlaylist(strategy);
    }
}
