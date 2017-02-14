package com.hammerox.sudokugen;

import java.util.Set;

import static com.hammerox.sudokugen.Board.getPosition;

/**
 * Created by Mauricio on 13-Feb-17.
 */

public class BoardLogger {

    private static Board board;
    private static int step;
    private static int maxStep;
    private static int index;
    private static Position position;
    private static Set<Integer> availableValues;
    private static StringBuffer buffer;

    public static void log(Board b, int s, int i, Set<Integer> available) {
        buffer = new StringBuffer();
        board = b;
        step = s;
        maxStep = (step > maxStep) ? step : maxStep;
        index = i;
        position = getPosition(i);
        availableValues = available;
        firstAndLastRow();
        for (int row = 1; row <= 9; row++) {
            logRow(row);
        }
        firstAndLastRow();
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
            appendSpace();
        }
        if (position.col - 1 == col && position.row == row){
            buffer.append("(");
        } else if (position.col == col && position.row == row) {
            buffer.append(")");
        } else {
            appendSpace();
        }
    }

    private static void logRowStart(int row) {
        verticalBorder(row);
        if (position.col == 1 && position.row == row) {
            buffer.append("(");
        } else {
            appendSpace();
        }
    }

    private static void logRowEnd(int row) {
        verticalBorder(row);
        appendSpace();
        displayInfo(row);
        buffer.append("\n");
    }

    private static void displayInfo(int row) {
        switch (row) {
            case 1:
                buffer.append("Step: ");
                buffer.append(step);
                break;
            case 2:
                buffer.append("Max Step: ");
                buffer.append(maxStep);
                break;
            case 3:
                buffer.append("Index: ");
                buffer.append(index);
                break;
            case 4:
                buffer.append("Row: ");
                buffer.append(position.row);
                break;
            case 5:
                buffer.append("Col: ");
                buffer.append(position.col);
                break;
            case 6:
                buffer.append("Box: ");
                buffer.append(Board.getBox(position).name());
                break;
            case 7:
                buffer.append("Available Values: ");
                buffer.append("[ ");
                for (Integer i : availableValues) {
                    buffer.append(i);
                    appendSpace();
                }
                buffer.append("]");
                break;
            case 8:
                buffer.append("Available Count: ");
                buffer.append(availableValues.size());
                break;
        }
    }

    private static void firstAndLastRow() {
        appendSpace();
        for (int i = 1; i <= 19; i++) {
            switch (i) {
                case 7:
                case 13:
                    buffer.append("|");
                    break;
                default:
                    buffer.append("-");
            }
        }
        appendSpace();
        buffer.append(" \n");
    }

    private static void appendSpace() {
        buffer.append(" ");
    }

    private static void verticalBorder(int row) {
        switch (row) {
            case 4:
            case 5:
            case 6:
                buffer.append("-");
                break;
            default:
                buffer.append("|");
                break;
        }
    }

}
