package com.amber.insane;

import com.amber.insane.sorters.OrderStrategy;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class FilesManager {
    private final String MUSIC_FOLDER_PATH;

    private final static String MUSIC_DIR_NAME = "Music";
    private final static String MUSIC_LIST_FILE_NAME = "Music list.txt";

    private Map<MusicType, List<MusicFile>> audioFiles;
    private List<MusicFile> audioList;
    private File listFile;

    public FilesManager(String rootFolderPath) {
        this.MUSIC_FOLDER_PATH = rootFolderPath + "/" + MUSIC_DIR_NAME;
        this.listFile = new File(rootFolderPath + "/" + MUSIC_LIST_FILE_NAME);
    }

    private void moveFilesToCommonMusicFolder() {
        for (int i = 0; i < audioList.size(); i++) {
            File file = audioList.get(i);
            File newFile = new File(MUSIC_FOLDER_PATH + "/" + String.format("%03d", i + 1) + ". " + file.getName());
            file.renameTo(newFile);
        }
    }

    private void deleteCategoriesFolders() {
        for (MusicType value : MusicType.values()) {
            File dir = new File(MUSIC_FOLDER_PATH + value.folderPath);
            if (!dir.exists()) {
                System.out.println("Directory " + value.folderPath + " does not exists");
            } else {
                boolean delete = dir.delete();
                if (delete) {
                    System.out.println("Directory " + value.folderPath + " deleted successfully");
                } else {
                    System.out.println("Sorry couldn’t delete " + value.folderPath + " directory");
                }
            }
        }
    }

    private void createCategoriesFolders() {
        for (MusicType value : MusicType.values()) {
            File dir = new File(MUSIC_FOLDER_PATH + value.folderPath);
            if (dir.exists()) {
                System.out.println("Directory " + value.folderPath + " already exists");
            } else {
                boolean bool = dir.mkdir();
                if (bool) {
                    System.out.println("Directory " + value.folderPath + " created successfully");
                } else {
                    System.out.println("Sorry could not create " + value.folderPath + " directory");
                }
            }
        }
    }

    private List<MusicFile> sortFiles(OrderStrategy orderStrategy, Map<MusicType, List<MusicFile>> audioFiles) {
        return orderStrategy.getSorter().sortFiles(audioFiles);
    }

    private void initAudioFiles() {
        audioFiles = new HashMap<>();
        for (MusicType musicType : MusicType.values()) {
            File musicFolder = new File(MUSIC_FOLDER_PATH + musicType.folderPath);
            List<File> folderFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(musicFolder.listFiles())));

            List<MusicFile> folderAudios = folderFiles.stream().map(MusicFile::new).collect(Collectors.toList());

            audioFiles.put(musicType, folderAudios);
        }
    }

    private void initCategoriesFolders() throws Exception {
        if (!listFile.exists()) {
            throw new Exception("List of audio files not found");
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(listFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                int categoryNum = Integer.parseInt(line.substring(0, 1));
                MusicType musicType = MusicType.values()[categoryNum];

                String trackName = line.substring(2);
                File directory = new File(MUSIC_FOLDER_PATH);

                //Verify if it is a valid file name
                if (!directory.exists()) {
                    System.out.println(String.format("Directory %s does not exist", MUSIC_FOLDER_PATH));
                    return;
                }

                //Verify if it is a directory and not a file path
                if (!directory.isDirectory()) {
                    System.out.println(String.format("Provided value %s is not a directory", MUSIC_FOLDER_PATH));
                    return;
                }

                File[] files = directory.listFiles(pathname -> pathname.getName().endsWith(trackName));
                if (files != null) {
                    File tempFile = files[0];
                    tempFile.renameTo(new File(MUSIC_FOLDER_PATH + musicType.folderPath + "/" + trackName));
                }
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException |
                ArrayIndexOutOfBoundsException e) {
            System.err.println("Problems with line format in the file " + listFile.getName());
        }

    }

    private void saveTracksToFile() throws IOException {
        boolean fileExists = listFile.exists();
        if (!fileExists) {
            fileExists = listFile.createNewFile();
        }

        if (fileExists) {
            FileWriter writer = new FileWriter(listFile);

            audioFiles.forEach((key, value) -> {
                int categoryNum = key.ordinal();
                value.forEach(file -> {
                    try {
                        writer.write(categoryNum + " " + file.getName() + "\n");
                    } catch (IOException e) {
                        System.err.println("Problem with writing file " + file.getName() + ": " + e.getMessage());
                    }
                });
            });

            writer.flush();
            writer.close();
        } else {
            System.err.println("Problems with creating music list file");
        }
    }

    public void sortPlaylist(OrderStrategy orderStrategy) throws Exception {
        initAudioFiles();
        saveTracksToFile(); //if error do not continue

        audioList = sortFiles(orderStrategy, audioFiles);
        moveFilesToCommonMusicFolder();
        deleteCategoriesFolders();
    }

    public void sortFolders() throws Exception {
        createCategoriesFolders();
        initCategoriesFolders();
    }
}
