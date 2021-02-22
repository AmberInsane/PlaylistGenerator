package com.amber.insane.utils;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    public static int getRandomArrayIndex(int arraySize) {
        return (int) (arraySize * Math.random());
    }

    public static long getAudioDuration(File file) {
        long duration = 0L;
        try {
            AudioFile audioFile = AudioFileIO.read(file);
            duration = audioFile.getAudioHeader().getTrackLength();
        } catch (CannotReadException | InvalidAudioFrameException | ReadOnlyFileException | TagException | IOException e) {
            e.printStackTrace();
        }
        return duration;
    }

    public static boolean isValidPath(String pathName) {
        try {
            Path path = Paths.get(pathName);
            return path.getRoot() != null;
        } catch (InvalidPathException | NullPointerException ex) {
            return false;
        }
    }

}
