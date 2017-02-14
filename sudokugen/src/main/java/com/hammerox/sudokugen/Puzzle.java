package com.hammerox.sudokugen;

/**
 * Created by Mauricio on 14-Feb-17.
 */

public class Puzzle extends Board {


    private Board solution;


    public Board create(int cellsToRemove) {
        solution = new Solver().getSingleSolution();

        return null;    // For now
    }
}
