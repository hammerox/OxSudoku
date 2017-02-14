package com.hammerox.sudokugen;


import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import com.hammerox.sudokugen.Board.Box;
import com.hammerox.sudokugen.util.Testable;


/**
 * Created by Mauricio on 21-Oct-16.
 */

public class BoardTests {

    @Test
    public void shouldCreateBoardWithAllCells() {
        Board board = new Board();
        Assert.assertEquals(81, board.size());
    }

    @Test
    public void shouldGetAllPossibleValuesOfACell() {
        Board board = new Board();
        setBoardValues(board);
        Set<Integer> availableValues = board.getAvailableValues(0);
        assertValuesAvailableOnSet(availableValues);
    }

    @Test
    public void shouldTestIfValueIsPossible() {
        Board board = new Board();
        setBoardValues(board);
        assertSingleValuesAvailable(board);
    }

    @Test
    public void shouldReturnIndexAfterGivenPosition() {
        assertIndex(10, 2, 2);
        assertIndex(21, 3, 4);
        assertIndex(62, 7, 9);
        assertIndex(76, 9, 5);
    }

    @Test
    public void shouldReturnBoxAfterGivenPosition() {
        Box box = Board.getBox(2, 3);
        assertBox(Box.TOP_LEFT, box);
        box = Board.getBox(9, 9);
        assertBox(Box.BOTTOM_RIGHT, box);
        box = Board.getBox(5, 9);
        assertBox(Box.CENTER_RIGHT, box);
    }

    @Test
    public void shouldThrowExceptionWhenRangeIsOut() throws Exception {
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                Board.Box box = Board.getBox(0, 5);
            }
        });
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                Board.Box box = Board.getBox(5, 0);
            }
        });
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                Board.Box box = Board.getBox(5, 10);
            }
        });
        Testable.assertThrows(IndexOutOfBoundsException.class, new Testable() {
            @Override
            public void run() throws Exception {
                Board.Box box = Board.getBox(20, 5);
            }
        });
    }

    @Test
    public void shouldGiveReachedIndexesFromPosition() {
        Set<Integer> indexes = Board.getIndexesInRange(new Position(1, 1), true);
        boolean containItself = indexes.contains(0);
        Assert.assertTrue(containItself);
        boolean containRow = indexes.contains(1);
        Assert.assertTrue(containRow);
        boolean containCol = indexes.contains(9);
        Assert.assertTrue(containCol);
        boolean containBox = indexes.contains(20);
        Assert.assertTrue(containBox);

        indexes = Board.getIndexesInRange(new Position(9, 9), false);
        Assert.assertFalse(indexes.contains(80));
        Assert.assertTrue(indexes.contains(79));
        Assert.assertTrue(indexes.contains(71));
        Assert.assertTrue(indexes.contains(60));
    }

    @Test
    public void shouldCountCellsWithValue() {
        Board board = new Board();
        setBoardValues(board);
        Assert.assertEquals(9, board.countFilledCells());
    }

    @Test
    public void shouldCountCellsWithoutValue() {
        Board board = new Board();
        setBoardValues(board);
        Assert.assertEquals(72, board.countEmptyCells());
    }

    private void setBoardValues(Board board) {
        // Cells in range of i = 0
        board.get(2).setValue(1);
        board.get(5).setValue(2);
        board.get(9).setValue(3);
        board.get(27).setValue(4);
        board.get(10).setValue(5);
        // Cells out of range of i = 0
        board.get(50).setValue(6);
        board.get(60).setValue(7);
        board.get(70).setValue(8);
        board.get(80).setValue(9);
    }

    private void assertValuesAvailableOnSet(Set<Integer> availableValues) {
        Assert.assertTrue(availableValues.contains(9));
        Assert.assertTrue(availableValues.contains(8));
        Assert.assertTrue(availableValues.contains(7));
        Assert.assertTrue(availableValues.contains(6));
        Assert.assertFalse(availableValues.contains(5));
        Assert.assertFalse(availableValues.contains(4));
        Assert.assertFalse(availableValues.contains(3));
        Assert.assertFalse(availableValues.contains(2));
        Assert.assertFalse(availableValues.contains(1));
    }

    private void assertSingleValuesAvailable(Board board) {
        Assert.assertTrue(board.isValueAvailable(0, 9));
        Assert.assertTrue(board.isValueAvailable(0, 8));
        Assert.assertTrue(board.isValueAvailable(0, 7));
        Assert.assertTrue(board.isValueAvailable(0, 6));
        Assert.assertFalse(board.isValueAvailable(0, 5));
        Assert.assertFalse(board.isValueAvailable(0, 4));
        Assert.assertFalse(board.isValueAvailable(0, 3));
        Assert.assertFalse(board.isValueAvailable(0, 2));
        Assert.assertFalse(board.isValueAvailable(0, 1));
    }

    private void assertIndex(int index, int actualRow, int actualCol) {
        Position position = Board.getPosition(index);
        int row = position.row;
        int col = position.col;
        Assert.assertEquals(row, actualRow);
        Assert.assertEquals(col, actualCol);
        int newIndex = Board.getIndex(row, col);
        Assert.assertEquals(newIndex, index);
    }

    private void assertBox(Box expected, Box actual) {
        Assert.assertEquals(expected, actual);
        int[] indexBox = actual.index;
        Assert.assertArrayEquals(expected.index, indexBox);
    }
}
