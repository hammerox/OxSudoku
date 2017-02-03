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
    public final BoardPosition.Box box;
    private final Set<Integer> reach;


    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.position = new Position(row, col);
        this.index = BoardPosition.getIndex(row, col);
        this.box = BoardPosition.getBox(row, col);
        this.reach = BoardPosition.getReachedIndexes(position, false);
    }

    public Cell(Position position) {
        this(position.row, position.col);
    }


    public Set<Integer> getReach() {
        return new HashSet<>(reach);
    }
}
