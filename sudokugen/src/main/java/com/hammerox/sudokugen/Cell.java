package com.hammerox.sudokugen;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mauricio on 02-Feb-17.
 */

public class Cell {

    public final int index;
    public final Position position;
    public final int row;
    public final int col;
    public final Board.Box box;

    private final Set<Integer> indexesInRange;
    private int value;
    private boolean hasValue;


    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.position = new Position(row, col);
        this.index = Board.getIndex(row, col);
        this.box = Board.getBox(row, col);
        this.indexesInRange = Board.getIndexesInRange(position, false);
    }

    public Cell(Position position) {
        this(position.row, position.col);
    }

    public Cell(int index) {
        this(Board.getPosition(index));
    }

    public Set<Integer> getIndexesInRange() {
        return new HashSet<>(indexesInRange);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (isBetween(value, 1, 9)) {
            this.value = value;
            hasValue = true;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public void clearValue() {
        value = 0;
        hasValue = false;
    }

    public boolean hasValue() {
        return hasValue;
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

}
