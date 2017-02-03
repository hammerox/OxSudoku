package com.hammerox.sudokugen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 20-Oct-16.
 */
public class Board {

    private List<Cell> cells;


    public Board() {
        initializeBoard();
    }


    private Cell getCell(int index) {
        return cells.get(index);
    }

    private Cell getCell(Position position) {
        int index = BoardPosition.getIndex(position);
        return getCell(index);
    }

    private Cell getCell(int row, int col) {
        int index = BoardPosition.getIndex(row, col);
        return getCell(index);
    }

    private void initializeBoard() {
        cells = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            Position position = BoardPosition.getPosition(i);
            cells.add(new Cell(position));
        }
    }

}
