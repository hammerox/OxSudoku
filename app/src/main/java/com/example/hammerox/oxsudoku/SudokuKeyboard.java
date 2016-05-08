package com.example.hammerox.oxsudoku;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;


public class SudokuKeyboard {

    private static int activeKey = 0;

    public SudokuKeyboard() {
    }

    public void createKeyboard(final Activity activity, final View rootView, int keyDim) {

        int keyboardDim = keyDim * 3;

        GridLayout keyboardLayout = (GridLayout) rootView.findViewById(R.id.sudoku_keyboard);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(keyboardDim, keyboardDim);
        params.addRule(RelativeLayout.BELOW, R.id.sudoku_gridlayout);
        keyboardLayout.setLayoutParams(params);
        for (int key = 1; key <= 9; key++) {
            Button keyButton = new Button(activity);
            keyButton.setLayoutParams(new ViewGroup.LayoutParams(keyDim, keyDim));
            String idString = "key_" + key;
            int id = activity.getResources()
                    .getIdentifier(idString, "id", activity.getPackageName());
            keyButton.setId(id);
            keyButton.setText("" + key);
            keyButton.setGravity(Gravity.CENTER);
            keyButton.setTypeface(Typeface.DEFAULT_BOLD);
            keyButton.setTextSize(30);
            keyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activeKey != 0) {
                        String idString = "key_" + activeKey;
                        int id = activity.getResources()
                                .getIdentifier(idString, "id", activity.getPackageName());
                        Button lastKey = (Button) rootView.findViewById(id);
                        lastKey.getBackground().setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
                    }
                    Button pressedKey = (Button)v;
                    int mColor = ContextCompat.getColor(activity, R.color.colorAccent);
                    pressedKey.getBackground().setColorFilter(mColor, PorterDuff.Mode.SRC_ATOP);
                    activeKey = Integer.valueOf(pressedKey.getText().toString());
                }
            });
            keyboardLayout.addView(keyButton);
        }
    }

    public static int getActiveKey() {
        return activeKey;
    }

    public static void setActiveKey(int activeKey) {
        SudokuKeyboard.activeKey = activeKey;
    }
}
