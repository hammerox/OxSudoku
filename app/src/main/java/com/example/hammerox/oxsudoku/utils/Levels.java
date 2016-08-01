package com.example.hammerox.oxsudoku.utils;


import java.util.ArrayList;
import java.util.List;

public class Levels {

    public final static int LEVEL_EASY = 0;
    public final static int LEVEL_MEDIUM = 1;
    public final static int LEVEL_HARD = 2;

    // Difficulty is the number of the puzzle's empty cells
    private final static int DIFFICULTY_EASY = 45;
    private final static int DIFFICULTY_MEDIUM = 55;
    private final static int DIFFICULTY_HARD = 80;  // Max value

    private final static String FILENAME_EASY = "level_easy";
    private final static String FILENAME_MEDIUM = "level_medium";
    private final static String FILENAME_HARD = "level_hard";

    private final static List<String> fileNameList = new ArrayList<>();

    static {
        fileNameList.add(FILENAME_EASY);
        fileNameList.add(FILENAME_MEDIUM);
        fileNameList.add(FILENAME_HARD);
    }


    public static int getDifficulty(int level) {
        switch (level) {        // Get level's respective difficulty
            case LEVEL_EASY:
                return DIFFICULTY_EASY;
            case LEVEL_MEDIUM:
                return DIFFICULTY_MEDIUM;
            case LEVEL_HARD:
                return DIFFICULTY_HARD;
        }

        // If level not found, return full grid
        return 0;
    }


    public static String getFileName(int level) {
        return fileNameList.get(level);
    }


    public static List<String> getFileNameList() {
        return fileNameList;
    }
}
