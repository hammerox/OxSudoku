package com.hammerox.sudokugen;

import com.hammerox.sudokugen.util.Testable;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Mauricio on 02-Feb-17.
 */

public class CellTests {

    @Test
    public void shouldCreateCellWithIntegers() {
        int row = 1;
        int col = 1;
        Cell cell = new Cell(row, col);
        Assert.assertEquals(0, cell.index);
        Assert.assertEquals(1, cell.row);
        Assert.assertEquals(1, cell.col);
        Assert.assertEquals(Board.Box.TOP_LEFT, cell.box);
    }

    @Test
    public void shouldCreateCellWithPosition() {
        Position position = new Position(2, 3);
        Cell cell = new Cell(position);
        Assert.assertEquals(11, cell.index);
        Assert.assertEquals(2, cell.row);
        Assert.assertEquals(3, cell.col);
        Assert.assertEquals(Board.Box.TOP_LEFT, cell.box);
    }

    @Test
    public void shouldCreateCellWithIndex() {
        int index = 8;
        Cell cell = new Cell(index);
        Assert.assertEquals(8, cell.index);
        Assert.assertEquals(1, cell.row);
        Assert.assertEquals(9, cell.col);
        Assert.assertEquals(Board.Box.TOP_RIGHT, cell.box);
    }

    @Test
    public void shouldSetAndGetCellValues() {
        Cell cell = new Cell(0);
        Assert.assertEquals(0, cell.getValue());
        cell.setValue(3);
        Assert.assertEquals(3, cell.getValue());
        cell.clearValue();
        Assert.assertEquals(0, cell.getValue());
    }

    @Test
    public void shouldThrowExceptionWhenSetInvalidValues() throws Exception {
        final Cell cell = new Cell(0);
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                cell.setValue(-1);
            }
        });

        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                cell.setValue(10);
            }
        });
    }

    @Test
    public void shouldDisplayIfContainsValue() {
        Cell cell = new Cell(80);
        Assert.assertFalse(cell.hasValue());
        cell.setValue(9);
        Assert.assertTrue(cell.hasValue());
        cell.clearValue();
        Assert.assertFalse(cell.hasValue());
    }

    @Test
    public void shouldCloneCells() {
        Cell cell = new Cell(0);
        cell.setValue(9);
        Cell cloneCell = cell.clone();
        Assert.assertEquals(0, cloneCell.index);
        Assert.assertEquals(9, cloneCell.getValue());
        Assert.assertFalse(cloneCell == cell);
    }

}
