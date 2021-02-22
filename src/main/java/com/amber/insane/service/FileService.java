package com.amber.insane.service;

import com.amber.insane.entity.MusicFile;
import com.amber.insane.enums.MusicType;
import com.amber.insane.exceptions.FileManagerException;
import com.amber.insane.enums.OrderStrategy;

import java.io.*;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;


public class FileService {
    private final String MUSIC_FOLDER_PATH;

    private final static String MUSIC_DIR_NAME = "Music";
    private final static String MUSIC_LIST_FILE_NAME = "Music list.txt";

    private Map<MusicType, List<MusicFile>> audioFiles;
    private List<MusicFile> audioList;
    private File listFile;

    private final Logger logger = LogManager.getLogger(FileService.class);

    public FileService(String rootFolderPath) {
        this.MUSIC_FOLDER_PATH = rootFolderPath + "/" + MUSIC_DIR_NAME;
        this.listFile = new File(rootFolderPath + "/" + MUSIC_LIST_FILE_NAME);
    }

    /**
     * Reads files from category folders, creates text file with list of files, sort files using chosen strategy,
     * move files to common folder by order (adds order number to the beginning of the file name),
     * deletes category folders
     *
     * @param orderStrategy - strategy to order files in playlist
     */
    public void sortPlaylist(OrderStrategy orderStrategy) {
        initAudioFiles();
        saveTracksToFile();

        audioList = sortFiles(orderStrategy, audioFiles);
        moveFilesToCommonMusicFolder();
        deleteCategoriesFolders();
    }

    /**
     * Creates categories folders, sort files from common playlist folder to separate category folders using text
     * file with list of files
     */
    public void sortFolders() {
        createCategoriesFolders();
        initCategoriesFolders();
    }

    /**
     * Moves files to common playlist directory adding order number to file name
     */
    private void moveFilesToCommonMusicFolder() {
        for (int i = 0; i < audioList.size(); i++) {
            File file = audioList.get(i);
            File newFile = new File(MUSIC_FOLDER_PATH + "/" + String.format("%03d", i + 1) + ". " + file.getName());
            file.renameTo(newFile);
        }
    }

    /**
     * Deletes all category folders if they are empty
     */
    private void deleteCategoriesFolders() {
        for (MusicType value : MusicType.values()) {
            File dir = new File(MUSIC_FOLDER_PATH + value.folderPath);
            if (!dir.exists()) {
                logAndThrowFileManagerException(String.format("Directory %s does not exist", value.folderPath));
            } else {
                if (!dir.isDirectory()) {
                    logAndThrowFileManagerException(String.format("Sorry, could not delete %s directory because " +
                            "it's not a directory", value.folderPath));
                }
                if (Objects.requireNonNull(dir.listFiles()).length > 0) {
                    logAndThrowFileManagerException(String.format("Sorry, could not delete %s directory because " +
                            "it's not empty", value.folderPath));
                }
                boolean delete = dir.delete();
                if (delete) {
                    logger.info(String.format("Directory %s was deleted successfully", value.folderPath));
                } else {
                    logAndThrowFileManagerException(String.format("Sorry, could not delete %s directory", value.folderPath));
                }
            }
        }
    }

    /**
     * Creates category directories if they doesn't exist
     */
    private void createCategoriesFolders() {
        for (MusicType value : MusicType.values()) {
            File dir = new File(MUSIC_FOLDER_PATH + value.folderPath);
            if (dir.exists()) {
                logAndThrowFileManagerException(String.format("Directory %s already exists", value.folderPath));
            } else {
                boolean bool = dir.mkdir();
                if (bool) {
                    logger.info(String.format("Directory %s created successfully", value.folderPath));
                } else {
                    logAndThrowFileManagerException(String.format("Sorry, could not create %s directory", value.folderPath));
                }
            }
        }
    }

    /**
     * Creates list of files in chosen order strategy
     *
     * @param orderStrategy - strategy to order files in playlist
     * @param audioFiles    - map of categories with music files
     * @return ordered playlist
     */
    private List<MusicFile> sortFiles(OrderStrategy orderStrategy, Map<MusicType, List<MusicFile>> audioFiles) {
        return orderStrategy.getSorter().sortFiles(audioFiles);
    }

    /**
     * Collects music files from category folders to map by category
     */
    private void initAudioFiles() {
        audioFiles = new HashMap<>();
        for (MusicType musicType : MusicType.values()) {
            File musicFolder = new File(MUSIC_FOLDER_PATH + musicType.folderPath);
            List<File> folderFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(musicFolder.listFiles())));

            List<MusicFile> folderAudios = folderFiles.stream().map(MusicFile::new).collect(Collectors.toList());

            audioFiles.put(musicType, folderAudios);
        }
    }

    /**
     * Reads files to map using text file with list of files
     */
    private void initCategoriesFolders() {
        if (!listFile.exists()) {
            logAndThrowFileManagerException("List of audio files not found");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(listFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(String.format("Reads line %s", line));

                int categoryNum = Integer.parseInt(line.substring(0, 1));
                MusicType musicType = MusicType.values()[categoryNum];

                String trackName = line.substring(2);
                File directory = new File(MUSIC_FOLDER_PATH);

                //Verify if it is a valid file name
                if (!directory.exists()) {
                    logAndThrowFileManagerException(String.format("Directory %s does not exist", MUSIC_FOLDER_PATH));
                }

                //Verify if it is a directory and not a file path
                if (!directory.isDirectory()) {
                    logAndThrowFileManagerException(String.format("Provided value %s is not a directory", MUSIC_FOLDER_PATH));
                }

                File[] files = directory.listFiles(pathname -> pathname.getName().endsWith(trackName));
                if (files != null) {
                    File tempFile = files[0];
                    tempFile.renameTo(new File(MUSIC_FOLDER_PATH + musicType.folderPath + "/" + trackName));
                }
            }
        } catch (IOException e) {
            logAndThrowFileManagerException(String.format("Problem with reading from the file %s: %s", listFile.getName(), e.getMessage()));
        } catch (NumberFormatException |
                ArrayIndexOutOfBoundsException e) {
            logAndThrowFileManagerException(String.format("Problems with line format in the file: %s", listFile.getName()));
        }

    }

    /**
     * Saves list of files names with number of the category to the txt file
     */
    private void saveTracksToFile() {
        boolean fileExists = listFile.exists();
        if (!fileExists) {
            try {
                listFile.createNewFile();
            } catch (IOException e) {
                logAndThrowFileManagerException(String.format("Problems with creating music list file: %s", e.getMessage()));
            }
        }

        try (FileWriter writer = new FileWriter(listFile)) {
            audioFiles.forEach((key, value) -> {
                int categoryNum = key.ordinal();
                value.forEach(file -> {
                    try {
                        writer.write(categoryNum + " " + file.getName() + "\n");
                    } catch (IOException e) {
                        logAndThrowFileManagerException(String.format("Problem with writing to the file %s: %s", file.getName(), e.getMessage()));
                    }
                });
            });

            writer.flush();
        } catch (IOException e) {
            logAndThrowFileManagerException(String.format("Problems with writing to file: %s", e.getMessage()));
        }
    }


    /**
     * Logs an error and throws an exception
     *
     * @param errMessage - the error message
     */
    private void logAndThrowFileManagerException(String errMessage) {
        logger.error(errMessage);
        throw new FileManagerException(errMessage);
    }
}
