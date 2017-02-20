package com.hammerox.sudokugen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mauricio on 04-Feb-17.
 */

public class Solver extends Board {

    /* Set "isToLog" to true ONLY FOR DEBUGGING
    *  It logs the whole board for evaluation.
    *  However it doubles the computation time. */
    private final static boolean isToLog = false;

    private final Board originalPuzzle;
    private List<Integer> indexes;
    private boolean stopOnFirstSolution;
    private int solutions;


    public Solver(Board puzzle) {
        this.originalPuzzle = puzzle;
        setBoard(originalPuzzle);
        setIndexOrder();
    }

    public Solver() {
        this(new Board());
    }


    public Board getSingleSolution() {
        stopOnFirstSolution = true;
        nextStep(0);
        return this;
    }

    public boolean hasValidSolution() {
        stopOnFirstSolution = false;
        solutions = 0;
        nextStep(0);
        return solutions == 1;
    }

    private void setIndexOrder() {
        Set<Integer> indexes = new LinkedHashSet<>(getAllIndexes());
        indexes = removeIndexesOnPuzzle(indexes);
        this.indexes = new ArrayList<>(indexes);
    }

    private Set<Integer> removeIndexesOnPuzzle(Set<Integer> indexes) {
        for (Cell cell : originalPuzzle) {
            if (cell.hasValue()) {
                indexes.remove(cell.index);
            }
        }
        return indexes;
    }

    private boolean nextStep(int step) {
        int index = indexes.get(step);
        Set<Integer> availableValues = getAvailableValues(index);
        boolean isBoardComplete = false;
        while (!isBoardComplete) {
            boolean isToBacktrack = addOrRemoveValue(step, index, availableValues);
            isBoardComplete = countFilledCells() == BOARD_SIZE;
            if (isToBacktrack) {
                return false;
            } else if (isBoardComplete) {
                return isDoneSolving();
            } else {
                isBoardComplete = nextStep(step + 1);
            }
        }
        return isBoardComplete;
    }

    private boolean addOrRemoveValue(int step, int currentIndex, Set<Integer> availableValues) {
        boolean isToBacktrack = (availableValues.isEmpty())
                ? removeValue(step)
                : addValue(currentIndex, availableValues);
        logBoard(step, currentIndex, availableValues);
        return isToBacktrack;
    }

    private boolean isDoneSolving() {
        return stopOnFirstSolution || ++solutions > 1;
    }

    private boolean addValue(int currentIndex, Set<Integer> availableValues) {
        int value = setRandomValue(currentIndex, availableValues);
        availableValues.remove(value);
        return false;
    }

    private boolean removeValue(int step) {
        clearValue(step);
        return true;
    }

    private void logBoard(int step, int currentIndex, Set<Integer> availableValues) {
        if (isToLog) {
            BoardLogger.log(this, step, currentIndex, availableValues);
        }
    }

    private int setRandomValue(int index, Set<Integer> availableValues) {
        List<Integer> list = new ArrayList<>(availableValues);
        Collections.shuffle(list);
        int value = list.get(0);
        set(index, value);
        return value;
    }

    private void clearValue(int step) {
        int previousIndex = indexes.get(step);
        get(previousIndex).clearValue();
    }
}
