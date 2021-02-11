package com.amber.insane.sorters;

import com.amber.insane.MusicType;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ISorter {
    List<File> sortFiles(Map<MusicType, List<File>> audioFiles);
}
