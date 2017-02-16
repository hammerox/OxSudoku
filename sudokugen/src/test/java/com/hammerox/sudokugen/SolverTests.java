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
        Board puzzle = new PuzzleMock().build(1);
        Solver solver = new Solver(puzzle);
        boolean isValidPuzzle = solver.hasValidSolution();
        Assert.assertTrue(isValidPuzzle);
    }

    @Test
    public void shouldConfirmPuzzleIsInvalid() {
        Board emptyBoard = new Board();
        Solver solver = new Solver(emptyBoard);
        boolean isValidPuzzle = solver.hasValidSolution();
        Assert.assertFalse(isValidPuzzle);
    }

    @Test
    public void shouldTestSolverSpeed() {
        Board puzzle = new PuzzleMock().build(PuzzleMock.MAX);
        Solver solver = new Solver(puzzle);
    }
}
