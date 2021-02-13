package com.amber.insane;

import com.amber.insane.utils.Utils;
import lombok.Getter;

import java.io.File;
import java.net.URI;
import java.util.Comparator;

@Getter
public class MusicFile extends File implements Comparator<MusicFile> {

    private long duration;

    public MusicFile(String pathname) {
        super(pathname);
        initDuration();
    }

    public MusicFile(File file) {
        this(file.getPath());
    }

    public MusicFile(String parent, String child) {
        super(parent, child);
        initDuration();
    }

    public MusicFile(File parent, String child) {
        super(parent, child);
        initDuration();
    }

    public MusicFile(URI uri) {
        super(uri);
        initDuration();
    }

    private void initDuration() {
        this.duration = Utils.getAudioDuration(this);
    }

    @Override
    public int compare(MusicFile o1, MusicFile o2) {
        return 0;
    }

    public static class MusicFileDurationComparator implements Comparator<MusicFile> {
        @Override
        public int compare(MusicFile o1, MusicFile o2) {
            return Long.compare(o1.getDuration(), o2.getDuration());
        }
    }
}
