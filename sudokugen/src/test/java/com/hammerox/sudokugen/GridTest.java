package com.hammerox.sudokugen;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Mauricio on 21-Oct-16.
 */

public class GridTest {

    Grid grid;

    @Before
    public void setUp() {
        grid = Grid.getInstance();
    }

    @Test
    public void indexToRow() {
        byte[] indexToRow = grid.getIndexToRow();

        Assert.assertEquals(0, indexToRow[6]);
        Assert.assertEquals(0, indexToRow[8]);
        Assert.assertEquals(1, indexToRow[9]);
        Assert.assertEquals(2, indexToRow[21]);
        Assert.assertEquals(7, indexToRow[65]);
        Assert.assertEquals(8, indexToRow[77]);

    }


    @Test
    public void indexToCol() {
        byte[] indexToCol = grid.getIndexToCol();

        Assert.assertEquals(6, indexToCol[6]);
        Assert.assertEquals(8, indexToCol[8]);
        Assert.assertEquals(0, indexToCol[9]);
        Assert.assertEquals(3, indexToCol[21]);
        Assert.assertEquals(2, indexToCol[65]);
        Assert.assertEquals(5, indexToCol[77]);

    }


    @Test
    public void indexToBox() {

        byte[] indexToBox = grid.getIndexToBox();

        Assert.assertEquals(2, indexToBox[6]);
        Assert.assertEquals(2, indexToBox[8]);
        Assert.assertEquals(0, indexToBox[9]);
        Assert.assertEquals(1, indexToBox[21]);
        Assert.assertEquals(6, indexToBox[65]);
        Assert.assertEquals(7, indexToBox[77]);

    }


    @Test
    public void rowToIndex() {
        byte[][] rows = grid.getRowGrid();

        Assert.assertEquals(72, rows[8][0]);
        Assert.assertEquals(8, rows[0][8]);

        Assert.assertEquals(30, rows[3][3]);

        Assert.assertEquals(47, rows[5][2]);
        Assert.assertEquals(23, rows[2][5]);
    }


    @Test
    public void colToIndex() {
        byte[][] cols = grid.getColGrid();

        Assert.assertEquals(72, cols[0][8]);
        Assert.assertEquals(8, cols[8][0]);

        Assert.assertEquals(30, cols[3][3]);

        Assert.assertEquals(47, cols[2][5]);
        Assert.assertEquals(23, cols[5][2]);
    }


    @Test
    public void boxToIndex() {
        byte[][] boxes = grid.getBoxGrid();

        Assert.assertEquals(20, boxes[0][8]);
        Assert.assertEquals(60, boxes[8][0]);

        Assert.assertEquals(36, boxes[3][3]);

        Assert.assertEquals(17, boxes[2][5]);
        Assert.assertEquals(35, boxes[5][2]);
    }

}
