package com.hammerox.sudokugen;

import static com.hammerox.sudokugen.Board.getPosition;

/**
 * Created by Mauricio on 13-Feb-17.
 */

public class BoardLogger {

    private static Board board;
    private static Position position;
    private static StringBuffer buffer;

    public static void log(Board b, int index) {
        buffer = new StringBuffer();
        board = b;
        position = getPosition(index);
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
        Cell c = board.get(row, col);
        if (c.hasValue()) {
            buffer.append(c.getValue());
        } else {
            buffer.append(" ");
        }
        buffer.append(" ");
    }

    private static void logRowEnd(int row) {
        if (row == position.row) {
            buffer.append("-\n");
        } else {
            buffer.append("|\n");
        }
    }

    private static void logRowStart(int row) {
        if (row == position.row) {
            buffer.append("- ");
        } else {
            buffer.append("| ");
        }
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
