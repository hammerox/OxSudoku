package com.example.hammerox.oxsudoku;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;


public class SudokuKeyboard {

    private static int activeKey = 0;

    public SudokuKeyboard() {
    }

/*Todo - Add Undo button*/
/*Todo - Add Pencil button*/
/*Todo - Show counters with remaining numbers*/
/*Todo - Idenfity completed numbers*/

    public void drawKeyboard(final Activity activity, final View rootView, final SudokuGrid sudokuGrid) {

        for (int key = 1; key <= 9; key++) {
            // Getting each button from keyboard's view.
            String idString = "key_" + key;
            int id = activity.getResources()
                    .getIdentifier(idString, "id", activity.getPackageName());
            Button keyButton = (Button) rootView.findViewById(id);
                // Appearance
            ColorStateList mColor = ContextCompat.getColorStateList(activity, R.color.colorMediumGray);
            ViewCompat.setBackgroundTintList(keyButton, mColor);
            keyButton.setText("" + key);
            keyButton.setGravity(Gravity.CENTER);
            keyButton.setTypeface(Typeface.DEFAULT_BOLD);
            keyButton.setTextSize(30);
                // Interaction
            keyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Todo - Unselect activeKey*/
                    // On click, make last active key back to default's appearance.
                    if (activeKey != 0) {       // activeKey = 0 means no key was clicked previously.
                        String idString = "key_" + activeKey;
                        int id = activity.getResources()
                                .getIdentifier(idString, "id", activity.getPackageName());
                        Button lastKey = (Button) rootView.findViewById(id);
                        ColorStateList mReleaseColor = ContextCompat
                                .getColorStateList(activity, R.color.colorMediumGray);
                        ViewCompat.setBackgroundTintList(lastKey, mReleaseColor);
                    }

                    // Changes active key's color.
                    Button pressedKey = (Button)v;
                    ColorStateList mPressedColor = ContextCompat
                            .getColorStateList(activity, R.color.colorAccent);
                    ViewCompat.setBackgroundTintList(pressedKey, mPressedColor);
                    activeKey = Integer.valueOf(pressedKey.getText().toString());

                    // Highlight grid's keys
                    sudokuGrid.setPuzzleHighlight(activity, activeKey);
                }
            });
        }
    }
    
    public void setClickListeners(final View rootView) {
        Button leftButton1 = (Button) rootView.findViewById(R.id.left_button_1);
        leftButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button leftButton2 = (Button) rootView.findViewById(R.id.left_button_2);
        leftButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button rightButton1 = (Button) rootView.findViewById(R.id.right_button_1);
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button rightButton2 = (Button) rootView.findViewById(R.id.right_button_2);
        rightButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static int getActiveKey() {
        return activeKey;
    }

    public static void setActiveKey(int activeKey) {
        SudokuKeyboard.activeKey = activeKey;
    }
}
