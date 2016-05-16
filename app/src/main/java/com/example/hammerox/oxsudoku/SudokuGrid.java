package com.example.hammerox.oxsudoku;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntegerRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hammerox.oxsudoku.Tools.SquareLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SudokuGrid {

    int lastInput = -1;

    private List<Integer> puzzleSolution;
    private List<Boolean> puzzleMask;
    private List<Integer> puzzleUserAnswers;
    private List<Boolean> puzzleCorrectAnswers;
    private List<Boolean> puzzleUserInput;
    private List<Integer> puzzleHighlight;

    private List<Boolean> isNumberComplete;

    /*Todo - Add input's history*/

    public SudokuGrid() {
        /* The puzzle is generated on class creation.
        * puzzleSolution = Integer. Contains all correct answers.
        * puzzleMask = Boolean. Show respective solution if true. False will demand user input.
        * puzzleCorrectAnswers = Boolean. Is true if respective user's input matches solution.
        * puzzleUserInput = Boolean. Is true if contains user value.
        * puzzleHightlight = Integer. 0 shows no highlight, 1 shows partial and 2 shows full highlight.
        * */
        SudokuGenerator puzzle = new SudokuGenerator();
        puzzleSolution = puzzle.board;
        puzzleMask = puzzle.mask;
        /*puzzleSolution = createSolution();
        puzzleMask = createPuzzleMask();*/
        puzzleUserAnswers = createPuzzleUserAnswers();
        puzzleCorrectAnswers = createPuzzleCorrectAnswers();
        puzzleUserInput = createPuzzleUserInput();
        puzzleHighlight = createPuzzleHighlight();

        isNumberComplete = createNumberCheckList();
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
                                TextView clickedText = (TextView) v;
                                int indexOfClick = getIndexFromView(activity, clickedText);
                                int[] position = getPositionFromIndex(indexOfClick);
                                int clickedRow = position[0];
                                int clickedCol = position[1];
                                List<Integer> rowColBox = getRowColBoxIndexes(clickedRow, clickedCol);

                                // Checking if user input is valid.
                                Boolean isValid = true;
                                List<Integer> invalidIndex = new ArrayList<Integer>();
                                for (Integer i : rowColBox) {
                                    if (i != indexOfClick) {
                                        int answer = puzzleUserAnswers.get(i);
                                        if (answer == activeKey) {
                                            isValid = false;
                                            invalidIndex.add(i);
                                        }
                                    }
                                }

                                // If user input is valid, commit changes.
                                // If user input is not valid, warn and show conflicts.
                                if (isValid) {
                                    commitChanges(activity, clickedText);
                                } else {
                                    Toast toast = Toast.makeText(activity, "You can't break the rules", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
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

    public void commitChanges(Activity activity, TextView view) {
        int size = 9 * 9;
        int activeKey = SudokuKeyboard.getActiveKey();
        int indexOfClick = getIndexFromView(activity, view);
        int[] position = getPositionFromIndex(indexOfClick);
        int clickedRow = position[0];
        int clickedCol = position[1];
        List<Integer> rowColBox = getRowColBoxIndexes(clickedRow, clickedCol);

        // Checking if there was a number before new input.
        int oldNumber = 0;
        Boolean isSubtituting = false;
        try {
            oldNumber = Integer.valueOf(view.getText().toString());
            if (oldNumber > 0) {
                isSubtituting = true;
            }
        } catch (NumberFormatException e) {}

        // Updating lastInput.
        if (lastInput >= 0) {
            int id = getIdFromIndex(activity, lastInput);
            TextView lastInputCell = (TextView) activity.findViewById(id);
            int mColorOld = ContextCompat.getColor(activity, R.color.colorAccent);
            lastInputCell.setTextColor(mColorOld);
        }
        lastInput = indexOfClick;

        // Setting new value.
        view.setText(String.valueOf(activeKey));
        view.setTextColor(Color.BLUE);

        // Updating puzzleUserAnswers.
        puzzleUserAnswers.set(indexOfClick, activeKey);

        // Updating puzzleUserInput.
        puzzleUserInput.set(indexOfClick, true);

        // Updating puzzleCorrectAnswers.
        int solution = puzzleSolution.get(indexOfClick);
        if (solution == activeKey) {
            puzzleCorrectAnswers.set(indexOfClick, true);
        } else {
            puzzleCorrectAnswers.set(indexOfClick, false);
        }

        // Updating puzzleHighlight Level 2
        setColorFilter(activity, clickedRow, clickedCol, view.getBackground(), 2);
        puzzleHighlight.set(indexOfClick, 2);

        // Updating puzzleHighlight Level 1
        for (Integer i : rowColBox) {
            if (puzzleHighlight.get(i) == 0) {
                setIntensityColor(activity, i, 1);
                puzzleHighlight.set(i, 1);
            }
        }

        // Updating isNumberComplete.
        int numberCounter = 0;
        for (int i = 0; i < size; i++) {
            if (puzzleUserAnswers.get(i) == activeKey) numberCounter++;
            if (numberCounter == 9) {
                int listsIndex = activeKey - 1;
                isNumberComplete.set(listsIndex, true);
            }
        }
        if (isSubtituting) {
            int listsIndex = oldNumber - 1;
            isNumberComplete.set(listsIndex, false);
            SudokuKeyboard.showButton(activity, oldNumber);
        }


        // Checking if puzzle is complete.
        int count = 0;
        for (Boolean answer : puzzleCorrectAnswers) {
            if (answer) count++;
        }
        float correctAnswersRatio
                = (float) count / (float) puzzleCorrectAnswers.size();
        if (correctAnswersRatio == 1) {
            Toast toast = Toast.makeText(activity, "CONGRATULATIONS!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Toast toast2 = Toast.makeText(activity, "You completed the puzzle!", Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }
    }

    public List<Integer> createSolution() {
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

    public List<Integer> createPuzzleUserAnswers() {
        List<Integer> userAnswers = new ArrayList<>();
        int size = 9 * 9;
        for (int i = 0; i < size; i++) {
            if (puzzleMask.get(i)) {
                userAnswers.add(puzzleSolution.get(i));
            } else {
                userAnswers.add(0);
            }
        }
        return userAnswers;
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
                puzzleHighlight.set(i, 0);
            }
        }
    }

    public void setPuzzleHighlight(Activity activity, int activeKey) {
        int size = 9*9;
        clearPuzzleHighlight(activity);

        // Setting highlight level 2 to activeKey's numbers .
        List<Integer> highlightList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int highlightLevel = 2;
            int solution = puzzleSolution.get(i);
            if (solution == activeKey) {
                Boolean needsHighlight = puzzleMask.get(i) || puzzleUserInput.get(i);
                if (needsHighlight) {
                    puzzleHighlight.set(i, highlightLevel);
                    highlightList.add(i);
                }
            }
        }

        for (Integer index : highlightList) {
            // Setting highlight level 1 to all rows and columns containing level 2.
            int[] position = getPositionFromIndex(index);
            int row = position[0];
            int col = position[1];
            setHighlightLevel1(row, col);
        }

        // Changing ColorFilter on highlights with level 1 or 2.
        for (int i = 0; i < 81; i++) {
            int intensity = puzzleHighlight.get(i);
            if (intensity != 0) {
                setIntensityColor(activity, i, intensity);
            }
        }
    }

    public void setHighlightLevel1(int row, int col) {
        // Box
        int[] boxIndexes = getBoxIndexes(row, col);
        for (int i : boxIndexes) {
            int intensity = puzzleHighlight.get(i);
            if (intensity == 0) puzzleHighlight.set(i, 1);
        }
        // Row
        for (int r = 1; r <= 9; r++) {
            if (r != row) {
                int i = getIndexFromPosition(r, col);
                int intensity = puzzleHighlight.get(i);
                if (intensity == 0) puzzleHighlight.set(i, 1);
            }
        }
        // Column
        for (int c = 1; c <= 9; c++) {
            if (c != col) {
                int i = getIndexFromPosition(row, c);
                int intensity = puzzleHighlight.get(i);
                if (intensity == 0) puzzleHighlight.set(i, 1);
            }
        }
    }

    public List<Boolean> createNumberCheckList() {
        Boolean[] array = new Boolean[9];
        Arrays.fill(array, false);
        return Arrays.asList(array);
    }

    public List<Integer> getRowColBoxIndexes(int row, int col) {
        List<Integer> indexes = new ArrayList<>();
        // Box
        int[] boxIndexes = getBoxIndexes(row, col);
        for (int i : boxIndexes) {
            indexes.add(i);
        }
        // Row
        for (int r = 1; r <= 9; r++) {
            if (r != row) {
                int i = getIndexFromPosition(r, col);
                indexes.add(i);
            }
        }
        // Column
        for (int c = 1; c <= 9; c++) {
            if (c != col) {
                int i = getIndexFromPosition(row, c);
                indexes.add(i);
            }
        }
        return indexes;
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
                        mColor = Color.WHITE;
                    } else if (highlightLevel == 1) {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlightWhite);
                    } else {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlightStrong);
                    }
                    mDrawable.setColorFilter(
                            new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
                    break;
                default:
                    if (highlightLevel == 0) {
                        mColor = Color.WHITE;
                    } else if (highlightLevel == 1) {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlightWhite);
                    } else {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlightStrong);
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
                        mColor = Color.WHITE;
                    } else if (highlightLevel == 1) {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlightWhite);
                    } else {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlightStrong);
                    }
                    mDrawable.setColorFilter(
                            new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
                    break;
                default:
                    if (highlightLevel == 0) {
                        mColor = Color.WHITE;
                    } else if (highlightLevel == 1) {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlightWhite);
                    } else {
                        mColor = ContextCompat.getColor(activity, R.color.colorHighlightStrong);
                    }
                    mDrawable.setColorFilter(
                            new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
                    break;
            }
        }
    }

    public int[] getBoxIndexes(int row, int col) {
        int[] boxIndexes = new int[] {};
        switch (row) {
            case 1:
            case 2:
            case 3:
                if (col == 1 || col == 2 || col == 3) {
                    boxIndexes = new int[]{0, 1, 2, 9, 10, 11, 18, 19, 20};

                } else if (col == 4 || col == 5 || col == 6) {
                    boxIndexes = new int[]{3, 4, 5, 12, 13, 14, 21, 22, 23};

                } else if (col == 7 || col == 8 || col == 9) {
                    boxIndexes = new int[]{6, 7, 8, 15, 16, 17, 24, 25, 26};

                }
                break;
            case 4:
            case 5:
            case 6:
                if (col == 1 || col == 2 || col == 3) {
                    boxIndexes = new int[]{27,28,29,36,37,38,45,46,47};

                } else if (col == 4 || col == 5 || col == 6) {
                    boxIndexes = new int[]{30,31,32,39,40,41,48,49,50};

                } else if (col == 7 || col == 8 || col == 9) {
                    boxIndexes = new int[]{33,34,35,42,43,44,51,52,53};

                }
                break;
            case 7:
            case 8:
            case 9:
                if (col == 1 || col == 2 || col == 3) {
                    boxIndexes = new int[]{54,55,56,63,64,65,72,73,74};

                } else if (col == 4 || col == 5 || col == 6) {
                    boxIndexes = new int[]{57,58,59,66,67,68,75,76,77};

                } else if (col == 7 || col == 8 || col == 9) {
                    boxIndexes = new int[]{60,61,62,69,70,71,78,79,80};

                }
                break;
        }
        return boxIndexes;
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

    public int getIndexFromPosition(int row, int col) {
        return 9 * (row - 1) + col - 1;
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

    public List<Boolean> getIsNumberComplete() {
        return isNumberComplete;
    }

    public void setIsNumberComplete(List<Boolean> isNumberComplete) {
        this.isNumberComplete = isNumberComplete;
    }
}
