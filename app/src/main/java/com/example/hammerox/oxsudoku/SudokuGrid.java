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

import java.util.ArrayList;
import java.util.List;


public class SudokuGrid {

    private List<Integer> solution;

    public SudokuGrid() {
        solution = createSolution();
    }

    public void createGrid(Activity activity, View rootView, int squareDim) {

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
                textView.setLayoutParams(new ViewGroup.LayoutParams(squareDim, squareDim));
                textView.setBackground(mDrawable);
                textView.setGravity(Gravity.CENTER);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextSize(30);

                int index = 9 * (row - 1) + col - 1;
                textView.setText(solution.get(index).toString());

                gridLayout.addView(textView);
            }
        }

    }

    public List<Integer> createSolution() {
        /*Todo - Create a sudoku puzzle generator*/
        int[] ints =   {6,2,7,3,8,9,4,1,5,
                        8,9,4,1,7,5,3,6,2,
                        1,3,5,6,4,2,7,8,9,
                        9,8,2,7,6,3,1,5,4,
                        4,5,3,2,9,1,8,7,6,
                        7,1,6,4,5,8,2,9,3,
                        3,7,1,9,2,6,5,4,8,
                        2,6,8,5,1,4,9,3,7,
                        5,4,9,8,3,7,6,2,1};
        List<Integer> solution = new ArrayList<Integer>();
        for (int index = 0; index < ints.length; index++)
        {
            solution.add(ints[index]);
        }

        return solution;
    }
}
