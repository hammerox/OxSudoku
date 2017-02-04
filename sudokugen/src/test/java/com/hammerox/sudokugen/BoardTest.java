package com.hammerox.sudokugen;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by Mauricio on 21-Oct-16.
 */

public class BoardTest {

    private Board board;

    @Before
    public void initialize() {
        board = new Board();
    }

    @Test
    public void shouldCreateBoardWithAllCells() {
        Assert.assertEquals(81, board.size());
    }

}
