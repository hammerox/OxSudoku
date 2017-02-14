package com.hammerox.sudokugen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mauricio on 14-Feb-17.
 */

public class PuzzleGenerator extends Board {


    private Board solution;
    private List<Integer> removedIndexes;
    private Random random = new Random();


    public Board createPuzzle(int cellsToRemove) {
        checkIfParameterIsValid(cellsToRemove);
        solution = new Solver().getSingleSolution();
        this.setBoard(solution);

        removedIndexes = new ArrayList<>();
        int step = 0;
        List<Integer> availableIndexes = getAllIndexes();

        while (step < cellsToRemove) {
            int index = removeRandomIndex(availableIndexes);
            Solver solver = new Solver(this);
            boolean isValid = solver.hasValidSolution();
            if (isValid) {
                step++;
            } else {
                undoStep(availableIndexes, index);
            }
        }

        BoardLogger.log(this);

        return this;
    }

    private void checkIfParameterIsValid(int cellsToRemove) {
        if (cellsToRemove < 0 || cellsToRemove > 81) {
            throw new IndexOutOfBoundsException();
        }
    }

    private List<Integer> getAllIndexes() {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            indexes.add(i);
        }
        return indexes;
    }

    private int removeRandomIndex(List<Integer> indexes) {
        int pick = randomNumber(indexes);
        int pickedIndex = indexes.get(pick);
        removeIndex(indexes, pick, pickedIndex);
        return pickedIndex;
    }

    private void undoStep(List<Integer> indexes, int index) {
        int originalValue = solution.get(index).getValue();
        int last = removedIndexes.size() - 1;
        addIndex(indexes, index, originalValue, last);
    }

    private int randomNumber(List<Integer> indexes) {
        int size = indexes.size();
        return random.nextInt(size);
    }

    private void removeIndex(List<Integer> indexes, int pick, int index) {
        indexes.remove(pick);
        this.get(index).clearValue();
        removedIndexes.add(index);
    }

    private void addIndex(List<Integer> indexes, int index, int originalValue, int last) {
        indexes.add(index);
        this.get(index).setValue(originalValue);
        removedIndexes.remove(last);
    }

}
