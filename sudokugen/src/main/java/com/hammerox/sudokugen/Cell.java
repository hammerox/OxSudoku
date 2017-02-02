package com.hammerox.sudokugen;

/**
 * Created by Mauricio on 02-Feb-17.
 */

public class Cell {

    public final int index;
    public final int row;
    public final int col;
    public final GridPosition.Box box;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.index = GridPosition.getIndex(row, col);
        this.box = GridPosition.getBox(row, col);
    }
}
