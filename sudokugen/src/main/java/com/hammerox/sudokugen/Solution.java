package com.hammerox.sudokugen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Mauricio on 04-Feb-17.
 */

public class Solution extends Board {


    private int step;
    private List<Integer> shuffledIndexes;
    private List<Set<Integer>> backtrackList;
    private boolean isBacktracking;
    private int cellsFilled;

    public Solution() {
        step = 0;
        backtrackList = new ArrayList<>();
        shuffleIndexes();
        while (step < 81) {
            Set<Integer> availableValues;
            int currentIndex = shuffledIndexes.get(step);

            if (isBacktracking) {
                availableValues = backtrackList.get(step);
            } else {
                availableValues = getAvailableValues(currentIndex);
                backtrackList.add(step, availableValues);
            }

            if (availableValues.isEmpty()) {
                get(currentIndex).clearValue();
                backtrackList.remove(step);
                step--;
                isBacktracking = true;
            } else {
                setRandomValueFromList(currentIndex, availableValues);
                step++;
                isBacktracking = false;
            }
            cellsFilled = countCells();
            BoardLogger.log(this, currentIndex);
        }
    }


    private void shuffleIndexes() {
        shuffledIndexes = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            shuffledIndexes.add(i);
        }
        Collections.shuffle(shuffledIndexes);
    }

    private int setRandomValueFromList(int index, Set<Integer> availableValues) {
        List<Integer> list = new ArrayList<>(availableValues);
        Collections.shuffle(list);
        int value = list.get(0);
        availableValues.remove(value);
        set(index, value);
        return value;
    }

    private int countCells() {
        int count = 0;
        for (Cell cell : this) {
            if (cell.hasValue()) {
                count++;
            }
        }
        return count;
    }
}
