package com.example.hammerox.oxsudoku;


import android.support.annotation.IntegerRes;
import android.util.Log;
import android.util.Pair;
import android.util.Range;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    List<Integer> board;
    List<Boolean> mask;

    public SudokuGenerator() {
        this.board = startBoard(1);
        this.board = fillBoard(board);
        this.mask = setPuzzle(board);
    }

    public List<Integer> startBoard(int iteration) {
        List<Integer> newBoard = createBoard(true);
        Random random = new Random();
        for (int i = 0; i < iteration; i++) {
            Boolean isValid = true;
            int value = (i % 9) + 1;
            int randIndex = random.nextInt(80);
            if (newBoard.get(randIndex) == 0) {
                int randRow = getPositionFromIndex(randIndex)[0];
                int randCol = getPositionFromIndex(randIndex)[1];
                List<Integer> checkList = getRowColBoxIndexes(randRow, randCol);
                for (Integer index : checkList) {
                    int checkValue = newBoard.get(index);
                    if (checkValue == value) {
                        isValid = false;
                        break;
                    }
                }
                if (isValid) {
                    newBoard.set(randIndex, value);
                } else {
                    i--;
                }
            } else {
                i--;
            }
        }

        return newBoard;
    }

    public List<Integer> fillBoard(List<Integer> board) {
        List<Boolean> toFill = getFillList(board);
        Pair<List<Integer>, Integer> pair  = doBacktrack(board, toFill, 1);
        int solution = pair.second;
        if (solution < 0) {
            while (solution < 1) {
                pair = doBacktrack(board, toFill, 1);
                solution = pair.second;
            }
        }
        return pair.first;
    }

    public List<Boolean> setPuzzle(List<Integer> board) {
        Boolean[] array = new Boolean[81];
        Arrays.fill(array, true);
        List<Boolean> toFill = Arrays.asList(array);
        List<Integer> boardToEdit = new ArrayList<>(board);
        List<Integer> allIndexes = new ArrayList<>();
        for (int i = 0; i <= 80; ++i) allIndexes.add(i);
        Collections.shuffle(allIndexes);

        int size = 9 * 9;
        for (int i = 0; i < size; i++) {
            int index = allIndexes.get(i);
            toFill.set(index, false);
            boardToEdit.set(index, 0);

            /*Todo - Continue puzzle algorithm*/
        }

        return mask;
    }

    public static Boolean isValidGame(List<Integer> board) {
        Boolean isValid = true;
        int size = 9 * 9;
        for (int i = 0; i < size; i++) {
            int row = getPositionFromIndex(i)[0];
            int col = getPositionFromIndex(i)[1];
            List<Integer> checkList = getRowColBoxIndexes(row, col);
            List<Integer> availableValue = getAvailableValues(board, checkList);
            Boolean firstCheck = availableValue.size() > 1;
            Boolean secondCheck = availableValue.get(0) != board.get(i);
            if (firstCheck || secondCheck) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    public Pair<List<Integer>, Integer> doBacktrack(List<Integer> board,
                                                    List<Boolean> toFill, int solutionsToBreak) {
        int solutions = 0;
        Boolean isBacktracking = false;
        List<List> listOfLists = new ArrayList<>();
        int size = 9 * 9;

        for (int i = 0; i < size; i++) {
            Boolean isNewCell = listOfLists.isEmpty() || listOfLists.size() <= i;
            if (i == -1) {
                break;
            } else {
                if (toFill.get(i)) {
                    int row = getPositionFromIndex(i)[0];
                    int col = getPositionFromIndex(i)[1];
                    List<Integer> checkList = getRowColBoxIndexes(row, col);
                    List<Integer> availableValues;
                    if (isBacktracking) {
                        availableValues = listOfLists.get(i);
                    } else {
                        availableValues = getAvailableValues(board, checkList);
                    }
                    if (availableValues.isEmpty()) {
                        isBacktracking = true;
                        i = i - 2;

                    } else {
                        if (isNewCell) {
                            listOfLists.add(availableValues);
                        } else {
                            listOfLists.set(i, availableValues);
                        }
                        int indexRange = availableValues.size() - 1;
                        int randomPosition = 0;
                        if (indexRange > 1) {
                            randomPosition = new Random().nextInt(indexRange);
                        }
                        int newValue = availableValues.get(randomPosition);
                        board.set(i, newValue);
                        availableValues.remove(randomPosition);
                        isBacktracking = false;

                        Boolean isComplete = true;
                        for (int j = 0; j < size; j++) {
                            if (board.get(j) == 0) {
                                isComplete = false;
                                break;
                            }
                        }
                        if (isComplete) {
                            solutions = solutions + 1;
                            if (solutions == solutionsToBreak) break;
                            i = i - 1;
                        }
                    }

                } else {
                    if (isBacktracking) {
                        i = i - 2;
                    } else {
                        if (isNewCell) {
                            listOfLists.add(new ArrayList());
                        } else {
                            listOfLists.set(i, new ArrayList());
                        }
                    }
                }
            }
        }
        return new Pair(board, solutions);
    }

    public List<Boolean> getFillList(List<Integer> board) {
        List<Boolean> toFill = new ArrayList<>();
        int size = 9 * 9;
        for (int i = 0; i < size; i++) {
            if (board.get(i) == 0) {
                toFill.add(true);
            } else {
                toFill.add(false);
            }
        }
        return toFill;
    }

    public static List<Integer> getAvailableValues(List<Integer> board, List<Integer> checkList) {
        List<Integer> forbiddenValues = new ArrayList<>();
        for (Integer index : checkList) {
            int checkValue = board.get(index);
            if (checkValue != 0) {
                if (forbiddenValues.isEmpty()) {
                    forbiddenValues.add(checkValue);
                } else {
                    Boolean isNewValue = true;
                    for (Integer n : forbiddenValues) {
                        if (n == checkValue) {
                            isNewValue = false;
                            break;
                        }
                    }
                    if (isNewValue) {
                        forbiddenValues.add(checkValue);
                    }
                }
            }
        }
        Boolean contains1 = false;
        Boolean contains2 = false;
        Boolean contains3 = false;
        Boolean contains4 = false;
        Boolean contains5 = false;
        Boolean contains6 = false;
        Boolean contains7 = false;
        Boolean contains8 = false;
        Boolean contains9 = false;
        List<Integer> availableList = new ArrayList<>();
        for (Integer n : forbiddenValues) {
            switch (n) {
                case 1: contains1 = true; break;
                case 2: contains2 = true; break;
                case 3: contains3 = true; break;
                case 4: contains4 = true; break;
                case 5: contains5 = true; break;
                case 6: contains6 = true; break;
                case 7: contains7 = true; break;
                case 8: contains8 = true; break;
                case 9: contains9 = true; break;
            }
        }
        if (!contains1) availableList.add(1);
        if (!contains2) availableList.add(2);
        if (!contains3) availableList.add(3);
        if (!contains4) availableList.add(4);
        if (!contains5) availableList.add(5);
        if (!contains6) availableList.add(6);
        if (!contains7) availableList.add(7);
        if (!contains8) availableList.add(8);
        if (!contains9) availableList.add(9);

        return availableList;
    }

    public List<Integer> createBoard (Boolean createEmpty) {
        List<Integer> board = new ArrayList<>();
        int size = 9 * 9;
        if (createEmpty) {
            for (int i = 0; i < size; i++) {
                board.add(0);
            }
        } else {
            for (int i = 0; i < size; i++) {
                int row = getPositionFromIndex(i)[0];
                board.add(row);
            }
        }
        return board;
    }

    public List<Integer> rotateHorizontal(List<Integer> list) {
        List<Integer> rotatedBoard = createBoard(true);
        int size = 9 * 9;
        for (int i = 0; i < size; i++) {
            int row = getPositionFromIndex(i)[0];
            int newRow = swapWith(row);
            int index = getIndexFromPosition(newRow, getPositionFromIndex(i)[1]);
            int value = list.get(i);
            rotatedBoard.set(index, value);
        }
        return rotatedBoard;
    }

    public List<Integer> rotateVertical(List<Integer> list) {
        List<Integer> rotatedBoard = createBoard(true);
        int size = 9 * 9;
        for (int i = 0; i < size; i++) {
            int col = getPositionFromIndex(i)[1];
            int newCol = swapWith(col);
            int index = getIndexFromPosition(getPositionFromIndex(i)[0], newCol);
            int value = list.get(i);
            rotatedBoard.set(index, value);
        }
        return rotatedBoard;
    }

    public List<Integer> rotateDiagonal(List<Integer> list) {
        List<Integer> rotatedBoard = createBoard(true);
        int size = 9 * 9;
        for (int i = 0; i < size; i++) {
            int row = getPositionFromIndex(i)[0];
            int col = getPositionFromIndex(i)[1];
            int newRow = col;
            int newCol = row;
            int index = getIndexFromPosition(newRow, newCol);
            int value = list.get(i);
            rotatedBoard.set(index, value);
        }
        return rotatedBoard;
    }

    public int swapWith(int rowOrCol) {
        int inverse = 0;
        switch (rowOrCol) {
            case 1:
                inverse = 9;
                break;
            case 2:
                inverse = 8;
                break;
            case 3:
                inverse = 7;
                break;
            case 4:
                inverse = 6;
                break;
            case 5:
                inverse = 5;
                break;
            case 6:
                inverse = 4;
                break;
            case 7:
                inverse = 3;
                break;
            case 8:
                inverse = 2;
                break;
            case 9:
                inverse = 1;
                break;
        }
        return inverse;
    }

    public static List<Integer> getRowColBoxIndexes(int row, int col) {
        List<Integer> indexes = new ArrayList<>();
        // Box
        int[] boxIndexes = getBoxIndexes(row, col);
        for (int i : boxIndexes) {
            if (i != getIndexFromPosition(row, col)) {
                indexes.add(i);
            }
        }
        // Row
        for (int r = 1; r <= 9; r++) {
            if (r != row) {
                int i = getIndexFromPosition(r, col);
                indexes.add(i);
            }
        }
        // Column
        for (int c = 1; c <= 9; c++) {
            if (c != col) {
                int i = getIndexFromPosition(row, c);
                indexes.add(i);
            }
        }
        return indexes;
    }

    public static int[] getBoxIndexes(int row, int col) {
        int[] boxIndexes = new int[] {};
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
        return boxIndexes;
    }

    public static int[] getPositionFromIndex(int index) {
        int row = index / 9 + 1;
        int col = index % 9 + 1;
        return new int[] {row,col};
    }

    public static int getIndexFromPosition(int row, int col) {
        return 9 * (row - 1) + col - 1;
    }

    public List<Integer> getBoard() {
        return board;
    }

}
