package com.example.hammerox.oxsudoku;

import android.app.Activity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class GridPosition {

    public static List<Integer> getRowColBoxIndexes(int row, int col, Boolean includeClickedPosition) {
        List<Integer> indexes = new ArrayList<>();
        // Box
        List<Integer> boxList = getBoxIndexes(row, col, includeClickedPosition);
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


    public static List<Integer> getBoxIndexes(int row, int col, Boolean includeClickedPosition) {
        int clickedIndex = getIndexFromPosition(row, col);
        int[] boxIndexes = new int[] {};
        List<Integer> indexes = new ArrayList<>();
        switch (row) {
            case 1:
            case 2:
            case 3:
                if (col == 1 || col == 2 || col == 3) {
                    boxIndexes = new int[]{0, 1, 2, 9, 10, 11, 18, 19, 20};

                } else if (col == 4 || col == 5 || col == 6) {
                    boxIndexes = new int[]{3, 4, 5, 12, 13, 14, 21, 22, 23};

                } else if (col == 7 || col == 8 || col == 9) {
                    boxIndexes = new int[]{6, 7, 8, 15, 16, 17, 24, 25, 26};

                }
                break;
            case 4:
            case 5:
            case 6:
                if (col == 1 || col == 2 || col == 3) {
                    boxIndexes = new int[]{27,28,29,36,37,38,45,46,47};

                } else if (col == 4 || col == 5 || col == 6) {
                    boxIndexes = new int[]{30,31,32,39,40,41,48,49,50};

                } else if (col == 7 || col == 8 || col == 9) {
                    boxIndexes = new int[]{33,34,35,42,43,44,51,52,53};

                }
                break;
            case 7:
            case 8:
            case 9:
                if (col == 1 || col == 2 || col == 3) {
                    boxIndexes = new int[]{54,55,56,63,64,65,72,73,74};

                } else if (col == 4 || col == 5 || col == 6) {
                    boxIndexes = new int[]{57,58,59,66,67,68,75,76,77};

                } else if (col == 7 || col == 8 || col == 9) {
                    boxIndexes = new int[]{60,61,62,69,70,71,78,79,80};

                }
                break;
        }

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


    //////////  CONVERTERS //////////

    public static int getIndexFromView(Activity activity, TextView view) {
        // Gets a grid's TextView and returns its index.
        int viewId = view.getId();
        String viewIdName = activity.getResources()
                .getResourceEntryName(viewId);
        String viewRowCol = viewIdName.split("_")[1];
        int rowIndex = Integer.valueOf(viewRowCol.substring(0,1));
        int colIndex = Integer.valueOf(viewRowCol.substring(1,2));
        return 9 * (rowIndex - 1) + colIndex - 1;
    }

    public static int[] getPositionFromIndex(int index) {
        int row = index / 9 + 1;
        int col = index % 9 + 1;
        return new int[] {row,col};
    }

    public static int getIndexFromPosition(int row, int col) {
        return 9 * (row - 1) + col - 1;
    }

    public static int getIdFromIndex(Activity activity, int index) {
        int[] position = getPositionFromIndex(index);
        int row = position[0];
        int col = position[1];
        String idString = "major_" + row + col;
        return activity.getResources()
                .getIdentifier(idString, "id", activity.getPackageName());
    }
}
