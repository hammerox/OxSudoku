package com.example.hammerox.oxsudoku;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;


public class SudokuGrid {

    public SudokuGrid() {
    }

    public static void createGrid(Activity activity, View rootView, int squareSize) {

        Context context = activity.getApplicationContext();

        GridLayout gridLayout = (GridLayout) rootView.findViewById(R.id.sudoku_gridlayout);
        for (int row = 1; row <= 9; row++) {
            for (int col = 1; col <= 9; col++) {
                Drawable mDrawable;
                if (col == 1) {
                    switch (row) {
                        case 3:
                        case 6:
                        case 9:
                            mDrawable = ResourcesCompat
                                    .getDrawable(context.getResources(), R.drawable.grid_border_d, null);
                            break;
                        default:
                            mDrawable = ResourcesCompat
                                    .getDrawable(context.getResources(), R.drawable.grid_border_a, null);
                            break;
                    }
                } else if (col == 3 || col == 6) {
                    switch (row) {
                        case 3:
                        case 6:
                        case 9:
                            mDrawable = ResourcesCompat
                                    .getDrawable(context.getResources(), R.drawable.grid_border_f, null);
                            break;
                        default:
                            mDrawable = ResourcesCompat
                                    .getDrawable(context.getResources(), R.drawable.grid_border_c, null);
                            break;
                    }
                } else {
                    switch (row) {
                        case 3:
                        case 6:
                        case 9:
                            mDrawable = ResourcesCompat
                                    .getDrawable(context.getResources(), R.drawable.grid_border_e, null);
                            break;
                        default:
                            mDrawable = ResourcesCompat
                                    .getDrawable(context.getResources(), R.drawable.grid_border_b, null);
                            break;
                    }
                }

                if (col == 4 || col == 5 || col == 6) {
                    switch (row) {
                        case 4:
                        case 5:
                        case 6:
                            int mColor = ContextCompat.getColor(activity, R.color.colorLightGray);
                            mDrawable.setColorFilter(
                                    new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
                            break;
                    }
                } else {
                    switch (row) {
                        case 1:
                        case 2:
                        case 3:
                        case 7:
                        case 8:
                        case 9:
                            int mColor = ContextCompat.getColor(activity, R.color.colorLightGray);
                            mDrawable.setColorFilter(
                                    new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
                            break;
                    }
                }
                TextView textView = new TextView(activity);
                String idString = "major_" + row + col;
                int id = activity.getResources()
                        .getIdentifier(idString, "id", activity.getPackageName());
                textView.setId(id);
                textView.setLayoutParams(new ViewGroup.LayoutParams(squareSize, squareSize));
                textView.setBackground(mDrawable);
                textView.setText(col + "");
                textView.setGravity(Gravity.CENTER);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextSize(30);
                gridLayout.addView(textView);
            }
        }

    }
}
