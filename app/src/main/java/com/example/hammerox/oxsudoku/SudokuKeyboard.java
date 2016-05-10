package com.example.hammerox.oxsudoku;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import com.example.hammerox.oxsudoku.Tools.SquareLayout;


public class SudokuKeyboard {

    private static int activeKey = 0;

    public SudokuKeyboard() {
    }

    public void createKeyboard(final Activity activity, final View rootView) {

        for (int key = 1; key <= 9; key++) {
            String idString = "key_" + key;
            int id = activity.getResources()
                    .getIdentifier(idString, "id", activity.getPackageName());
            Button keyButton = (Button) rootView.findViewById(id);

            ColorStateList mColor = ContextCompat.getColorStateList(activity, R.color.colorMediumGray);
            ViewCompat.setBackgroundTintList(keyButton, mColor);
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
                        ColorStateList mReleaseColor = ContextCompat
                                .getColorStateList(activity, R.color.colorMediumGray);
                        ViewCompat.setBackgroundTintList(lastKey, mReleaseColor);

                    }
                    Button pressedKey = (Button)v;
                    ColorStateList mPressedColor = ContextCompat
                            .getColorStateList(activity, R.color.colorAccent);
                    ViewCompat.setBackgroundTintList(pressedKey, mPressedColor);
                    activeKey = Integer.valueOf(pressedKey.getText().toString());
                }
            });
        }
    }

    public static int getActiveKey() {
        return activeKey;
    }

    public static void setActiveKey(int activeKey) {
        SudokuKeyboard.activeKey = activeKey;
    }
}
