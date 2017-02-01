package com.example.hammerox.oxsudoku.utils.gametools;

import android.content.Context;
import android.view.View;

import com.example.hammerox.oxsudoku.ui.views.SudokuGrid;

/**
 * Created by Mauricio on 01-Feb-17.
 */

public class Undo extends GameTool {

    public Undo(Context context, SudokuGrid sudokuGrid) {
        super(context, sudokuGrid);
    }

    @Override
    public void onClick(View view) {
        getSudokuGrid().undoLastInput(view.getContext());
    }
}
