package com.amber.insane.service.sorters;

import com.amber.insane.entity.MusicFile;
import com.amber.insane.enums.MusicType;
import com.amber.insane.exceptions.SorterException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.amber.insane.utils.MatrixUtils.*;

public class SmartSorter implements ISorter {
    private static int SET_LENGTH = 20; //20 minutes
    private static int MATRIX_STEP = 10; //10 seconds

    private final Logger logger = LogManager.getLogger(SmartSorter.class);

    @Override
    public List<MusicFile> sortFiles(Map<MusicType, List<MusicFile>> audioFiles) {
        List<MusicFile> playlist = new ArrayList<>();

        int setsNumber = calculateSetsNumber(audioFiles);

        if (setsNumber == 0) {
            throw new SorterException(String.format("Playlist is too short to be generated. " +
                    "Minimum length of the set is %d seconds", SET_LENGTH * 60));
        }
        logger.info("Sets number is " + setsNumber);

        audioFiles.values().forEach(list -> list.sort(new MusicFile.MusicFileDurationComparator()));

        MusicType[] musicTypes = MusicType.values();

        while (setsNumber > 0) {
            for (MusicType musicType : musicTypes) {
                List<MusicFile> musicFiles = audioFiles.get(musicType);

                if (setsNumber > 1) {
                    long commonDuration = musicFiles.stream().mapToLong(MusicFile::getDuration).sum();
                    long setDuration = commonDuration / setsNumber;

                    logger.info("musicType " + musicType);
                    logger.info("maxSetDuration " + setDuration);

                    List<MusicFile> musicFilesSorted = extractSublist(musicFiles, setDuration);

                    playlist.addAll(musicFilesSorted);
                } else {
                    playlist.addAll(musicFiles);//for 1 just take all leftovers
                }
            }

            setsNumber--;
        }

        return playlist;
    }

    private List<MusicFile> extractSublist(List<MusicFile> musicFiles, long maxSetDuration) {
        long[] arrayOfDurations = createAndFillArrayOfDurations(musicFiles.get(0).getDuration(), maxSetDuration);

        long[][] dynamicMatrix = createAndFillBackpackMatrix(musicFiles, arrayOfDurations);

        return extractMusicFiles(musicFiles, arrayOfDurations, dynamicMatrix);
    }

    private List<MusicFile> extractMusicFiles(List<MusicFile> musicFiles, long[] arrayOfDurations, long[][] dynamicMatrix) {
        List<MusicFile> music = new ArrayList<>();

        int currentColumnIndex = arrayOfDurations.length - 1;

        int currentMaxIndex = dynamicMatrix.length - 1;

        long leftDuration = dynamicMatrix[currentMaxIndex][currentColumnIndex];
        long firstColumnDuration = arrayOfDurations[0];

        while (leftDuration > 0) {
            int lastRowIndex = currentMaxIndex;
            long currentMax = -1;

            logger.debug(currentMaxIndex + " " + currentMax + " " + currentColumnIndex);

            for (int i = 0; i < lastRowIndex; i++) {
                long curElem = dynamicMatrix[i][currentColumnIndex];
                if (curElem == -1) break;
                if (currentMax <= curElem) {
                    currentMax = curElem;
                    currentMaxIndex = i;
                }
            }

            MusicFile selectedFile = musicFiles.remove(currentMaxIndex);

            leftDuration = currentMax - selectedFile.getDuration();

            if (leftDuration >= 0) {
                currentColumnIndex = (int) ((float) (leftDuration - firstColumnDuration) / MATRIX_STEP);
                logger.info("selected " + selectedFile.getName() + " " + selectedFile.getDuration() + " left " + leftDuration);

                music.add(selectedFile);
            }
        }

        return music;
    }

    private long[] createAndFillArrayOfDurations(long shortestDuration, long maxSetDuration) {
        long firstColumnDuration = shortestDuration - MATRIX_STEP;//to create fake column
        int stepsNumber = (int) Math.ceil(((float) (maxSetDuration - firstColumnDuration) / MATRIX_STEP)) + 1;

        long[] arrayOfDurations = new long[stepsNumber];
        for (int i = 0; i < arrayOfDurations.length - 1; i++) {
            arrayOfDurations[i] = firstColumnDuration + MATRIX_STEP * i;
        }

        arrayOfDurations[arrayOfDurations.length - 1] = maxSetDuration;

        return arrayOfDurations;
    }

    private long[][] createAndFillBackpackMatrix(List<MusicFile> musicFiles, long[] arrayOfDurations) {
        long firstColumnDuration = arrayOfDurations[0];
        long[][] dynamicMatrix = createBackpackMatrix(musicFiles.size(), arrayOfDurations.length);


        for (int i = 0; i < dynamicMatrix.length; i++) {
            long currentDuration = musicFiles.get(i).getDuration();

            for (int j = 0; j < dynamicMatrix[i].length; j++) {
                long maxDuration = arrayOfDurations[j];

                if (currentDuration <= maxDuration) {
                    dynamicMatrix[i][j] = currentDuration;

                    long leftDuration = maxDuration - currentDuration;

                    if (leftDuration > firstColumnDuration) {
                        int prevColumnNum = (int) ((float) (leftDuration - firstColumnDuration)) / MATRIX_STEP;
                        //logger.debug("currentDuration " + currentDuration + " leftDuration " + leftDuration + " prevColumnNum " + prevColumnNum + " value " + arrayOfDurations[prevColumnNum]);
                        long prevMax = -1;

                        for (int k = 0; k < i; k++) {
                            if (dynamicMatrix[k][prevColumnNum] > prevMax) {
                                prevMax = dynamicMatrix[k][prevColumnNum];
                            }
                        }

                        if (prevMax > -1) {
                            dynamicMatrix[i][j] += prevMax;
                        }
                    }
                }
            }
        }

        logger.debug(getTablePrint(arrayOfDurations, dynamicMatrix));

        return dynamicMatrix;
    }

    /**
     * Calculates number of sets: finds the minimum of sets' duration, divides it by set length and round to greater to
     * have one set at least
     *
     * @param audioFiles - music files splat by music type
     * @return number of sets
     */
    private int calculateSetsNumber(Map<MusicType, List<MusicFile>> audioFiles) {
        int numberOfSets = 0;

        Optional<Long> minMusicTypeDuration = audioFiles.entrySet()
                .stream()
                .map(entry -> entry.getValue().stream()
                        .mapToLong(MusicFile::getDuration)
                        .sum())
                .min(Long::compareTo);

        if (minMusicTypeDuration.isPresent()) {
            numberOfSets = (int) ((float) minMusicTypeDuration.get() / 60 / SET_LENGTH);
        }
        return numberOfSets;
    }
}
