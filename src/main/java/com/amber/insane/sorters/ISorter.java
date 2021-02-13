package com.amber.insane.sorters;

import com.amber.insane.MusicFile;
import com.amber.insane.MusicType;

import java.util.List;
import java.util.Map;

public interface ISorter {
    List<MusicFile> sortFiles(Map<MusicType, List<MusicFile>> audioFiles);
}
