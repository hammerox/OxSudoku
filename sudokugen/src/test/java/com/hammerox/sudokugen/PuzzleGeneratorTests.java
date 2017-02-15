package com.hammerox.sudokugen;

import com.hammerox.sudokugen.util.PuzzleMock;
import com.hammerox.sudokugen.util.Testable;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by Mauricio on 14-Feb-17.
 */

public class PuzzleGeneratorTests {

    @Test
    public void shouldBuildValidPuzzle() {
        PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
        puzzleGenerator.createPuzzle(10);
        Assert.assertEquals(10, puzzleGenerator.countEmptyCells());
    }

    @Test
    public void shouldNotAllowNegativeValues() throws Exception {
        final PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                puzzleGenerator.createPuzzle(-1);
            }
        });
    }

    @Test
    public void shouldNotAllowValuesGreaterThan81() throws Exception {
        final PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                puzzleGenerator.createPuzzle(82);
            }
        });
    }

    @Test
    public void shouldBuildDifficultPuzzles40() {
        int removeCount = 40;
        PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
        puzzleGenerator.createPuzzle(removeCount);
        Assert.assertEquals(removeCount, puzzleGenerator.countEmptyCells());
    }

    @Test
    public void shouldBuildDifficultPuzzles45() {
        int removeCount = 45;
        PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
        puzzleGenerator.createPuzzle(removeCount);
        Assert.assertEquals(removeCount, puzzleGenerator.countEmptyCells());
    }

    @Test
    public void shouldBuildDifficultPuzzles50() {
        int removeCount = 50;
        PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
        puzzleGenerator.createPuzzle(removeCount);
        Assert.assertEquals(removeCount, puzzleGenerator.countEmptyCells());
    }

    @Test
    public void shouldUseGivenSolution() {
        PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
        Board solution = new PuzzleMock(0);
        puzzleGenerator.useSolution(solution);
        Assert.assertEquals(solution.get(0).getValue(), puzzleGenerator.get(0).getValue());
        Assert.assertEquals(solution.get(10).getValue(), puzzleGenerator.get(10).getValue());
        Assert.assertEquals(solution.get(80).getValue(), puzzleGenerator.get(80).getValue());
    }

    @Test
    public void shouldSaveHitoryOfRemovedIndexes() {
        PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
        Board solution = new PuzzleMock(0);
        puzzleGenerator.useSolution(solution);
        puzzleGenerator.createPuzzle(45);
        List<Integer> indexHistory = puzzleGenerator.getRemovedIndexes();
        Assert.assertEquals(45, indexHistory.size());
    }

}
