package com.hammerox.sudokugen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mauricio on 14-Feb-17.
 */

public class PuzzleGenerator extends Board {


    private Board solution;
    private List<Integer> removedIndexes;
    private int step;
    private int attempts;
    private List<Integer> indexPool;


    public Board createPuzzle(int cellsToRemove) {
        checkIfArgumentIsValid(cellsToRemove);
        checkSolution();
        resetVariables();

        while (step < cellsToRemove) {
            if (attempts >= indexPool.size()) {
                break;
            } else {
                shuffleIndexesOnFirstAttempt();
                int index = removeNextIndex();
                if (boardIsValid()) {
                    goToNextStep();
                } else {
                    undoChanges(index);
                }
            }
        }
        BoardLogger.log(solution);
        BoardLogger.log(this);
        return this;
    }

    private void shuffleIndexesOnFirstAttempt() {
        if (attempts == 0) {
            Collections.shuffle(indexPool);
        }
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

    private int removeNextIndex() {
        int index = indexPool.get(attempts);
        removeIndex(index);
        return index;
    }

    private void removeIndex(int index) {
        indexPool.remove(attempts);
        this.get(index).clearValue();
        removedIndexes.add(index);
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
        undoStep(index);
        attempts++;
    }

    private void undoStep(int index) {
        int originalValue = solution.get(index).getValue();
        int lastPosition = removedIndexes.size() - 1;
        addIndex(index, originalValue, lastPosition);
    }

    private void addIndex(int index, int originalValue, int last) {
        indexPool.add(index);
        this.get(index).setValue(originalValue);
        removedIndexes.remove(last);
    }

}
