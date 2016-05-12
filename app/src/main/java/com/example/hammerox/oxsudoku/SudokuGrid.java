package com.example.hammerox.oxsudoku;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hammerox.oxsudoku.Tools.SquareLayout;

import java.util.ArrayList;
import java.util.List;


public class SudokuGrid {

    private List<Integer> puzzleSolution;
    private List<Boolean> puzzleMask;
    private List<Boolean> puzzleCorrectAnswers;
    private List<Boolean> puzzleUserInput;
    private List<Integer> puzzleHighlight;


    public SudokuGrid() {
        /* The puzzle is generated on class creation.
        * puzzleSolution = Integer. Contains all correct answers.
        * puzzleMask = Boolean. Show respective solution if true. False will demand user input.
        * puzzleCorrectAnswers = Boolean. Is true if respective user's input matches solution.
        * puzzleUserInput = Boolean. Is true if contains user value.
        * puzzleHightlight = Integer. 0 shows no highlight, 1 shows partial and 2 shows full highlight.
        * */
        puzzleSolution = createSolution();
        puzzleMask = createPuzzleMask();
        puzzleCorrectAnswers = createPuzzleCorrectAnswers();
        puzzleUserInput = createPuzzleUserInput();
        puzzleHighlight = createPuzzleHighlight();
    }

    public void drawPuzzle(final Activity activity, final View rootView) {

        SquareLayout gridLayout = (SquareLayout) rootView.findViewById(R.id.sudoku_gridlayout);
        for (int row = 1; row <= 9; row++) {
            // Creating a LinearLayout for each row
            LinearLayout linearLayout = new LinearLayout(activity);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f));

            for (int col = 1; col <= 9; col++) {
                // Creating a TextView for each square on the grid.
                TextView textView = new TextView(activity);
                    // ID
                String idString = "major_" + row + col;
                int id = activity.getResources()
                        .getIdentifier(idString, "id", activity.getPackageName());
                textView.setId(id);
                    // LayoutParams
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f));
                    // Appearance
                Drawable mDrawable = getGridDrawable(activity, row, col);
                setColorFilter(activity, row, col, mDrawable, 0);
                textView.setBackground(mDrawable);
                textView.setGravity(Gravity.CENTER);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
                textView.setTextSize(30);

                int index = 9 * (row - 1) + col - 1;
                // If puzzleMask is true, show number. Otherwise, demand user input;
                if (puzzleMask.get(index)) {
                    textView.setText(String.valueOf(puzzleSolution.get(index)));
                } else {
                    // User's TextView should be clickable and have a different color.
                    int mColor = ContextCompat.getColor(activity, R.color.colorAccent);
                    textView.setTextColor(mColor);
                    textView.isClickable();
                        // Interaction
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int activeKey = SudokuKeyboard.getActiveKey();
                            if (activeKey != 0) {       // A key from keyboard must be selected.
                                // Setting new value.
                                TextView clickedText = (TextView) v;
                                clickedText.setText(String.valueOf(activeKey));

                                // Updating puzzleUserInput.
                                int indexOfClick = getIndexFromView(activity, clickedText);
                                puzzleUserInput.set(indexOfClick, true);

                                // Updating puzzleCorrectAnswers.
                                int solution = puzzleSolution.get(indexOfClick);
                                if (solution == activeKey) {
                                    puzzleCorrectAnswers.set(indexOfClick, true);
                                } else {
                                    puzzleCorrectAnswers.set(indexOfClick, false);
                                }

                                // Updating puzzleHighlight
                                int[] position = getPositionFromIndex(indexOfClick);
                                setColorFilter(activity, position[0], position[1],
                                        clickedText.getBackground(), 2);
                                puzzleHighlight.set(indexOfClick, 2);

                                // Checking if puzzle is complete.
                                int count = 0;
                                for (Boolean answer : puzzleCorrectAnswers) {
                                    if (answer) count++;
                                }
                                float correctAnswersRatio
                                        = (float) count / (float) puzzleCorrectAnswers.size();
                                if (correctAnswersRatio == 1) {
                                    Toast.makeText(activity, "CONGRATS!", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
                linearLayout.addView(textView);
            }
            gridLayout.addView(linearLayout);
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

    public List<Boolean> createPuzzleCorrectAnswers() {
        List<Boolean> list = new ArrayList<>();
        for (Boolean bol : puzzleMask) {
            list.add(bol);
        }
        return list;
    }

    public List<Boolean> createPuzzleUserInput() {
        List<Boolean> userInput = new ArrayList<>();
        int size = 9*9;
        for (int i = 0; i < size; i++) {
            userInput.add(false);
        }
        return userInput;
    }

    public List<Integer> createPuzzleHighlight() {
        List<Integer> list = new ArrayList<>();
        int size = 9*9;
        for (int i = 0; i < size; i++) {
            list.add(0);
        }
        return list;
    }

    public void clearPuzzleHighlight(Activity activity) {
        // This makes puzzleHighlight as default.
        int size = 9*9;
        for (int i = 0; i < size; i++) {
            int intensity = puzzleHighlight.get(i);
            if (intensity != 0) {
                setIntensityColor(activity, i, 0);
            }
            if (puzzleHighlight.get(i) != 0) {
                puzzleHighlight.set(i, 0);
            }
        }
    }

    public void setPuzzleHighlight(Activity activity, int activeKey) {
        int size = 9*9;
        clearPuzzleHighlight(activity);

        for (int i = 0; i < size; i++) {
            int highlightLevel = 2;
            int solution = puzzleSolution.get(i);
            if (solution == activeKey) {
                Boolean needsHighlight = puzzleMask.get(i) || puzzleUserInput.get(i);
                if (needsHighlight) {
                    puzzleHighlight.set(i, highlightLevel);
                    setIntensityColor(activity, i, highlightLevel);
                }
            }
        }
    }

    public void setIntensityColor(Activity activity, int index, int highlightLevel) {
        int[] position = getPositionFromIndex(index);
        int id = getIdFromIndex(activity, index);
        Drawable mDrawable = activity.findViewById(id).getBackground();
        setColorFilter(activity, position[0], position[1], mDrawable, highlightLevel);
    }

    public Drawable getGridDrawable(Activity activity, int row, int col) {
        /*  Different drawables are used according to its grid position, as shown on the diagram:
        *       1 2 3 4 5 6 7 8 9
        *       -----------------
        *   1 | a b c b b c b b b
        *   2 | a b c b b c b b b
        *   3 | d e f e e f e e e
        *   4 | a b c b b c b b b
        *   5 | a b c b b c b b b
        *   6 | d e f e e f e e e
        *   7 | a b c b b c b b b
        *   8 | a b c b b c b b b
        *   9 | d e f e e f e e e  */
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
        return mDrawable;
    }

    public void setColorFilter(Activity activity, int row, int col,
                               Drawable mDrawable, int highlightLevel) {
        /*  Grid uses different colors, with w = white and g = gray.
        *       1 2 3 4 5 6 7 8 9
        *       -----------------
        *   1 | g g g w w w g g g
        *   2 | g g g w w w g g g
        *   3 | g g g w w w g g g
        *   4 | w w w g g g w w w
        *   5 | w w w g g g w w w
        *   6 | w w w g g g w w w
        *   7 | g g g w w w g g g
        *   8 | g g g w w w g g g
        *   9 | g g g w w w g g g  */
        int mColor;
        if (col == 4 || col == 5 || col == 6) {
            switch (row) {
                case 4:
                case 5:
                case 6:
                    if (highlightLevel == 0) {
                        mColor = ContextCompat.getColor(activity, R.color.colorLightGray);
                    } else {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlight);
                    }
                    mDrawable.setColorFilter(
                            new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
                    break;
                default:
                    if (highlightLevel == 0) {
                        mColor = Color.WHITE;
                    } else {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlight);
                    }
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
                    if (highlightLevel == 0) {
                        mColor = ContextCompat.getColor(activity, R.color.colorLightGray);
                    } else {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlight);
                    }
                    mDrawable.setColorFilter(
                            new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
                    break;
                default:
                    if (highlightLevel == 0) {
                        mColor = Color.WHITE;
                    } else {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlight);
                    }
                    mDrawable.setColorFilter(
                            new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
                    break;
            }
        }
    }

    public int getIndexFromView(Activity activity, TextView view) {
        // Gets a grid's TextView and returns its index.
        int viewId = view.getId();
        String viewIdName = activity.getResources()
                .getResourceEntryName(viewId);
        String viewRowCol = viewIdName.split("_")[1];
        int rowIndex = Integer.valueOf(viewRowCol.substring(0,1));
        int colIndex = Integer.valueOf(viewRowCol.substring(1,2));
        return 9 * (rowIndex - 1) + colIndex - 1;
    }

    public int[] getPositionFromIndex(int index) {
        int row = index / 9 + 1;
        int col = index % 9 + 1;
        return new int[] {row,col};
    }

    public int getIdFromIndex(Activity activity, int index) {
        int[] position = getPositionFromIndex(index);
        int row = position[0];
        int col = position[1];
        String idString = "major_" + row + col;
        return activity.getResources()
                .getIdentifier(idString, "id", activity.getPackageName());
    }

    // Getters and Setters

    public List<Boolean> getPuzzleCorrectAnswers() {
        return puzzleCorrectAnswers;
    }

    public void setPuzzleCorrectAnswers(List<Boolean> puzzleCorrectAnswers) {
        this.puzzleCorrectAnswers = puzzleCorrectAnswers;
    }

    public List<Boolean> getPuzzleMask() {
        return puzzleMask;
    }

    public void setPuzzleMask(List<Boolean> puzzleMask) {
        this.puzzleMask = puzzleMask;
    }

    public List<Integer> getPuzzleSolution() {
        return puzzleSolution;
    }

    public void setPuzzleSolution(List<Integer> puzzleSolution) {
        this.puzzleSolution = puzzleSolution;
    }

    public List<Boolean> getPuzzleUserInput() {
        return puzzleUserInput;
    }

    public void setPuzzleUserInput(List<Boolean> puzzleUserInput) {
        this.puzzleUserInput = puzzleUserInput;
    }
}
