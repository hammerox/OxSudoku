package com.hammerox.sudokugen;

/**
 * Created by Mauricio on 20-Oct-16.
 */
public class Grid {

    private final static Grid INSTANCE;

    private static byte[] indexToRow;
    private static byte[] indexToCol;
    private static byte[] indexToBox;

    private static byte[][] rowToIndex;
    private static byte[][] colToIndex;
    private static byte[][] boxToIndex;


    private Grid() {}

    static {

        try{
            INSTANCE = new Grid();
        } catch(Exception e) {
            throw new RuntimeException("Exception occured in creating singleton INSTANCE");
        }

    }
    

    //
    // Getters and array inicialization
    //

    public static Grid getInstance(){
        return INSTANCE;
    }


    public byte[] getIndexToRow() {

        if (indexToRow == null) {
            createIndexToRow();
        }

        return indexToRow;

    }


    public byte[] getIndexToCol() {

        if (indexToCol == null) {
            createIndexToCol();
        }

        return indexToCol;

    }


    public byte[] getIndexToBox() {

        if (indexToBox == null) {
            createIndexToBox();
        }

        return indexToBox;

    }


    public byte[][] getRowGrid() {

        if (rowToIndex == null) {
            createRowGrid();
        }

        return rowToIndex;
    }


    public byte[][] getColGrid() {

        if (colToIndex == null) {
            createColGrid();
        }

        return colToIndex;

    }


    public byte[][] getBoxGrid() {

        if (boxToIndex == null) {

            boxToIndex = new byte[9][9];

            createFirstBox();
            createOtherBoxes();

        }

        return boxToIndex;

    }


    //
    // Private Methods
    //

    private void createIndexToRow() {

        indexToRow = new byte[81];

        for (int index = 0; index < 81; index++) {

            indexToRow[index] = (byte) (index / 9);

        }

    }


    private void createIndexToCol() {

        indexToCol = new byte[81];

        for (int index = 0; index < 81; index++) {

            indexToCol[index] = (byte) (index % 9);

        }

    }


    private void createIndexToBox() {

        indexToBox = new byte[81];
        boxToIndex = getBoxGrid();

        for (int box = 0; box < 9; box++) {

            for (int j = 0; j < 9; j++) {

                indexToBox[ boxToIndex[box][j] ] = (byte) box;

            }

        }

    }


    private void createRowGrid() {

        rowToIndex = new byte[9][9];
        byte index = 0;

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                rowToIndex[row][col] = index++;

            }

        }

    }


    private void createColGrid() {

        colToIndex = new byte[9][9];
        byte index = 0;

        for (int row = 0; row < 9; row++) {

            for (int col = 0; col < 9; col++) {

                colToIndex[col][row] = index++;

            }

        }

    }


    private void createFirstBox() {

        boxToIndex[0][0] = 0;
        int sumIndex = 0;
        int[] sumArray = new int[] {1, 1, 7};

        for (int j = 1; j < 9; j++) {

            boxToIndex[0][j] = (byte) (boxToIndex[0][j-1] + sumArray[sumIndex]);
            sumIndex = sumIndex == 2 ? 0 : ++sumIndex;

        }

    }


    private void createOtherBoxes() {

        byte[] sumArray = new byte[] {3, 3, 21};

        for (int j = 0; j < 9; j++) {

            int sumIndex = 0;

            for (int box = 1; box < 9; box++) {

                boxToIndex[box][j] = (byte) (boxToIndex[box - 1][j] + sumArray[sumIndex]);
                sumIndex = sumIndex == 2 ? 0 : ++sumIndex;

            }

        }

    }

}
