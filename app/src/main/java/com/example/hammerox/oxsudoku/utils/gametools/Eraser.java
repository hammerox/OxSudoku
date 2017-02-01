package com.example.hammerox.oxsudoku.utils.gametools;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;

import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.ui.views.SudokuKeyboard;

/**
 * Created by Mauricio on 01-Feb-17.
 */

public class Eraser extends GameTool {

    public Eraser(Context context) {
        super(context);
    }

    @Override
    public void onClick(View view) {
        boolean pencilMode = SudokuKeyboard.getPencilMode();
        boolean eraseMode = SudokuKeyboard.getEraseMode();

        eraseMode = !eraseMode;
        SudokuKeyboard.setEraseMode(eraseMode);

        Button button = (Button) view.findViewById(R.id.left_button_2);
        if (eraseMode) {
            SudokuKeyboard.setButtonColor(button, SudokuKeyboard.mColorAccent);
        } else {
            SudokuKeyboard.setButtonColor(button, SudokuKeyboard.mColorBackground);
        }

        View layout = getLayout(view);

        for (int i = 1; i <= 9; i++) {
            String idString = "key_" + i;
            int id = view.getContext().getResources()
                    .getIdentifier(idString, "id", view.getContext().getPackageName());
            Button keyButton = (Button) layout.findViewById(id);
            if (eraseMode) {
                keyButton.setTextColor(Color.WHITE);
            } else {
                keyButton.setTextColor(Color.BLACK);
            }
        }
    }
}
