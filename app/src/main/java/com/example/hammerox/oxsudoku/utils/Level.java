package com.example.hammerox.oxsudoku.utils;

/**
 * Created by Mauricio on 13-Jan-17.
 */

public enum Level {

    EASY(0, 45, "level_easy"),
    MEDIUM(1, 55, "level_medium"),
    HARD(2, 80, "level_hard");

    public final int id;
    public final int emptyCells;
    public final String fileName;

    Level(int id, int emptyCells, String fileName) {
        this.id = id;
        this.emptyCells = emptyCells;
        this.fileName = fileName;
    }
}
