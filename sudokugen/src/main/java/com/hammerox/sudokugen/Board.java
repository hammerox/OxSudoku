package com.hammerox.sudokugen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mauricio on 20-Oct-16.
 */
public class Board extends ArrayList<Cell> {


    public Board() {
        initializeBoard();
    }


    public Cell get(Position position) {
        int index = getIndex(position);
        return super.get(index);
    }

    public Cell get(int row, int col) {
        int index = getIndex(row, col);
        return super.get(index);
    }

    public static Position getPosition(int index) {
        int row = index / 9 + 1;
        int col = index % 9 + 1;
        return new Position(row, col);
    }

    public static int getIndex(int row, int col) {
        return 9 * (row - 1) + col - 1;
    }

    public static int getIndex(Position position) {
        return getIndex(position.row, position.col);
    }

    public static Box getBox(int row, int col) {
        if (isBetween(row, 1, 3)) {
            return getTopBox(col);
        } else if (isBetween(row, 4, 6)) {
            return getCenterBox(col);
        } else if (isBetween(row, 7, 9)) {
            return getBottomBox(col);
        }
        throw new IndexOutOfBoundsException();
    }

    public static Box getBox(Position position) {
        return getBox(position.row, position.col);
    }

    public static Set<Integer> getReachedIndexes(Position position, Boolean includeClickedPosition) {
        Set<Integer> indexes = new HashSet<>();
        indexes.addAll(getBoxIndexes(position));
        indexes.addAll(getRowIndexes(position));
        indexes.addAll(getColIndexes(position));
        if (!includeClickedPosition) {
            indexes.remove(getIndex(position));
        }
        return indexes;
    }

    public static Set<Integer> getBoxIndexes(Position position) {
        int[] boxIndexes = getBox(position).index;
        Set<Integer> indexes = new HashSet<>();
        for (int i : boxIndexes) {
            indexes.add(i);
        }
        return indexes;
    }


    public static Set<Integer> getRowIndexes(Position position) {
        Set<Integer> rowIndexes = new HashSet<>();
        for (int row = 1; row <= 9; row++) {
            int i = getIndex(row, position.col);
            rowIndexes.add(i);
        }
        return rowIndexes;
    }


    public static Set<Integer> getColIndexes(Position position) {
        Set<Integer> colIndexes = new HashSet<>();
        for (int col = 1; col <= 9; col++) {
            int i = getIndex(position.row, col);
            colIndexes.add(i);
        }
        return colIndexes;
    }

    private void initializeBoard() {
        if (!this.isEmpty()) {
            this.clear();
        }
        for (int i = 0; i < 81; i++) {
            Position position = getPosition(i);
            this.add(new Cell(position));
        }
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    private static Box getTopBox(int col) {
        if (isBetween(col, 1, 3)) {
            return Box.TOP_LEFT;
        } else if (isBetween(col, 4, 6)) {
            return Box.TOP_CENTER;
        } else if (isBetween(col, 7, 9)) {
            return Box.TOP_RIGHT;
        }
        throw new IndexOutOfBoundsException();
    }

    private static Box getCenterBox(int col) {
        if (isBetween(col, 1, 3)) {
            return Box.CENTER_LEFT;
        } else if (isBetween(col, 4, 6)) {
            return Box.CENTER_CENTER;
        } else if (isBetween(col, 7, 9)) {
            return Box.CENTER_RIGHT;
        }
        throw new IndexOutOfBoundsException();
    }

    private static Box getBottomBox(int col) {
        if (isBetween(col, 1, 3)) {
            return Box.BOTTOM_LEFT;
        } else if (isBetween(col, 4, 6)) {
            return Box.BOTTOM_CENTER;
        } else if (isBetween(col, 7, 9)) {
            return Box.BOTTOM_RIGHT;
        }
        throw new IndexOutOfBoundsException();
    }

    public enum Box {

        TOP_LEFT(new int[]{ 0,  1,  2,  9, 10, 11, 18, 19, 20}),
        TOP_CENTER(new int[]{ 3,  4,  5, 12, 13, 14, 21, 22, 23}),
        TOP_RIGHT(new int[]{ 6,  7,  8, 15, 16, 17, 24, 25, 26}),
        CENTER_LEFT(new int[]{27, 28, 29, 36, 37, 38, 45, 46, 47}),
        CENTER_CENTER(new int[]{30, 31, 32, 39, 40, 41, 48, 49, 50}),
        CENTER_RIGHT(new int[]{33, 34, 35, 42, 43, 44, 51, 52, 53}),
        BOTTOM_LEFT(new int[]{54, 55, 56, 63, 64, 65, 72, 73, 74}),
        BOTTOM_CENTER(new int[]{57, 58, 59, 66, 67, 68, 75, 76, 77}),
        BOTTOM_RIGHT(new int[]{60, 61, 62, 69, 70, 71, 78, 79, 80});

        public final int[] index;

        Box(int[] index) {
            this.index = index;
        }
    }

}
