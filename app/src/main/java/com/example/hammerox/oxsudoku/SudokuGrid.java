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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SudokuGrid {

    private List<Integer> puzzleSolution;
    private List<Boolean> puzzleMask;
    private List<Boolean> puzzleCorrectAnswers;


    public SudokuGrid() {
        puzzleSolution = createSolution();
    }

    public void createGrid(final Activity activity, final View rootView, int squareDim) {

        GridLayout gridLayout = (GridLayout) rootView.findViewById(R.id.sudoku_gridlayout);
        for (int row = 1; row <= 9; row++) {
            for (int col = 1; col <= 9; col++) {
                Drawable mDrawable = drawGrid(activity, row, col);

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

                puzzleMask = createPuzzleMask();
                puzzleCorrectAnswers = createPuzzleCorrectAnswers(puzzleMask);

                int index = 9 * (row - 1) + col - 1;
                if (puzzleMask.get(index)) {
                    textView.setText(String.valueOf(puzzleSolution.get(index)));
                } else {
                    int mColor = ContextCompat.getColor(activity, R.color.colorAccent);
                    textView.setTextColor(mColor);
                    textView.isClickable();
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int activeKey = SudokuKeyboard.getActiveKey();
                            if (activeKey != 0) {
                                TextView clickedText = (TextView) v;
                                clickedText.setText(String.valueOf(activeKey));

                                GridLayout parent = (GridLayout) clickedText.getParent();
                                int indexOfClick = parent.indexOfChild(clickedText);
                                int solution = puzzleSolution.get(indexOfClick);
                                if (solution == activeKey) {
                                    puzzleCorrectAnswers.set(indexOfClick, true);
                                } else {
                                    puzzleCorrectAnswers.set(indexOfClick, false);
                                }
                                int count = 0;
                                for (Boolean answer : puzzleCorrectAnswers) {
                                    if (answer) {count = count + 1;}
                                }
                                float correctAnswersRatio = (float)count / (float)puzzleCorrectAnswers.size();
                                if (correctAnswersRatio == 1) {
                                    Toast.makeText(activity, "CONGRATS!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
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
        for (int anInt : ints) {
            solution.add(anInt);
        }
        return solution;
    }

    public List<Boolean> createPuzzleMask() {
        boolean[] bols =   {false,false,true,   true,false,true,    true,false,false,
                            true,false,false,   true,false,true,    false,false,true,
                            false,false,false,  false,true,false,   false,false,false,

                            false,true,false,   false,true,false,   false,true,false,
                            true,false,true,    true,false,true,    true,false,true,
                            false,true,false,   false,true,false,   false,true,false,

                            false,false,false,  false,true,false,   false,false,false,
                            true,false,false,   true,false,true,    false,false,true,
                            false,false,true,   true,false,true,    true,false,false};
        List<Boolean> mask = new ArrayList<>();
        for (boolean bol : bols) {
            mask.add(bol);
        }
        return mask;
    }

    public List<Boolean> createPuzzleCorrectAnswers(List<Boolean> mask) {
        return mask;
    }

    public Drawable drawGrid(Activity activity, int row, int col) {
        Context context = activity.getApplicationContext();
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

        return mDrawable;
    }
}
