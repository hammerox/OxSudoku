package com.hammerox.sudokugen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 20-Oct-16.
 */
public class Grid {

    private List<Cell> cells;


    public Grid() {
        initializeGrid();
    }


    private Cell getCell(int index) {
        return cells.get(index);
    }

    private Cell getCell(Position position) {
        int index = GridPosition.getIndex(position);
        return getCell(index);
    }

    private Cell getCell(int row, int col) {
        int index = GridPosition.getIndex(row, col);
        return getCell(index);
    }

    private void initializeGrid() {
        cells = new ArrayList<>();
        for (int i = 0; i < 81; i++) {
            Position position = GridPosition.getPosition(i);
            cells.add(new Cell(position));
        }
    }

}
