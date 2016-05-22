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
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hammerox.oxsudoku.Tools.SquareLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SudokuGrid {

    final static int GRID_SIZE = 81;

    private int lastInput = -1;

    private List<Integer> puzzleSolution;
    private List<Boolean> hasSolution;
    private List<Integer> puzzleAnswers;
    private List<Boolean> isAnswerCorrect;
    private List<Boolean> hasUserInput;
    private List<Integer> puzzleHighlight;

    private List<Boolean> isNumberComplete;

    /*Todo - Add input's history*/

    public SudokuGrid() {
        /* The puzzle is generated on class creation.
        * puzzleSolution = Integer. Contains all correct answers.
        * hasSolution = Boolean. Show respective solution if true. False will demand user input.
        * isAnswerCorrect = Boolean. Is true if respective user's input matches solution.
        * hasUserInput = Boolean. Is true if contains user value.
        * puzzleHightlight = Integer. 0 shows no highlight, 1 shows partial and 2 shows full highlight.
        * */

        puzzleSolution = createDebugSolution();
        hasSolution = createDebugMask();
        /*SudokuGenerator puzzle = new SudokuGenerator();
        puzzleSolution = puzzle.board;
        hasSolution = puzzle.mask;*/
        puzzleAnswers = createPuzzleAnswers();
        isAnswerCorrect = createIsAnswerCorrect();
        hasUserInput = createPuzzleUserInput();
        puzzleHighlight = createPuzzleHighlight();

        isNumberComplete = createNumberCheckList();
    }


    public void drawPuzzle(final Activity activity, final View rootView) {

        SquareLayout gridLayout = (SquareLayout) rootView.findViewById(R.id.sudoku_gridlayout);
        for (int row = 1; row <= 9; row++) {
            // Creating a LinearLayout for each row
            LinearLayout cellLayout = new LinearLayout(activity);
            cellLayout.setOrientation(LinearLayout.HORIZONTAL);
            cellLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f));

            for (int col = 1; col <= 9; col++) {
                // Creating a TextView for each square on the grid.
                TextView answerView = new TextView(activity);
                    // ID
                String idString = "major_" + row + col;
                int id = activity.getResources()
                        .getIdentifier(idString, "id", activity.getPackageName());
                answerView.setId(id);
                    // LayoutParams
                answerView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f));
                    // Appearance
                Drawable mDrawable = getGridDrawable(activity, row, col);
                setColorFilter(activity, mDrawable, 0);
                answerView.setBackground(mDrawable);
                answerView.setGravity(Gravity.CENTER);
                answerView.setTypeface(Typeface.DEFAULT_BOLD);
                answerView.setTextSize(30);

                int index = 9 * (row - 1) + col - 1;
                // If hasSolution is true, show number. Otherwise, demand user input;
                if (hasSolution.get(index)) {
                    answerView.setText(String.valueOf(puzzleSolution.get(index)));
                } else {
                    // User's TextView should be clickable and have a different color.
                    int mColor = ContextCompat.getColor(activity, R.color.colorAccent);
                    answerView.setTextColor(mColor);
                    answerView.isClickable();
                        // Interaction
                    setCellClickListener(activity, answerView);
                    setCellTouchListener(activity, answerView);
                }
                cellLayout.addView(answerView);
            }
            gridLayout.addView(cellLayout);
        }

    }


    public void setCellTouchListener(final Activity activity, final TextView cell) {
        cell.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setColorFilter(activity, v.getBackground(), 4);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    int i = GridPosition.getIndexFromView(activity, cell);
                    int intensity = puzzleHighlight.get(i);
                    setColorFilter(activity, v.getBackground(), intensity);
                }
                return false;
            }
        });
    }


    public void setCellClickListener(final Activity activity, TextView cell) {
        cell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int activeKey = SudokuKeyboard.getActiveKey();
                if (activeKey != 0) {       // A key from keyboard must be selected.
                    TextView clickedText = (TextView) v;
                    int indexOfClick =
                            GridPosition.getIndexFromView(activity, clickedText);
                    int[] position = GridPosition.getPositionFromIndex(indexOfClick);
                    int clickedRow = position[0];
                    int clickedCol = position[1];
                    List<Integer> rowColBox =
                            GridPosition.getRowColBoxIndexes(clickedRow, clickedCol, false);

                    // Checking if user input is valid.
                    Boolean isValid = true;
                    for (Integer i : rowColBox) {
                        int answer = puzzleAnswers.get(i);
                        if (answer == activeKey) {
                            isValid = false;
                            break;
                        }
                    }

                    // If user input is valid, commit changes.
                    // If user input is not valid, warn and show conflicts.
                    updatePuzzleHighlight(activity, activeKey);
                    if (isValid) {
                        commitChanges(activity, clickedText);
                    } else {
                        showConflicts(activity, clickedRow, clickedCol, activeKey);
                    }
                }
            }
        });
    }


    public void commitChanges(Activity activity, TextView view) {
        int activeKey = SudokuKeyboard.getActiveKey();
        int indexOfClick = GridPosition.getIndexFromView(activity, view);
        int[] position = GridPosition.getPositionFromIndex(indexOfClick);
        int clickedRow = position[0];
        int clickedCol = position[1];
        List<Integer> rowColBox = GridPosition.getRowColBoxIndexes(clickedRow, clickedCol, false);

        // Checking if there was a number before new input.
        int oldNumber = 0;
        Boolean isSubtituting = false;

        String oldNumberString = view.getText().toString();
        if (oldNumberString.length() > 0) {
            oldNumber = Integer.valueOf(oldNumberString);
            isSubtituting = true;
        }

        // Updating lastInput.
        if (lastInput >= 0) {
            int id = GridPosition.getIdFromIndex(activity, lastInput);
            TextView lastInputCell = (TextView) activity.findViewById(id);
            int mColorOld = ContextCompat.getColor(activity, R.color.colorAccent);
            lastInputCell.setTextColor(mColorOld);
        }
        lastInput = indexOfClick;

        // Setting new value.
        view.setText(String.valueOf(activeKey));
        view.setTextColor(Color.BLUE);

        // Updating puzzleAnswers.
        puzzleAnswers.set(indexOfClick, activeKey);

        // Updating hasUserInput.
        hasUserInput.set(indexOfClick, true);

        // Updating isAnswerCorrect.
        int solution = puzzleSolution.get(indexOfClick);
        if (solution == activeKey) {
            isAnswerCorrect.set(indexOfClick, true);
        } else {
            isAnswerCorrect.set(indexOfClick, false);
        }

        // Updating puzzleHighlight Level 2
        setColorFilter(activity, view.getBackground(), 2);
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
        for (int i = 0; i < GRID_SIZE; i++) {
            if (puzzleAnswers.get(i) == activeKey) numberCounter++;
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
        for (Boolean answer : isAnswerCorrect) {
            if (answer) count++;
        }
        float correctAnswersRatio
                = (float) count / (float) isAnswerCorrect.size();
        if (correctAnswersRatio == 1) {
            Toast toast = Toast.makeText(activity, "CONGRATULATIONS!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Toast toast2 = Toast.makeText(activity, "You completed the puzzle!", Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }
    }


    public void showConflicts(Activity activity, int clickedRow, int clickedCol, int activeKey) {
        Boolean isOnBox = false;
        Boolean isOnRow = false;
        Boolean isOnCol = false;
        List<Integer> conflictIndexes = new ArrayList<>();

        // Check Box
        List<Integer> checkBox = GridPosition.getBoxIndexes(clickedRow, clickedCol, false);
        for (Integer i : checkBox) {
            if (puzzleAnswers.get(i) == activeKey) {
                isOnBox = true;
                conflictIndexes.add(i);
                break;
            }
        }

        // Check Row
        List<Integer> checkRow = GridPosition.getRowIndexes(clickedRow, clickedCol, false);
        for (Integer i : checkRow) {
            if (puzzleAnswers.get(i) == activeKey) {
                isOnRow = true;
                conflictIndexes.add(i);
                break;
            }
        }

        // Check Col
        List<Integer> checkCol = GridPosition.getColIndexes(clickedRow, clickedCol, false);
        for (Integer i : checkCol) {
            if (puzzleAnswers.get(i) == activeKey) {
                isOnCol = true;
                conflictIndexes.add(i);
                break;
            }
        }

        // Draw
        if (isOnBox) {
            for (Integer i : checkBox) {
                int id = GridPosition.getIdFromIndex(activity, i);
                Drawable cell = activity.findViewById(id).getBackground();
                setColorFilter(activity, cell, 3);
            }

        }

        if (isOnRow) {
            for (Integer i : checkRow) {
                int id = GridPosition.getIdFromIndex(activity, i);
                Drawable cell = activity.findViewById(id).getBackground();
                setColorFilter(activity, cell, 3);
            }

        }

        if (isOnCol) {
            for (Integer i : checkCol) {
                int id = GridPosition.getIdFromIndex(activity, i);
                Drawable cell = activity.findViewById(id).getBackground();
                setColorFilter(activity, cell, 3);
            }
        }

        // Mark conflicting cells.
        for (Integer i : conflictIndexes) {
            int id = GridPosition.getIdFromIndex(activity, i);
            Drawable cell = activity.findViewById(id).getBackground();
            setColorFilter(activity, cell, 4);
        }

    }


    public void clearPuzzleHighlight(Activity activity) {
        // This makes puzzleHighlight as default.
        for (int i = 0; i < GRID_SIZE; i++) {
            setIntensityColor(activity, i, 0);
            puzzleHighlight.set(i, 0);
        }
    }


    public void updatePuzzleHighlight(Activity activity, int activeKey) {
        clearPuzzleHighlight(activity);

        // Setting highlight level 2 to activeKey's numbers .
        List<Integer> highlightList = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            int highlightLevel = 2;
            int numberOnCell = puzzleAnswers.get(i);
            if (numberOnCell == activeKey) {
                puzzleHighlight.set(i, highlightLevel);
                highlightList.add(i);
            }
        }

        for (Integer index : highlightList) {
            // Setting highlight level 1 to all rows and columns containing level 2.
            int[] position = GridPosition.getPositionFromIndex(index);
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
        List<Integer> allIndexes = GridPosition.getRowColBoxIndexes(row, col, false);
        for (Integer i : allIndexes) {
            int intensity = puzzleHighlight.get(i);
            if (intensity == 0) puzzleHighlight.set(i, 1);
        }
    }


    public void setIntensityColor(Activity activity, int index, int highlightLevel) {
        int id = GridPosition.getIdFromIndex(activity, index);
        Drawable mDrawable = activity.findViewById(id).getBackground();
        setColorFilter(activity, mDrawable, highlightLevel);
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


    public void setColorFilter(Activity activity, Drawable mDrawable, int highlightLevel) {
        /* Highlight level 0 = Background.
        *  Highlight level 1 = Check area.
        *  Highlight level 2 = Active number.
        *  Highlight level 3 = Conflict area.
        *  Highlight level 4 = Conflicting cell.
        *  */
        int mColor;
        if (highlightLevel == 0) {
            mColor = Color.WHITE;
        } else if (highlightLevel == 1) {
            mColor = ContextCompat.getColor(activity, R.color.colorHighlightWhite);
        } else if (highlightLevel == 2) {
            mColor = ContextCompat.getColor(activity, R.color.colorHighlightStrong);
        } else if (highlightLevel == 3) {
            mColor = ContextCompat.getColor(activity, R.color.colorWarnArea);
        } else {
            mColor = ContextCompat.getColor(activity, R.color.colorWarn);
        }
        mDrawable.setColorFilter(
                new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
    }


    //////////  LIST CREATERS //////////

    public List<Integer> createPuzzleAnswers() {
        List<Integer> userAnswers = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            if (hasSolution.get(i)) {
                userAnswers.add(puzzleSolution.get(i));
            } else {
                userAnswers.add(0);
            }
        }
        return userAnswers;
    }


    public List<Boolean> createIsAnswerCorrect() {
        List<Boolean> list = new ArrayList<>();
        for (Boolean bol : hasSolution) {
            list.add(bol);
        }
        return list;
    }


    public List<Boolean> createPuzzleUserInput() {
        List<Boolean> userInput = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            userInput.add(false);
        }
        return userInput;
    }


    public List<Integer> createPuzzleHighlight() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            list.add(0);
        }
        return list;
    }


    public List<Boolean> createNumberCheckList() {
        Boolean[] array = new Boolean[9];
        Arrays.fill(array, false);
        return Arrays.asList(array);
    }


    //////////  DEBUG METHODS //////////

    public List<Integer> createDebugSolution() {
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


    public List<Boolean> createDebugMask() {
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


    //////////  GETTERS AND SETTERS //////////

    public List<Boolean> getIsAnswerCorrect() {
        return isAnswerCorrect;
    }

    public void setIsAnswerCorrect(List<Boolean> isAnswerCorrect) {
        this.isAnswerCorrect = isAnswerCorrect;
    }

    public List<Boolean> getHasSolution() {
        return hasSolution;
    }

    public void setHasSolution(List<Boolean> hasSolution) {
        this.hasSolution = hasSolution;
    }

    public List<Integer> getPuzzleSolution() {
        return puzzleSolution;
    }

    public void setPuzzleSolution(List<Integer> puzzleSolution) {
        this.puzzleSolution = puzzleSolution;
    }

    public List<Boolean> getHasUserInput() {
        return hasUserInput;
    }

    public void setHasUserInput(List<Boolean> hasUserInput) {
        this.hasUserInput = hasUserInput;
    }

    public List<Boolean> getIsNumberComplete() {
        return isNumberComplete;
    }

    public void setIsNumberComplete(List<Boolean> isNumberComplete) {
        this.isNumberComplete = isNumberComplete;
    }
}
