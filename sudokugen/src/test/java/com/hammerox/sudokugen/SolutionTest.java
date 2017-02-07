package com.hammerox.sudokugen;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Mauricio on 04-Feb-17.
 */

public class SolutionTest {

    @Test
    public void shouldHaveABoard() {
        Board solution = new Solution();
        Assert.assertEquals(81, solution.size());
    }

    @Test
    public void shouldBeFilledWithValues() {
        Board board = new Solution();
        int emptyCellCount = 0;
        for (Cell cell : board) {
            if (!cell.hasValue()) {
                emptyCellCount++;
            }
        }
        Assert.assertEquals(0, emptyCellCount);
    }

    @Test
    public void valuesShouldNotBreakSudokuRules() {
        Board board = new Solution();
        int emptyCellCount = 0;
        for (Cell cell : board) {
            if (!cell.hasValue()) {
                emptyCellCount++;
            }
        }
        Assert.assertEquals(0, emptyCellCount);
    }
}
