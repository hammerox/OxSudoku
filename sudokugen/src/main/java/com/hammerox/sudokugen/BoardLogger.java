package com.hammerox.sudokugen;

import java.util.Set;

import static com.hammerox.sudokugen.Board.getPosition;

/**
 * Created by Mauricio on 13-Feb-17.
 */

public class BoardLogger {

    private static Board board;
    private static int index;
    private static Position position;
    private static Set<Integer> availableValues;
    private static StringBuffer buffer;

    public static void log(Board b, int i, Set<Integer> available) {
        buffer = new StringBuffer();
        board = b;
        index = i;
        position = getPosition(i);
        availableValues = available;
        logColumn();
        for (int row = 1; row <= 9; row++) {
            logRow(row);
        }
        logColumn();
        System.out.print(buffer.toString());
    }

    private static void logRow(int row) {
        logRowStart(row);
        for (int col = 1; col <= 9; col++) {
            logValue(row, col);
        }
        logRowEnd(row);
    }

    private static void logValue(int row, int col) {
        Cell cell = board.get(row, col);
        if (cell.hasValue()) {
            buffer.append(cell.getValue());
        } else {
            buffer.append(" ");
        }
        if (position.col - 1 == col && position.row == row){
            buffer.append("(");
        } else if (position.col == col && position.row == row) {
            buffer.append(")");
        } else {
            buffer.append(" ");
        }
    }

    private static void logRowStart(int row) {
        if (row == position.row) {
            buffer.append("- ");
        } else {
            buffer.append("| ");
        }
    }

    private static void logRowEnd(int row) {
        if (row == position.row) {
            buffer.append("-");
        } else {
            buffer.append("|");
        }

        buffer.append(" ");

        switch (row) {
            case 1:
                buffer.append("Index: ");
                buffer.append(index);
                break;
            case 2:
                buffer.append("Row: ");
                buffer.append(position.row);
                break;
            case 3:
                buffer.append("Col: ");
                buffer.append(position.col);
                break;
            case 4:
                buffer.append("Box: ");
                buffer.append(Board.getBox(position).name());
                break;
            case 5:
                buffer.append("Available Values: ");
                buffer.append("[ ");
                for (Integer i : availableValues) {
                    buffer.append(i);
                    buffer.append(" ");
                }
                buffer.append("]");
                break;
            case 6:
                buffer.append("Available Count: ");
                buffer.append(availableValues.size());
                break;
        }

        buffer.append("\n");
    }

    private static void logColumn() {
        buffer.append(" -");
        for (int col = 1; col <= 9; col++) {
            if(col == position.col) {
                buffer.append("|-");
            } else {
                buffer.append("--");
            }
        }
        buffer.append(" \n");
    }

}
