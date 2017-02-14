package com.hammerox.sudokugen;

/**
 * Created by Mauricio on 14-Feb-17.
 */

public class Puzzle extends Board {


    private Board solution;


    public Board create(int cellsToRemove) {
        checkIfParameterIsValid(cellsToRemove);
        solution = new Solver().getSingleSolution();

        return null;    // For now
    }

    private void checkIfParameterIsValid(int cellsToRemove) {
        if (cellsToRemove < 0 || cellsToRemove > 81) {
            throw new IndexOutOfBoundsException();
        }
    }
}
