package com.hammerox.sudokugen;

import org.junit.Assert;
import org.junit.Test;

public class GridPositionTest {

    @Test
    public void positionIndexRelation() {
        int index = 10;
        int[] position = GridPosition.getPositionFromIndex(index);
        int row = position[0];
        int col = position[1];
        Assert.assertEquals(row, 2);
        Assert.assertEquals(col, 2);
        int newIndex = GridPosition.getIndexFromPosition(row, col);
        Assert.assertEquals(newIndex, index);

        index = 21;
        position = GridPosition.getPositionFromIndex(index);
        row = position[0];
        col = position[1];
        Assert.assertEquals(row, 3);
        Assert.assertEquals(col, 4);
        newIndex = GridPosition.getIndexFromPosition(row, col);
        Assert.assertEquals(newIndex, index);

        index = 62;
        position = GridPosition.getPositionFromIndex(index);
        row = position[0];
        col = position[1];
        Assert.assertEquals(row, 7);
        Assert.assertEquals(col, 9);
        newIndex = GridPosition.getIndexFromPosition(row, col);
        Assert.assertEquals(newIndex, index);

        index = 76;
        position = GridPosition.getPositionFromIndex(index);
        row = position[0];
        col = position[1];
        Assert.assertEquals(row, 9);
        Assert.assertEquals(col, 5);
        newIndex = GridPosition.getIndexFromPosition(row, col);
        Assert.assertEquals(newIndex, index);
    }

}
