package com.hammerox.sudokugen;

/**
 * Created by Mauricio on 20-Oct-16.
 */
public class Grid {

    private final static Grid instance;
    private static byte[][] grid = new byte[9][9];

    private Grid() {}

    static {

        try{
            instance = new Grid();
        } catch(Exception e) {
            throw new RuntimeException("Exception occured in creating singleton instance");
        }

        byte index = 0;
        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                grid[row][col] = index++;

            }

        }

    }


    public static Grid getInstance(){
        return instance;
    }


    public byte[][] getGrid() {
        return grid;
    }

}
