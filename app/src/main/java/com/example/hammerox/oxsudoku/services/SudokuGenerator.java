package com.example.hammerox.oxsudoku.services;


import android.util.Log;
import android.util.Pair;

import com.example.hammerox.oxsudoku.utils.GridPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/*
*
* Algorithm taken from:
* http://stackoverflow.com/questions/6924216/how-to-generate-sudoku-boards-with-unique-solutions
* User: Doc Brown
*
* */

public class SudokuGenerator {

    public final static int GRID_SIZE = 81;

    List<Integer> board;
    List<Boolean> mask = new ArrayList<>();
    List<Boolean> emptyCellList;
    List<Integer> boardToEdit;
    List<Integer> shuffledIndexes;
    int maxEmptyCells;
    int emptyCellsCounter = 0;

    public SudokuGenerator() {
        this.board = createBoard(1);
        this.board = fillBoard(board);
        /*setPuzzle(50);*/
    }

    public SudokuGenerator(int maxEmptyCells) {
        this.board = createBoard(1);
        this.board = fillBoard(board);
        this.maxEmptyCells = maxEmptyCells;
        /*setPuzzle(maxEmptyCells);*/
    }

    public List<Integer> createBoard(int iteration) {
        List<Integer> newBoard = createBoard(true);
        Random random = new Random();
        for (int i = 0; i < iteration; i++) {
            Boolean isValid = true;
            int value = (i % 9) + 1;
            int randIndex = random.nextInt(80);
            if (newBoard.get(randIndex) == 0) {
                int randRow = GridPosition.getPositionFromIndex(randIndex)[0];
                int randCol = GridPosition.getPositionFromIndex(randIndex)[1];
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
        List<Boolean> toFill = createFillList(board);
        List<Integer> boardCopy = new ArrayList<>(board);
        Pair<List<Integer>, Integer> pair  = doBacktrack(boardCopy, toFill, 1);
        int solution = pair.second;
        if (solution < 1) {
            while (solution < 1) {
                boardCopy = new ArrayList<>(board);
                pair = doBacktrack(boardCopy, toFill, 1);
                solution = pair.second;
            }
        }
        return pair.first;
    }

    public void preparePuzzle() {
        // Creating a boolean list. If TRUE, cell is EMPTY. If false, cell contains solution.
        // Setting all initial values to FALSE.
        Boolean[] array = new Boolean[81];
        Arrays.fill(array, false);
        emptyCellList = Arrays.asList(array);

        // Shuffling indexes.
        boardToEdit = new ArrayList<>(board);
        shuffledIndexes = new ArrayList<>();
        for (int i = 0; i <= 80; ++i) shuffledIndexes.add(i);
        Collections.shuffle(shuffledIndexes);
    }

    public void setPuzzle() {
        for (int i = 0; i < GRID_SIZE; i++) {                // For every cell on the board...
            tryToRemoveCell(i);                         // execute tryToRemoveCell().
            if (emptyCellsCounter == maxEmptyCells) {
                break;                                  // Stop if the number of empty cells is enough...
            }
        }
        fillToMask(emptyCellList);                      // and convert it to a mask.
    }

    public void tryToRemoveCell(int iteration) {
        int index = shuffledIndexes.get(iteration);  // Pick a random cell, ...
        emptyCellList.set(index, true);             // mark it as empty ...
        boardToEdit.set(index, 0);                  // and make it empty.

        List<Integer> boardCopy = new ArrayList<>(boardToEdit);
        Pair<List<Integer>, Integer> pair           // Try to solve it...
                = doBacktrack(boardCopy, emptyCellList, 2);
        int solutions = pair.second;                // and count the number of possible solutions.
        Log.d("onCreate", "i: " + iteration + ", emptyCells: " + emptyCellsCounter + " ,solutions: " + solutions );
        if (solutions == 1) {                       // If there is a single solution...
            emptyCellsCounter++;                    // keep changes and count.
        } else {                                    // If there is more than one solution...
            emptyCellList.set(index, false);        // undo the changes.
            boardToEdit.set(index, board.get(index));
        }
    }

    public void fillToMask(List<Boolean> fillList) {
        for (int i = 0; i < GRID_SIZE; i++) {
            Boolean toMask = !fillList.get(i);
            mask.add(toMask);
        }
    }

    public Pair<List<Integer>, Integer> doBacktrack(List<Integer> board,
                                                    List<Boolean> toFill, int solutionsToBreak) {
        int solutions = 0;
        Boolean isBacktracking = false;
        Boolean hasSolution = false;
        List<List> listOfLists = new ArrayList<>();
        int step = 0;

        for (int i = 0; i < GRID_SIZE; i++) {
            step++;
            Boolean isNewCell = listOfLists.isEmpty() || listOfLists.size() <= i;
            if (i == -1) {
                /*Log.d("onCreate", "Break by index");*/
                break;
            } else {
                if (toFill.get(i)) {
                    int row = GridPosition.getPositionFromIndex(i)[0];
                    int col = GridPosition.getPositionFromIndex(i)[1];
                    List<Integer> checkList = getRowColBoxIndexes(row, col);
                    List<Integer> availableNumbers;
                    if (isBacktracking) {
                        availableNumbers = listOfLists.get(i);
                    } else {
                        availableNumbers = getAvailableNumbers(board, checkList);
                    }
                    if (availableNumbers.isEmpty()) {
                        board.set(i, 0);
                        isBacktracking = true;
                        i = i - 2;

                    } else {
                        if (isNewCell && !hasSolution) {
                            listOfLists.add(availableNumbers);
                        } else {
                            listOfLists.set(i, availableNumbers);
                        }
                        int indexRange = availableNumbers.size() - 1;
                        int randomPosition = 0;
                        if (indexRange > 1) {
                            randomPosition = new Random().nextInt(indexRange);
                        }
                        int newValue = availableNumbers.get(randomPosition);
                        board.set(i, newValue);
                        availableNumbers.remove(randomPosition);
                        isBacktracking = false;

                        Boolean isComplete = true;
                        for (int j = 0; j < GRID_SIZE; j++) {
                            if (board.get(j) == 0) {
                                isComplete = false;
                                break;
                            }
                        }
                        if (isComplete) {
                            /*Log.d("onCreate", "Board is complete");*/
                            solutions = solutions + 1;
                            //Log.d("onCreate", "solutions: " + solutions);
                            if (solutions == solutionsToBreak) {
                                /*Log.d("onCreate", "Break by solutions");*/
                                break;
                            }
                            i = listOfLists.size() - 2;
                            isBacktracking = true;
                            hasSolution = true;
                        }
                    }

                } else {
                    if (isBacktracking) {
                        i = i - 2;
                    } else {
                        if (isNewCell && !hasSolution) {
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

    public static Boolean isValidGame(List<Integer> board) {
        Boolean isValid = true;
        for (int i = 0; i < GRID_SIZE; i++) {                        // For every cell on the board,...
            int row = GridPosition.getPositionFromIndex(i)[0];
            int col = GridPosition.getPositionFromIndex(i)[1];
            List<Integer> checkList = getRowColBoxIndexes(row, col);
            List<Integer> availableValue                        // get all possible values to insert.
                    = getAvailableNumbers(board, checkList);
            Boolean firstCheck = availableValue.size() > 1;     // If there is more than one solution...
            Boolean secondCheck                                 // or if possible value is different...
                    = availableValue.get(0) != board.get(i);    // from the solution...
            if (firstCheck || secondCheck) {
                isValid = false;                                // the puzzle is not valid.
                break;
            }
        }
        return isValid;
    }

    public List<Boolean> createFillList(List<Integer> board) {
        List<Boolean> toFill = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            if (board.get(i) == 0) {
                toFill.add(true);
            } else {
                toFill.add(false);
            }
        }
        return toFill;
    }

    public static List<Integer> getAvailableNumbers(List<Integer> board, List<Integer> checkList) {
        // Getting an array with all forbidden values for the respective cell.
        List<Integer> forbiddenValues = new ArrayList<>();

                                                    // For each cell on row, column and ...
        for (Integer index : checkList) {           // box of the respective cell...
            int checkValue = board.get(index);      // get its number...
            if (checkValue != 0) {                  // and ignore if 0.
                if (forbiddenValues.isEmpty()) {        // If list is new...
                    forbiddenValues.add(checkValue);    // add number to list.
                } else {                                // If list already contains numbers, ...
                    Boolean isNewValue = true;
                    for (Integer n : forbiddenValues) { // check for each value inside it...
                        if (n == checkValue) {          // if evaluating number is already on list.
                            isNewValue = false;         // If so, number isn't new...
                            break;                      // so ignore it.
                        }
                    }
                    if (isNewValue) {                   // If not, add it to the list.
                        forbiddenValues.add(checkValue);
                    }
                }
            }
        }
        Boolean[] array = new Boolean[9];               // Create a boolean value for each number...
        Arrays.fill(array, true);                       // from 1 to 9. Set all values as true...
        List<Boolean> isPossible = Arrays.asList(array);// and put them into an array.

        for (Integer cannotExist : forbiddenValues) {   // For every number inside the forbidden list...
            isPossible.set(cannotExist - 1, false);     // set it's respective boolean to false.
        }

        List<Integer> availableList = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            if (isPossible.get(i - 1)) {                    // Get all true values from our list...
                availableList.add(i);               // and add its respective number to a new...
            }                                           // list with all possible numbers.
        }

        return availableList;
    }

    public List<Integer> createBoard (Boolean createEmpty) {
        List<Integer> board = new ArrayList<>();
        if (createEmpty) {
            for (int i = 0; i < GRID_SIZE; i++) {
                board.add(0);
            }
        } else {
            for (int i = 0; i < GRID_SIZE; i++) {
                int row = GridPosition.getPositionFromIndex(i)[0];
                board.add(row);
            }
        }
        return board;
    }

    public List<Integer> rotateHorizontal(List<Integer> list) {
        List<Integer> rotatedBoard = createBoard(true);
        for (int i = 0; i < GRID_SIZE; i++) {
            int row = GridPosition.getPositionFromIndex(i)[0];
            int newRow = swapWith(row);
            int index = GridPosition.getIndexFromPosition(
                    newRow, GridPosition.getPositionFromIndex(i)[1]);
            int value = list.get(i);
            rotatedBoard.set(index, value);
        }
        return rotatedBoard;
    }

    public List<Integer> rotateVertical(List<Integer> list) {
        List<Integer> rotatedBoard = createBoard(true);
        for (int i = 0; i < GRID_SIZE; i++) {
            int col = GridPosition.getPositionFromIndex(i)[1];
            int newCol = swapWith(col);
            int index = GridPosition.getIndexFromPosition(
                    GridPosition.getPositionFromIndex(i)[0], newCol);
            int value = list.get(i);
            rotatedBoard.set(index, value);
        }
        return rotatedBoard;
    }

    public List<Integer> rotateDiagonal(List<Integer> list) {
        List<Integer> rotatedBoard = createBoard(true);
        for (int i = 0; i < GRID_SIZE; i++) {
            int row = GridPosition.getPositionFromIndex(i)[0];
            int col = GridPosition.getPositionFromIndex(i)[1];
            int newRow = col;
            int newCol = row;
            int index = GridPosition.getIndexFromPosition(newRow, newCol);
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
            if (i != GridPosition.getIndexFromPosition(row, col)) {
                indexes.add(i);
            }
        }
        // Row
        for (int r = 1; r <= 9; r++) {
            if (r != row) {
                int i = GridPosition.getIndexFromPosition(r, col);
                indexes.add(i);
            }
        }
        // Column
        for (int c = 1; c <= 9; c++) {
            if (c != col) {
                int i = GridPosition.getIndexFromPosition(row, c);
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

    public List<Integer> getBoard() {
        return board;
    }

    public void setBoard(List<Integer> board) {
        this.board = board;
    }

    public List<Integer> getBoardToEdit() {
        return boardToEdit;
    }

    public void setBoardToEdit(List<Integer> boardToEdit) {
        this.boardToEdit = boardToEdit;
    }

    public List<Boolean> getEmptyCellList() {
        return emptyCellList;
    }

    public void setEmptyCellList(List<Boolean> emptyCellList) {
        this.emptyCellList = emptyCellList;
    }

    public int getEmptyCellsCounter() {
        return emptyCellsCounter;
    }

    public void setEmptyCellsCounter(int emptyCellsCounter) {
        this.emptyCellsCounter = emptyCellsCounter;
    }

    public static int getGridSize() {
        return GRID_SIZE;
    }

    public List<Boolean> getMask() {
        return mask;
    }

    public void setMask(List<Boolean> mask) {
        this.mask = mask;
    }

    public int getMaxEmptyCells() {
        return maxEmptyCells;
    }

    public void setMaxEmptyCells(int maxEmptyCells) {
        this.maxEmptyCells = maxEmptyCells;
    }

    public List<Integer> getShuffledIndexes() {
        return shuffledIndexes;
    }

    public void setShuffledIndexes(List<Integer> shuffledIndexes) {
        this.shuffledIndexes = shuffledIndexes;
    }
}
