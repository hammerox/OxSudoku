package com.hammerox.sudokugen;

import com.hammerox.sudokugen.util.SolutionMock;
import com.hammerox.sudokugen.util.Testable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Mauricio on 14-Feb-17.
 */

public class PuzzleGeneratorTests {

    private PuzzleGenerator puzzleGenerator;
    private Board solution;

    @Before
    public void setup() {
        puzzleGenerator = new PuzzleGenerator();
        solution = new SolutionMock();
    }

    @Test
    public void shouldBuildValidPuzzle() {
        puzzleGenerator.createPuzzle(10);
        Assert.assertEquals(10, puzzleGenerator.countEmptyCells());
    }

    @Test
    public void shouldNotAllowNegativeValues() throws Exception {
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                puzzleGenerator.createPuzzle(-1);
            }
        });
    }

    @Test
    public void shouldNotAllowValuesGreaterThan81() throws Exception {
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                puzzleGenerator.createPuzzle(82);
            }
        });
    }

    @Test
    public void shouldUseGivenSolution() {
        puzzleGenerator.useSolution(solution);
        Assert.assertEquals(solution.get(0).getValue(), puzzleGenerator.get(0).getValue());
        Assert.assertEquals(solution.get(10).getValue(), puzzleGenerator.get(10).getValue());
        Assert.assertEquals(solution.get(80).getValue(), puzzleGenerator.get(80).getValue());
    }

    @Test
    public void puzzleGeneratedShouldLookLikeSolution() {
        puzzleGenerator.useSolution(solution);
        puzzleGenerator.createPuzzle(40);
        assertRemainingIndexes();
    }

    @Test
    public void shouldBuildDifficultPuzzles40() {
        int removeCount = 40;
        puzzleGenerator.createPuzzle(removeCount);
        Assert.assertEquals(removeCount, puzzleGenerator.countEmptyCells());
    }

    @Test
    public void shouldBuildDifficultPuzzles45() {
        int removeCount = 45;
        puzzleGenerator.createPuzzle(removeCount);
        Assert.assertEquals(removeCount, puzzleGenerator.countEmptyCells());
    }

    @Test
    public void shouldBuildDifficultPuzzles50() {
        int removeCount = 50;
        puzzleGenerator.createPuzzle(removeCount);
        Assert.assertEquals(removeCount, puzzleGenerator.countEmptyCells());
    }

    @Test
    public void shouldBreakIfCannotRemoveMoreValues() {
        puzzleGenerator.useSolution(solution);
        puzzleGenerator.createPuzzle(70);
        Assert.assertTrue(puzzleGenerator.countEmptyCells() < 70);
    }

    @Test
    public void shouldSaveHitoryOfRemovedIndexes() {
        puzzleGenerator.useSolution(solution);
        puzzleGenerator.createPuzzle(45);
        List<Integer> indexHistory = puzzleGenerator.getRemovedIndexes();
        Assert.assertEquals(45, indexHistory.size());
    }


    private void assertRemainingIndexes() {
        Set<Integer> remainingIndexes = new LinkedHashSet<>(Board.getAllIndexes());
        List<Integer> indexHistory = puzzleGenerator.getRemovedIndexes();
        remainingIndexes.removeAll(indexHistory);
        for (Integer i :
                remainingIndexes) {
            Assert.assertEquals(solution.get(i).getValue(), puzzleGenerator.get(i).getValue());
        }
    }

}
