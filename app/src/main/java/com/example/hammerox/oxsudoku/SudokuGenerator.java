package com.example.hammerox.oxsudoku;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    List<Integer> board;

    public SudokuGenerator() {
        this.board = startBoard(11);
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
                    if (index != randIndex) {
                        int checkValue = newBoard.get(index);
                        if (checkValue == value) {
                            isValid = false;
                            break;
                        }
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

    public List<Integer> getRowColBoxIndexes(int row, int col) {
        List<Integer> indexes = new ArrayList<>();
        // Box
        int[] boxIndexes = getBoxIndexes(row, col);
        for (int i : boxIndexes) {
            indexes.add(i);
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

    public int[] getBoxIndexes(int row, int col) {
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

    public int[] getPositionFromIndex(int index) {
        int row = index / 9 + 1;
        int col = index % 9 + 1;
        return new int[] {row,col};
    }

    public int getIndexFromPosition(int row, int col) {
        return 9 * (row - 1) + col - 1;
    }

    public List<Integer> getBoard() {
        return board;
    }

}
