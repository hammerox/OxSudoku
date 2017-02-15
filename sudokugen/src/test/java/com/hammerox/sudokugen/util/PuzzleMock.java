package com.hammerox.sudokugen.util;

import com.hammerox.sudokugen.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mauricio on 14-Feb-17.
 */

public class PuzzleMock extends Board {


    public final static int MAX = 53;
    public final static int[] INDEX_HISTORY = new int[]
            {0, 20, 10, 25, 23, 67, 26, 5 , 55, 21,
            64, 32, 38, 44, 72, 63, 54, 80, 28, 8 ,
            53, 30, 31, 19, 7 , 42, 57, 75, 48, 3 ,
            49, 70, 34, 18, 37, 9 , 12, 51, 74, 66,
            59, 65, 11, 56, 4 , 50, 45, 47, 78, 62,
            15, 58, 27};

    private List<Integer> shuffledIndexes;


    public PuzzleMock() {
        this.setBoard(new SolutionMock());
    }


    public void randomBuild(int emptyCells) {
        shuffleIndexes();
        removeValues(emptyCells);
    }

    public Board mockedBuild(int cellsToRemove) {
        for (int i = 0; i < cellsToRemove; i++) {
            get(INDEX_HISTORY[i]).clearValue();
        }
        return this;
    }

    private void shuffleIndexes() {
        shuffledIndexes = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            shuffledIndexes.add(i);
        }
        Collections.shuffle(shuffledIndexes);
    }

    private void removeValues(int emptyCells) {
        for (int i = 0; i < emptyCells; i++) {
            int index = shuffledIndexes.get(i);
            get(index).clearValue();
        }
    }

}
