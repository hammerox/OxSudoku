package com.hammerox.sudokugen.util;

import com.hammerox.sudokugen.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mauricio on 14-Feb-17.
 */

public class PuzzleMock extends Board {


    public final static int MAX = 57;
    public final static int[] INDEX_HISTORY = new int[]
            {38,40,68,5 ,63,0 ,22,34,7 ,48,
             33,73,30,71,3 ,55,16,69,18,17,
             26,60,51,77,13,8 ,32,35,11,54,
             58,6 ,46,65,47,66,31,20,45,1 ,
             36,10,76,19,21,49,43,59,64,74,
             70,50,52,39,79,61,78};

    private List<Integer> shuffledIndexes;


    public PuzzleMock() {
        this.setBoard(new SolutionMock());
    }


    public Board build(int cellsToRemove) {
        for (int i = 0; i < cellsToRemove; i++) {
            get(INDEX_HISTORY[i]).clearValue();
        }
        return this;
    }

    public void randomBuild(int emptyCells) {
        shuffleIndexes();
        removeValues(emptyCells);
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
