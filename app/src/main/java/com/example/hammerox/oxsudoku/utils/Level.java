package com.example.hammerox.oxsudoku.utils;

/**
 * Created by Mauricio on 13-Jan-17.
 */

public enum Level {

    EASY    (45, "level_easy"),
    MEDIUM  (55, "level_medium"),
    HARD    (80, "level_hard");

    public final int emptyCells;
    public final String fileName;

    Level(int emptyCells, String fileName) {
        this.emptyCells = emptyCells;
        this.fileName = fileName;
    }
}
