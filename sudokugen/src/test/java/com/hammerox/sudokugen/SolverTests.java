package com.hammerox.sudokugen;

import com.hammerox.sudokugen.util.PuzzleMock;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Mauricio on 04-Feb-17.
 */

public class SolverTests {

    @Test
    public void shouldHaveABoard() {
        Board solution = new Solver().getSingleSolution();
        Assert.assertEquals(81, solution.size());
    }

    @Test
    public void shouldBeFilledWithValues() {
        Board solution = new Solver().getSingleSolution();
        int emptyCellCount = 0;
        for (Cell cell : solution) {
            if (!cell.hasValue()) {
                emptyCellCount++;
            }
        }
        Assert.assertEquals(0, emptyCellCount);
    }

    @Test
    public void valuesShouldNotBreakSudokuRules() {
        Board solution = new Solver().getSingleSolution();
        int irregularCellCount = 0;
        for (Cell cell : solution) {
            int numberOfAvailableValues = solution.getAvailableValues(cell.index).size();
            if (numberOfAvailableValues != 1) {
                irregularCellCount++;
            }
        }
        Assert.assertEquals(0, irregularCellCount);
    }

    @Test
    public void shouldFindOnlyOneSolution() {
        Board puzzle = new PuzzleMock(1);
    }

    @Test
    public void shouldFindMoreThanOneSolution() {

    }
}
