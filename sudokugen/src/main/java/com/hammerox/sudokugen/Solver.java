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

    /* Set isToLog to true ONLY FOR DEBUGGING
    *  It logs the whole board for evaluation.
    *  However it doubles the computation time. */
    private final static boolean isToLog = false;

    private List<Integer> shuffledIndexes;


    public Solver(Board board) {
        copyBoardCells(board);
        getIndexOrder();
        startSolving();
    }


    private void copyBoardCells(Board board) {
        removeAll(this);
        addAll(board);
    }

    private void getIndexOrder() {
        Set<Integer> indexes = new LinkedHashSet<>();
        for (Box box : Box.values()) {
            for (int i : box.index) {
                indexes.add(i);
                indexes.addAll(get(i).getIndexesInRange());
            }
            if (indexes.size() == BOARD_SIZE) {
                break;
            }
        }
        shuffledIndexes = new ArrayList<>(indexes);
    }

    private void startSolving() {
        nextStep(0);
    }

    private boolean nextStep(int step) {
        int currentIndex = shuffledIndexes.get(step);
        Set<Integer> availableValues = getAvailableValues(currentIndex);
        boolean isBoardComplete = false;
        while (!isBoardComplete) {
            boolean isToBacktrack = addOrRemoveValue(step, currentIndex, availableValues);
            boolean isToContinue = countFilledCells() < BOARD_SIZE;
            if (isToContinue) {
                if (isToBacktrack) {
                    return false;
                } else {
                    isBoardComplete = nextStep(step + 1);
                }
            } else {
                return true;
            }
        }
        return true;
    }

    private void logBoard(int step, int currentIndex, Set<Integer> availableValues) {
        if (isToLog) {
            BoardLogger.log(this, step, currentIndex, availableValues);
        }
    }

    private boolean addOrRemoveValue(int step, int currentIndex, Set<Integer> availableValues) {
        boolean isToBacktrack;
        if (availableValues.isEmpty()) {
            isToBacktrack = removeValue(step);
        } else {
            isToBacktrack = addValue(currentIndex, availableValues);
        }
        logBoard(step, currentIndex, availableValues);
        return isToBacktrack;
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

    private void clearValue(int step) {
        int previousIndex = shuffledIndexes.get(step);
        get(previousIndex).clearValue();
    }

    private int setRandomValue(int index, Set<Integer> availableValues) {
        List<Integer> list = new ArrayList<>(availableValues);
        Collections.shuffle(list);
        int value = list.get(0);
        set(index, value);
        return value;
    }
}
