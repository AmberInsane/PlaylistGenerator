package com.amber.insane.service.sorters;

import com.amber.insane.entity.MusicFile;
import com.amber.insane.enums.MusicType;

import java.util.List;
import java.util.Map;

public interface ISorter {
    List<MusicFile> sortFiles(Map<MusicType, List<MusicFile>> audioFiles);
}
