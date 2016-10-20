package com.hammerox.sudokugen;

/**
 * Created by Mauricio on 20-Oct-16.
 */

public class JavaTest {

    public static void main(String[] args) {
        System.out.printf("helloworld\n");

        printGrid();
    }


    public static void printGrid() {
        byte[][] grid = Grid.getInstance().getGrid();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.format("%2d ", grid[row][col]);
            }
            System.out.print("\n");
        }
    }

}
