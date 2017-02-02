package com.example.hammerox.oxsudoku.utils.gametools;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;

import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.ui.views.SudokuGrid;

/**
 * Created by Mauricio on 01-Feb-17.
 */

abstract class GameTool implements View.OnClickListener {

    private SudokuGrid sudokuGrid;
    private float defaultSize;
    private float smallSize;

    GameTool(Context context) {
        defaultSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, context.getResources().getDisplayMetrics());
        smallSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                10, context.getResources().getDisplayMetrics());
    }

    GameTool(Context context, SudokuGrid sudokuGrid) {
        this(context);
        this.sudokuGrid = sudokuGrid;
    }

    View getLayout(View view) {
        int id = 0;
        while(id != R.id.sudoku_button_container) {
            view = (View) view.getParent();
            id = view.getId();
        }
        return view;
    }

    SudokuGrid getSudokuGrid() {
        return sudokuGrid;
    }

    float getDefaultSize() {
        return defaultSize;
    }

    float getSmallSize() {
        return smallSize;
    }
}
