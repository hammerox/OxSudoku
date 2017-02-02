package com.hammerox.sudokugen;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Mauricio on 02-Feb-17.
 */

public class CellTest  {

    @Test
    public void shouldCreateCellWithIntegers() {
        int row = 1;
        int col = 1;
        Cell cell = new Cell(row, col);
        Assert.assertEquals(0, cell.index);
        Assert.assertEquals(1, cell.row);
        Assert.assertEquals(1, cell.col);
        Assert.assertEquals(GridPosition.Box.TOP_LEFT, cell.box);
    }

    @Test
    public void shouldCreateCellWithPosition() {
        Position position = new Position(2, 3);
        Cell cell = new Cell(position);
        Assert.assertEquals(11, cell.index);
        Assert.assertEquals(2, cell.row);
        Assert.assertEquals(3, cell.col);
        Assert.assertEquals(GridPosition.Box.TOP_LEFT, cell.box);
    }

}
