package com.amber.insane.service.sorters;

import com.amber.insane.entity.MusicFile;
import com.amber.insane.enums.MusicType;
import com.amber.insane.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RandomSorter implements ISorter {
    @Override
    public List<MusicFile> sortFiles(Map<MusicType, List<MusicFile>> audioFiles) {
        List<MusicFile> playlist = new ArrayList<>();

        while (!audioFiles.isEmpty()) {
            Set<MusicType> musicTypes = audioFiles.keySet();
            MusicType musicType = (MusicType) musicTypes.toArray()[Utils.getRandomArrayIndex(musicTypes.size())];

            List<MusicFile> files = audioFiles.get(musicType);
            if (files.isEmpty()) {
                audioFiles.remove(musicType);
            } else {
                playlist.add(files.remove(Utils.getRandomArrayIndex(files.size())));
            }
        }

        return playlist;
    }
}
