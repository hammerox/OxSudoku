package com.example.hammerox.oxsudoku;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;


public class SudokuKeyboard {

    public SudokuKeyboard() {
    }

    public static void createKeyboard(Activity activity, View rootView, int keyDim) {

        int keyboardDim = keyDim * 3;

        GridLayout keyboardLayout = (GridLayout) rootView.findViewById(R.id.sudoku_keyboard);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(keyboardDim, keyboardDim);
        params.addRule(RelativeLayout.BELOW, R.id.sudoku_gridlayout);
        keyboardLayout.setLayoutParams(params);
        for (int key = 1; key <= 9; key++) {
            Button button = new Button(activity);
            button.setLayoutParams(new ViewGroup.LayoutParams(keyDim, keyDim));
            String idString = "key_" + key;
            int id = activity.getResources()
                    .getIdentifier(idString, "id", activity.getPackageName());
            button.setId(id);
            button.setText("" + key);
            button.setGravity(Gravity.CENTER);
            button.setTypeface(Typeface.DEFAULT_BOLD);
            button.setTextSize(30);
            keyboardLayout.addView(button);
        }
    }

}
