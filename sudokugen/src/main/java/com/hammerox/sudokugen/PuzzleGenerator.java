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
    private int step;
    private int attempts;
    private List<Integer> indexPool;


    public Board createPuzzle(int cellsToRemove) {
        checkIfArgumentIsValid(cellsToRemove);
        checkSolution();
        resetVariables();

        while (step < cellsToRemove) {
            int index = removeNextIndex();
            if (boardIsValid()) {
                goToNextStep();
            } else {
                undoChanges(index);
            }
        }
        BoardLogger.log(this);
        return this;
    }

    public void useSolution(Board solution) {
        this.setBoard(solution);
    }

    public List<Integer> getRemovedIndexes() {
        return removedIndexes;
    }

    private void checkIfArgumentIsValid(int cellsToRemove) {
        if (cellsToRemove < 0 || cellsToRemove > 81) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void checkSolution() {
        if (solution == null) {
            solution = new Solver().getSingleSolution();
            this.setBoard(solution);
        }
    }

    private void resetVariables() {
        removedIndexes = new ArrayList<>();
        step = 0;
        attempts = 0;
        indexPool = getAllIndexes();
    }

    private List<Integer> getAllIndexes() {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            indexes.add(i);
        }
        return indexes;
    }

    private int removeNextIndex() {
        int pick = randomNumber(indexPool);
        int pickedIndex = indexPool.get(pick);
        removeIndex(indexPool, pick, pickedIndex);
        return pickedIndex;
    }

    private boolean boardIsValid() {
        Solver solver = new Solver(this);
        return solver.hasValidSolution();
    }

    private void goToNextStep() {
        step++;
        attempts = 0;
    }

    private void undoChanges(int index) {
        undoStep(indexPool, index);
        attempts++;
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
