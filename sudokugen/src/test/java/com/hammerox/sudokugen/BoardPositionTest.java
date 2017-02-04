package com.hammerox.sudokugen;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class BoardPositionTest {

    @Test
    public void shouldReturnIndexAfterGivenPosition() {
        int index = 10;
        Position position = BoardPosition.getPosition(index);
        int row = position.row;
        int col = position.col;
        Assert.assertEquals(row, 2);
        Assert.assertEquals(col, 2);
        int newIndex = BoardPosition.getIndex(row, col);
        Assert.assertEquals(newIndex, index);

        index = 21;
        position = BoardPosition.getPosition(index);
        row = position.row;
        col = position.col;
        Assert.assertEquals(row, 3);
        Assert.assertEquals(col, 4);
        newIndex = BoardPosition.getIndex(row, col);
        Assert.assertEquals(newIndex, index);

        index = 62;
        position = BoardPosition.getPosition(index);
        row = position.row;
        col = position.col;
        Assert.assertEquals(row, 7);
        Assert.assertEquals(col, 9);
        newIndex = BoardPosition.getIndex(row, col);
        Assert.assertEquals(newIndex, index);

        index = 76;
        position = BoardPosition.getPosition(index);
        row = position.row;
        col = position.col;
        Assert.assertEquals(row, 9);
        Assert.assertEquals(col, 5);
        newIndex = BoardPosition.getIndex(row, col);
        Assert.assertEquals(newIndex, index);
    }

    @Test
    public void shouldReturnBoxAfterGivenPosition() {
        BoardPosition.Box box = BoardPosition.getBox(2, 3);
        Assert.assertEquals(box, BoardPosition.Box.TOP_LEFT);
        int[] indexBox = box.index;
        Assert.assertArrayEquals(indexBox, BoardPosition.Box.TOP_LEFT.index);

        box = BoardPosition.getBox(9, 9);
        Assert.assertEquals(box, BoardPosition.Box.BOTTOM_RIGHT);
        indexBox = box.index;
        Assert.assertArrayEquals(indexBox, BoardPosition.Box.BOTTOM_RIGHT.index);

        box = BoardPosition.getBox(5, 9);
        Assert.assertEquals(box, BoardPosition.Box.CENTER_RIGHT);
        indexBox = box.index;
        Assert.assertArrayEquals(indexBox, BoardPosition.Box.CENTER_RIGHT.index);

        box = BoardPosition.getBox(5, 9);
        Assert.assertEquals(box, BoardPosition.Box.CENTER_RIGHT);
        indexBox = box.index;
        Assert.assertArrayEquals(indexBox, BoardPosition.Box.CENTER_RIGHT.index);
    }

    @Test
    public void shouldThrowExceptionWhenRangeIsOut() throws Exception {
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            void run() throws Exception {
                BoardPosition.Box box = BoardPosition.getBox(0, 5);
            }
        });
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            void run() throws Exception {
                BoardPosition.Box box = BoardPosition.getBox(5, 0);
            }
        });
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            void run() throws Exception {
                BoardPosition.Box box = BoardPosition.getBox(5, 10);
            }
        });
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            void run() throws Exception {
                BoardPosition.Box box = BoardPosition.getBox(20, 5);
            }
        });
    }

    @Test
    public void shouldGiveReachedIndexesFromPosition() {
        Set<Integer> indexes = BoardPosition.getReachedIndexes(new Position(1, 1), true);
        boolean containItself = indexes.contains(0);
        Assert.assertTrue(containItself);
        boolean containRow = indexes.contains(1);
        Assert.assertTrue(containRow);
        boolean containCol = indexes.contains(9);
        Assert.assertTrue(containCol);
        boolean containBox = indexes.contains(20);
        Assert.assertTrue(containBox);

        indexes = BoardPosition.getReachedIndexes(new Position(9, 9), false);
        Assert.assertFalse(indexes.contains(80));
        Assert.assertTrue(indexes.contains(79));
        Assert.assertTrue(indexes.contains(71));
        Assert.assertTrue(indexes.contains(60));
    }

}
