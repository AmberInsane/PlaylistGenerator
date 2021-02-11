package com.amber.insane.sorters;

import com.amber.insane.MusicType;
import com.amber.insane.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RandomSorter implements ISorter {
    @Override
    public List<File> sortFiles(Map<MusicType, List<File>> audioFiles) {
        List<File> playlist = new ArrayList<>();

        while (!audioFiles.isEmpty()) {
            Set<MusicType> musicTypes = audioFiles.keySet();
            MusicType musicType = (MusicType) musicTypes.toArray()[Utils.getArrayIndex(musicTypes.size())];

            List<File> files = audioFiles.get(musicType);
            if (files.isEmpty()) {
                audioFiles.remove(musicType);
            } else {
                playlist.add(files.remove(Utils.getArrayIndex(files.size())));
            }
        }

        return playlist;
    }
}
