package com.example.hammerox.oxsudoku.utils;


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
        switch (level) {        // Get level's respective tag name
            case LEVEL_EASY:
                return FILENAME_EASY;
            case LEVEL_MEDIUM:
                return FILENAME_MEDIUM;
            case LEVEL_HARD:
                return FILENAME_HARD;
        }

        return null;
    }
}
