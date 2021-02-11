package com.amber.insane.sorters;

import com.amber.insane.MusicType;
import com.amber.insane.Utils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

public class SmartSorter implements ISorter {
    private static int SET_LENGTH = 20; //20 minutes
    private static int MIN_TAIL_LENGTH = 10; //10 minutes

    @Override
    public List<File> sortFiles(Map<MusicType, List<File>> audioFiles) {
        List<File> playlist = new ArrayList<>();

        int setsNumber = calculateSetsNumber(audioFiles);
        System.out.println(setsNumber);

        while (setsNumber > 0) {
            setsNumber--;
        }
//        while (!audioFiles.isEmpty()) {
//            MusicType musicType = MusicType.JOURNEY;
//
//            List<File> files = audioFiles.get(musicType);
//            if (files.isEmpty()) {
//                audioFiles.remove(musicType);
//            } else {
//                playlist.add(files.remove(Utils.getArrayIndex(files.size())));
//            }
//        }

        return playlist;
    }

    /**
     * Calculates number of sets: finds the minimum of sets' duration, divides it by set length and adds one to have
     * one set at least
     *
     * @param audioFiles - music files splat by music type
     * @return number of sets
     */
    private int calculateSetsNumber(Map<MusicType, List<File>> audioFiles) {
        int numberOfSets = 0;

        Optional<Long> minMusicTypeDuration = audioFiles.entrySet()
                .stream()
                .map(entry -> entry.getValue().stream()
                        .map(Utils::getAudioDuration)
                        .mapToLong(Long::longValue).sum()).min(Long::compareTo);

        if (minMusicTypeDuration.isPresent()) {
            numberOfSets = ((int) (minMusicTypeDuration.get() / 60 / SET_LENGTH));
            if ((numberOfSets == 0)) {
                numberOfSets++;
            }
        }
        return numberOfSets;
    }
}
