package com.hammerox.sudokugen;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Mauricio on 04-Feb-17.
 */

public class SolverTest {

    @Test
    public void shouldHaveABoard() {
        Board emptyBoard = new Board();
        Board solution = new Solver(emptyBoard);
        Assert.assertEquals(81, solution.size());
    }

    @Test
    public void shouldBeFilledWithValues() {
        Board emptyBoard = new Board();
        Board solution = new Solver(emptyBoard);
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
        Board emptyBoard = new Board();
        Board solution = new Solver(emptyBoard);
        int irregularCellCount = 0;
        for (Cell cell : solution) {
            int numberOfAvailableValues = solution.getAvailableValues(cell.index).size();
            if (numberOfAvailableValues != 1) {
                irregularCellCount++;
            }
        }
        Assert.assertEquals(0, irregularCellCount);
    }
}
