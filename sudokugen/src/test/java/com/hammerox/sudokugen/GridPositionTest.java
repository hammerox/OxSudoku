package com.hammerox.sudokugen;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GridPositionTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldReturnIndexAfterGivenPosition() {
        int index = 10;
        int[] position = GridPosition.getPosition(index);
        int row = position[0];
        int col = position[1];
        Assert.assertEquals(row, 2);
        Assert.assertEquals(col, 2);
        int newIndex = GridPosition.getIndex(row, col);
        Assert.assertEquals(newIndex, index);

        index = 21;
        position = GridPosition.getPosition(index);
        row = position[0];
        col = position[1];
        Assert.assertEquals(row, 3);
        Assert.assertEquals(col, 4);
        newIndex = GridPosition.getIndex(row, col);
        Assert.assertEquals(newIndex, index);

        index = 62;
        position = GridPosition.getPosition(index);
        row = position[0];
        col = position[1];
        Assert.assertEquals(row, 7);
        Assert.assertEquals(col, 9);
        newIndex = GridPosition.getIndex(row, col);
        Assert.assertEquals(newIndex, index);

        index = 76;
        position = GridPosition.getPosition(index);
        row = position[0];
        col = position[1];
        Assert.assertEquals(row, 9);
        Assert.assertEquals(col, 5);
        newIndex = GridPosition.getIndex(row, col);
        Assert.assertEquals(newIndex, index);
    }

    @Test
    public void shouldReturnBoxAfterGivenPosition() {
        GridPosition.Box box = GridPosition.getBox(2, 3);
        Assert.assertEquals(box, GridPosition.Box.TOP_LEFT);
        int[] indexBox = box.index;
        Assert.assertArrayEquals(indexBox, GridPosition.Box.TOP_LEFT.index);

        box = GridPosition.getBox(9, 9);
        Assert.assertEquals(box, GridPosition.Box.BOTTOM_RIGHT);
        indexBox = box.index;
        Assert.assertArrayEquals(indexBox, GridPosition.Box.BOTTOM_RIGHT.index);

        box = GridPosition.getBox(5, 9);
        Assert.assertEquals(box, GridPosition.Box.CENTER_RIGHT);
        indexBox = box.index;
        Assert.assertArrayEquals(indexBox, GridPosition.Box.CENTER_RIGHT.index);

        box = GridPosition.getBox(5, 9);
        Assert.assertEquals(box, GridPosition.Box.CENTER_RIGHT);
        indexBox = box.index;
        Assert.assertArrayEquals(indexBox, GridPosition.Box.CENTER_RIGHT.index);
    }

    @Test
    public void shouldThrowExceptionWhenRangeIsOut() {
        exception.expect(IndexOutOfBoundsException.class);
        GridPosition.Box box = GridPosition.getBox(0, 5);
        box = GridPosition.getBox(5, 0);
        box = GridPosition.getBox(5, 10);
        box = GridPosition.getBox(20, 5);
    }

}
