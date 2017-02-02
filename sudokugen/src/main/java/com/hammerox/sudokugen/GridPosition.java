package com.hammerox.sudokugen;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class GridPosition {


    public static int[] getPositionFromIndex(int index) {
        int row = index / 9 + 1;
        int col = index % 9 + 1;
        return new int[] {row, col};
    }


    public static int getIndexFromPosition(int row, int col) {
        return 9 * (row - 1) + col - 1;
    }


    // Not yet tested


    public static List<Integer> getRowColBoxIndexes(int row, int col, Boolean includeClickedPosition) {
        List<Integer> indexes = new ArrayList<>();
        // Box
        List<Integer> boxList = getBoxList(row, col, includeClickedPosition);
        indexes.addAll(boxList);
        // Row
        List<Integer> rowList = getRowIndexes(row, col, includeClickedPosition);
        indexes.addAll(rowList);
        // Column
        List<Integer> colList = getColIndexes(row, col, includeClickedPosition);
        indexes.addAll(colList);
        // Removing duplicates
        HashSet hashSet = new HashSet();
        hashSet.addAll(indexes);
        indexes.clear();
        indexes.addAll(hashSet);

        return indexes;
    }


    public static List<Integer> getRowColBoxIndexes(int index, Boolean includeClickedPosition) {
        int[] position = getPositionFromIndex(index);
        int row = position[0];
        int col = position[1];
        return getRowColBoxIndexes(row, col, includeClickedPosition);
    }


    public static List<Integer> getBoxList(int row, int col, Boolean includeClickedPosition) {
        int clickedIndex = getIndexFromPosition(row, col);
        byte[] boxIndexes = Box.getBoxIndexes(row, col);
        List<Integer> indexes = new ArrayList<>();

        for (int i : boxIndexes) {
            Boolean isToAdd = (clickedIndex != i) || includeClickedPosition;
            if (isToAdd) indexes.add(i);
        }
        return indexes;
    }


    public static List<Integer> getRowIndexes(int row, int col, Boolean includeClickedPosition) {
        List<Integer> rowIndexes = new ArrayList<>();
        for (int r = 1; r <= 9; r++) {
            Boolean isToAdd = (r != row) || includeClickedPosition;
            if (isToAdd) {
                int i = getIndexFromPosition(r, col);
                rowIndexes.add(i);
            }
        }
        return rowIndexes;
    }


    public static List<Integer> getColIndexes(int row, int col, Boolean includeClickedPosition) {
        List<Integer> colIndexes = new ArrayList<>();
        for (int c = 1; c <= 9; c++) {
            Boolean isToAdd = (c != col) || includeClickedPosition;
            if (isToAdd) {
                int i = getIndexFromPosition(row, c);
                colIndexes.add(i);
            }
        }
        return colIndexes;
    }


    public static int getCellId(int row, int col) {
        String idString = "" + row + col;
        return Integer.valueOf(idString);
    }


    public static int getCellId(int index) {
        int[] position = getPositionFromIndex(index);
        int row = position[0];
        int col = position[1];
        String idString = "" + row + col;
        return Integer.valueOf(idString);
    }


    public static int getPencilId(int row, int col, int number) {
        String idString = "1" + number + row + col;
        return Integer.valueOf(idString);
    }


    public static int getPencilId(int index, int number) {
        int[] position = getPositionFromIndex(index);
        int row = position[0];
        int col = position[1];
        return getPencilId(row, col, number);
    }


    public static int getIdFromIndex(int index) {
        int[] position = getPositionFromIndex(index);
        int row = position[0];
        int col = position[1];
        return getCellId(row, col);
    }
}
