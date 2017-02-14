package com.hammerox.sudokugen;

import com.hammerox.sudokugen.util.Testable;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Mauricio on 14-Feb-17.
 */

public class PuzzleGeneratorTests {

    @Test
    public void shouldBuildValidPuzzle() {
        PuzzleGenerator puzzleGenerator = new PuzzleGenerator();
        puzzleGenerator.createPuzzle(10);
        BoardLogger.log(puzzleGenerator);
        Assert.assertTrue(puzzleGenerator.countEmptyCells() == 10);
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

}
