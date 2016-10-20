package com.hammerox.sudokugen;

/**
 * Created by Mauricio on 20-Oct-16.
 */

public class Box extends GridPosition {

    public final static byte[] BOX_TOP_LEFT =        new byte[]{ 0,  1,  2,  9, 10, 11, 18, 19, 20};
    public final static byte[] BOX_TOP_CENTER =      new byte[]{ 3,  4,  5, 12, 13, 14, 21, 22, 23};
    public final static byte[] BOX_TOP_RIGHT =       new byte[]{ 6,  7,  8, 15, 16, 17, 24, 25, 26};
    public final static byte[] BOX_CENTER_LEFT =     new byte[]{27, 28, 29, 36, 37, 38, 45, 46, 47};
    public final static byte[] BOX_CENTER_CENTER =   new byte[]{30, 31, 32, 39, 40, 41, 48, 49, 50};
    public final static byte[] BOX_CENTER_RIGHT =    new byte[]{33, 34, 35, 42, 43, 44, 51, 52, 53};
    public final static byte[] BOX_BOTTOM_LEFT =     new byte[]{54, 55, 56, 63, 64, 65, 72, 73, 74};
    public final static byte[] BOX_BOTTOM_CENTER =   new byte[]{57, 58, 59, 66, 67, 68, 75, 76, 77};
    public final static byte[] BOX_BOTTOM_RIGHT =    new byte[]{60, 61, 62, 69, 70, 71, 78, 79, 80};


    public static byte[] getBoxIndexes(int row, int col) {

        if (isBetween(row, 1, 3)) {

            return getBoxOnTop(col);

        } else if (isBetween(row, 4, 6)) {

            return getBoxOnCenter(col);

        } else if (isBetween(row, 7, 9)) {

            return getBoxOnBottom(col);

        }

        return null;

    }


    public static byte[] getBoxIndexes(int index) {

        int[] position = getPositionFromIndex(index);

        return getBoxIndexes(position[0], position[1]);

    }


    public static byte[] getBoxOnTop(int col) {

        if (isBetween(col, 1, 3)) {

            return BOX_TOP_LEFT;

        } else if (isBetween(col, 4, 6)) {

            return BOX_TOP_CENTER;

        } else if (isBetween(col, 7, 9)) {

            return BOX_TOP_RIGHT;

        }

        return null;
    }


    public static byte[] getBoxOnCenter(int col) {

        if (isBetween(col, 1, 3)) {

            return BOX_CENTER_LEFT;

        } else if (isBetween(col, 4, 6)) {

            return BOX_CENTER_CENTER;

        } else if (isBetween(col, 7, 9)) {

            return BOX_CENTER_RIGHT;

        }

        return null;
    }


    public static byte[] getBoxOnBottom(int col) {

        if (isBetween(col, 1, 3)) {

            return BOX_BOTTOM_LEFT;

        } else if (isBetween(col, 4, 6)) {

            return BOX_BOTTOM_CENTER;

        } else if (isBetween(col, 7, 9)) {

            return BOX_BOTTOM_RIGHT;

        }

        return null;

    }


    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

}
