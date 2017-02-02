package com.example.hammerox.oxsudoku.utils;


import android.content.Context;
import android.view.View;

import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.ui.views.SudokuGrid;
import com.example.hammerox.oxsudoku.utils.gametools.CheckAnswer;
import com.example.hammerox.oxsudoku.utils.gametools.Eraser;
import com.example.hammerox.oxsudoku.utils.gametools.Pencil;
import com.example.hammerox.oxsudoku.utils.gametools.Undo;

/**
 * Created by Mauricio on 01-Feb-17.
 */

public enum Tool {

    PENCIL(R.id.left_button_1),
    ERASER(R.id.left_button_2),
    CHECK(R.id.right_button_1),
    UNDO(R.id.right_button_2);

    private int id;

    Tool(int id) {
        this.id = id;
    }


    public View.OnClickListener getClick(Context context, SudokuGrid grid) {
        switch (this) {
            case PENCIL:
                return new Pencil(context);
            case ERASER:
                return new Eraser(context);
            case CHECK:
                return new CheckAnswer(context, grid);
            case UNDO:
                return new Undo(context, grid);
            default:
                throw new NullPointerException();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
