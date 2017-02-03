package com.hammerox.sudokugen;

/**
 * Created by Mauricio on 20-Oct-16.
 */

public class JavaTest {

    public static void main(String[] args) {
        System.out.printf("helloworld\n");

        printRowGrid();
        printColGrid();
        printBoxGrid();
    }


    public static void printRowGrid() {
        byte[][] grid = new Board().getRowGrid();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.format("%2d ", grid[row][col]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }


    public static void printColGrid() {
        byte[][] grid = new Board().getColGrid();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                System.out.format("%2d ", grid[row][col]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }


    public static void printBoxGrid() {
        byte[][] grid = new Board().getBoxGrid();

        for (int box = 0; box < 9; box++) {
            for (int item = 0; item < 9; item++) {
                System.out.format("%2d ", grid[box][item]);
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

}
