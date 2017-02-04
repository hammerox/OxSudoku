package com.hammerox.sudokugen;

import java.util.ArrayList;

/**
 * Created by Mauricio on 20-Oct-16.
 */
public class Board extends ArrayList<Cell> {


    public Board() {
        initializeBoard();
    }


    public Cell get(Position position) {
        int index = BoardPosition.getIndex(position);
        return super.get(index);
    }

    public Cell get(int row, int col) {
        int index = BoardPosition.getIndex(row, col);
        return super.get(index);
    }

    private void initializeBoard() {
        if (!this.isEmpty()) {
            this.clear();
        }
        for (int i = 0; i < 81; i++) {
            Position position = BoardPosition.getPosition(i);
            this.add(new Cell(position));
        }
    }

}
