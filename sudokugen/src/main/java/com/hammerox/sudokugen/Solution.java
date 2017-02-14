package com.hammerox.sudokugen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mauricio on 04-Feb-17.
 */

public class Solution extends Board {


    private List<Integer> shuffledIndexes;


    public Solution() {
        getIndexOrder();
        startSolving();
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

        boolean boardIsIncomplete = true;
        while (boardIsIncomplete) {
            boolean isToBacktrack;
            if (availableValues.isEmpty()) {
                clearValue(step);
                isToBacktrack = true;
            } else {
                int value = setRandomValue(currentIndex, availableValues);
                availableValues.remove(value);
                isToBacktrack = false;
            }

            int cellsFilled = countCells();


            BoardLogger.log(this, step, currentIndex, availableValues);

            boolean isToContinue = cellsFilled < BOARD_SIZE;
            if (isToContinue) {
                if (isToBacktrack) {
                    return true;
                } else {
                    boardIsIncomplete = nextStep(step + 1);
                }
            } else {
                return false;
            }
        }
        return false;
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
