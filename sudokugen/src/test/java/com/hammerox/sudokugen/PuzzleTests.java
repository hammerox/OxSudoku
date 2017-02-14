package com.hammerox.sudokugen;

import com.hammerox.sudokugen.util.Testable;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Mauricio on 14-Feb-17.
 */

public class PuzzleTests {

    @Test
    public void shouldBuildValidPuzzle() {
        Puzzle puzzle = new Puzzle();
        puzzle.create(10);
        Assert.assertTrue(puzzle.countEmptyCells() == 10);
    }

    @Test
    public void shouldNotAllowNegativeValues() throws Exception {
        final Puzzle puzzle = new Puzzle();
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                puzzle.create(-1);
            }
        });
    }

    @Test
    public void shouldNotAllowValuesGreaterThan81() throws Exception {
        final Puzzle puzzle = new Puzzle();
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                puzzle.create(82);
            }
        });
    }

}
