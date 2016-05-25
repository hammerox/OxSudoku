package com.example.hammerox.oxsudoku;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hammerox.oxsudoku.Tools.SquareLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SudokuGrid {

    final static int GRID_SIZE = 81;
    final static int KEYBOARD_SIZE = 9;

    private int lastInputId = -1;
    private Boolean lastInputIsPencil;
    private int emptyCells;

    private List<Integer> puzzleSolution;
    private List<Boolean> hasSolution;
    private List<Integer> puzzleAnswers;
    private List<Boolean> isAnswerCorrect;
    private List<Boolean> hasUserInput;
    private List<Integer> puzzleHighlight;
    private List<List<Integer>> puzzlePencil;
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

        /*Todo LOW - Don't forget to delete these lines when debugging is finished.*/
/*        emptyCells = createDebugEmptyCells();
        puzzleSolution = createDebugSolution();
        hasSolution = createDebugMask();*/
        /**/

        int maxemptyCells = 60;
        SudokuGenerator puzzle = new SudokuGenerator(maxemptyCells);
        emptyCells = puzzle.emptyCells;
        puzzleSolution = puzzle.board;
        hasSolution = puzzle.mask;
        puzzleAnswers = createAnswerList();
        puzzleHighlight = createHighlightList();
        puzzlePencil = createPencilList();

        isAnswerCorrect = createBooleanGrid(GRID_SIZE, false);
        hasUserInput = createBooleanGrid(GRID_SIZE, false);
        isNumberComplete = createBooleanGrid(KEYBOARD_SIZE, false);
    }


    public void drawPuzzle(final Activity activity, final View rootView) {

        SquareLayout gridLayout = (SquareLayout) rootView.findViewById(R.id.sudoku_gridlayout);
        for (int row = 1; row <= 9; row++) {
            // Creating a LinearLayout for each row
            LinearLayout rowLayout = new LinearLayout(activity);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1.0f));

            for (int col = 1; col <= 9; col++) {
                FrameLayout cellView = new FrameLayout(activity);
                cellView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f));
                // ID
                int cellId = GridPosition.getCellId(row, col, cellView);
                cellView.setId(cellId);
                // Appearance
                Drawable mDrawable = getGridDrawable(activity, row, col);
                setColorFilter(activity, mDrawable, 0);
                cellView.setBackground(mDrawable);

                // Creating views for answers and sketch for each square on the grid.
                TextView answerView = createAnswerSlot(activity, row, col);
                TableLayout pencilView = createPencilSlot(activity, row, col);

                int index = GridPosition.getIndexFromPosition(row, col);
                // If hasSolution is true, show number. Otherwise, demand user input;
                if (hasSolution.get(index)) {
                    answerView.setText(String.valueOf(puzzleSolution.get(index)));
                } else {
                    // User's TextView should be clickable and have a different color.
                    int mColor = ContextCompat.getColor(activity, R.color.colorAccent);
                    answerView.setTextColor(mColor);
                    answerView.isClickable();
                        // Interaction
                    setCellTouchListener(activity, answerView);
                    setCellTouchListener(activity, pencilView);
                    setAnswerClickListener(activity, cellView, answerView, pencilView);
                    setPencilClickListener(activity, cellView, answerView, pencilView);
                }
                cellView.addView(answerView);
                cellView.addView(pencilView);
                pencilView.setVisibility(View.GONE);
                rowLayout.addView(cellView);
            }
            gridLayout.addView(rowLayout);
        }

    }


    public void setCellTouchListener(final Activity activity, final View cell) {
        cell.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                FrameLayout parent = (FrameLayout) v.getParent();
                Drawable background = parent.getBackground();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setColorFilter(activity, background, 4);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    int i = GridPosition.getIndexFromView(cell);
                    int intensity = puzzleHighlight.get(i);
                    setColorFilter(activity, background, intensity);
                }
                return false;
            }
        });
    }


    public void setAnswerClickListener(final Activity activity,
                                       final FrameLayout cellLayout,
                                       final TextView answerView,
                                       final TableLayout pencilView) {

        answerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexOfClick =
                        GridPosition.getIndexFromView(v);
                Boolean pencilMode = SudokuKeyboard.getPencilMode();
                Boolean eraseMode = SudokuKeyboard.getEraseMode();
                int activeKey = SudokuKeyboard.getActiveKey();
                Log.d("class", v.getClass().toString());
                if (activeKey != 0) {       // A key from keyboard must be selected.
                    if (pencilMode) {
                        Boolean hasAnswer = getHasUserInput().get(indexOfClick);
                        if (!hasAnswer) {   // Pencil cannot override answer
                            pencilClick(activity, v, cellLayout, pencilView, indexOfClick, activeKey);
                        }
                    } else {
                        answerClick(activity, v, cellLayout, answerView, indexOfClick, activeKey);
                    }
                }
            }
        });
    }


    public void setPencilClickListener(final Activity activity,
                                       final FrameLayout cellLayout,
                                       final TextView answerView,
                                       final TableLayout pencilView) {

        pencilView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexOfClick =
                        GridPosition.getIndexFromView(v);
                Boolean pencilMode = SudokuKeyboard.getPencilMode();
                Boolean eraseMode = SudokuKeyboard.getEraseMode();
                int activeKey = SudokuKeyboard.getActiveKey();
                Log.d("class", v.getClass().toString());
                if (activeKey != 0) {       // A key from keyboard must be selected.
                    if (pencilMode) {
                        pencilClick(activity, v, cellLayout, pencilView, indexOfClick, activeKey);
                    } else {
                        answerClick(activity, v, cellLayout, answerView, indexOfClick, activeKey);
                    }
                }
            }
        });

    }


    public void answerClick(Activity activity,
                            View clickedView,
                            FrameLayout cellLayout,
                            TextView answerView,
                            int indexOfClick, int activeKey) {

        /*  NORMAL MODE (ANSWER MODE)
            Checking if user input is valid.
            If user input is valid, commit changes.
            If user input is not valid, warn and show conflicts.*/
        Boolean isValid = isValidAnswer(indexOfClick, activeKey);
        updatePuzzleHighlight(activity, activeKey);
        if (isValid) {
            Boolean needsToSwap = needsToSwapViews(clickedView, answerView);
            if (needsToSwap) {
                swapViews(clickedView, answerView);
            }
            commitChanges(activity, cellLayout, answerView);
        } else {
            showConflicts(activity, indexOfClick, activeKey);
        }
    }


    public void pencilClick(Activity activity,
                            View clickedView,
                            FrameLayout cellLayout,
                            TableLayout pencilView,
                            int indexOfClick, int activeKey) {

        /*  PENCIL MODE
        *   Update last input and make new changes to clicked layout.*/
        int sketchId = GridPosition.getPencilId(indexOfClick, activeKey);
        TextView sketchView = (TextView) pencilView.findViewById(sketchId);
        Pair testValues = isNewPencilNumber(indexOfClick, activeKey);
        Boolean isNewPencilNumber = (Boolean) testValues.first;

        if (isNewPencilNumber) {
            updateLastInput(activity, sketchView, true);
            Boolean needsToSwap = needsToSwapViews(clickedView, pencilView);
            if (needsToSwap) {
                swapViews(clickedView, pencilView);
            }
            sketchView.setTextColor(Color.BLUE);
            puzzlePencil.get(indexOfClick).add(activeKey);

        } else {
            int positionToRemove = (Integer) testValues.second;
            sketchView.setTextColor(Color.TRANSPARENT);
            puzzlePencil.get(indexOfClick).remove(positionToRemove);
            if (sketchId == lastInputId) {
                lastInputId = -1;
            }
        }

    }


    public void clearPencilCell(Activity activity, int index) {
        List<Integer> pencilList = puzzlePencil.get(index);
        Boolean containsSomething = !pencilList.isEmpty();
        if (containsSomething) {
            for (Integer number : pencilList) {
                int idToRemove = GridPosition.getPencilId(index, number);
                TextView pencil = (TextView) activity.findViewById(idToRemove);
                pencil.setTextColor(Color.TRANSPARENT);
                pencilList.remove(0);
            }
        }
    }


    public void commitChanges(Activity activity, FrameLayout cellView, TextView view) {
        int activeKey = SudokuKeyboard.getActiveKey();
        int indexOfClick = GridPosition.getIndexFromView(view);
        List<Integer> rowColBox = GridPosition.getRowColBoxIndexes(indexOfClick, false);

        // Checking if there was a number before new input.
        int oldNumber = 0;
        Boolean isSubstituting = false;

        String oldNumberString = view.getText().toString();
        if (oldNumberString.length() > 0) {
            oldNumber = Integer.valueOf(oldNumberString);
            isSubstituting = true;
        }

        // Updating lastInputId.
        updateLastInput(activity, view, false);

        // Setting new value.
        view.setText(String.valueOf(activeKey));
        view.setTextColor(Color.BLUE);

        // Updating puzzleAnswers.
        puzzleAnswers.set(indexOfClick, activeKey);

        // Cleaning respective puzzlePencil.
        clearPencilCell(activity, indexOfClick);

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
        setColorFilter(activity, cellView.getBackground(), 2);
        puzzleHighlight.set(indexOfClick, 2);


        // For every cell on rowColBox...
        for (Integer i : rowColBox) {
            // Updating puzzleHighlight Level 1
            if (puzzleHighlight.get(i) == 0) {
                setIntensityColor(activity, i, 1);
                puzzleHighlight.set(i, 1);
            }

            // Updating puzzlePencil
            Pair testValues = isNewPencilNumber(i, activeKey);
            Boolean containsSamePencilNumber = !(Boolean)testValues.first;
            if (containsSamePencilNumber) {
                int idToRemove = GridPosition.getPencilId(i, activeKey);
                TextView sketchView = (TextView) activity.findViewById(idToRemove);
                sketchView.setTextColor(Color.TRANSPARENT);

                int indexToRemove = (Integer) testValues.second;
                puzzlePencil.get(i).remove(indexToRemove);
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
        if (isSubstituting) {
            int listsIndex = oldNumber - 1;
            isNumberComplete.set(listsIndex, false);
            SudokuKeyboard.showButton(activity, oldNumber);
        }

        // Checking if puzzle is complete.
        int answerCounter = 0;
        for (Boolean answer : isAnswerCorrect) {
            if (answer) answerCounter++;
        }
        if (answerCounter == emptyCells) {
            Toast toast = Toast.makeText(activity, "CONGRATULATIONS!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Toast toast2 = Toast.makeText(activity, "You completed the puzzle!", Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }
    }


    public Boolean isValidAnswer(int clickedRow, int clickedCol, int activeKey) {
        List<Integer> rowColBox =
                GridPosition.getRowColBoxIndexes(clickedRow, clickedCol, false);
        for (Integer i : rowColBox) {
            int answer = puzzleAnswers.get(i);
            if (answer == activeKey) {
                return false;
            }
        }
        return true;
    }


    public Boolean isValidAnswer(int index, int activeKey) {
        int[] position = GridPosition.getPositionFromIndex(index);
        int row = position[0];
        int col = position[1];
        return isValidAnswer(row, col, activeKey);
    }


    public Pair<Boolean, Integer> isNewPencilNumber(int index, int activeKey) {
        /*  Check on cell's pencil list if it contains the same input number.
        *   If it finds an equal number, returns FALSE.
        *   If it do not find, it returns TRUE. */
        List<Integer> pencilNumbers = puzzlePencil.get(index);
        if (pencilNumbers.isEmpty()) {
            return new Pair<>(true, -1);
        } else {
            int size = pencilNumbers.size();
            for (int i = 0; i < size; i++) {
                if (pencilNumbers.get(i) == activeKey) {
                    return new Pair<>(false, i);
                }
            }
        }
        return new Pair<>(true, -1);
    }


    public void updateLastInput(Activity activity, View newView, Boolean isNewInputPencil) {
        // Updating lastInputId.
        if (lastInputId >= 0) {
            TextView lastInputCell = (TextView) activity.findViewById(lastInputId);
            int mColorOld;
            if (lastInputIsPencil) {
                mColorOld = ContextCompat.getColor(activity, R.color.colorPrimaryLight);
            } else {
                mColorOld = ContextCompat.getColor(activity, R.color.colorAccent);
            }
            lastInputCell.setTextColor(mColorOld);
        }
        lastInputId = newView.getId();
        this.lastInputIsPencil = isNewInputPencil;
    }


    public Boolean needsToSwapViews(View clickedView, View viewToCompare) {
        Object clickedClass = clickedView.getClass();
        Object compareToClass = viewToCompare.getClass();
        return !clickedClass.equals(compareToClass);
    }


    public void swapViews(View clickedView, View viewToSwap) {
        clickedView.setVisibility(View.GONE);
        viewToSwap.setVisibility(View.VISIBLE);
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
                int id = GridPosition.getIdFromIndex(i);
                Drawable cell = activity.findViewById(id).getBackground();
                setColorFilter(activity, cell, 3);
            }

        }

        if (isOnRow) {
            for (Integer i : checkRow) {
                int id = GridPosition.getIdFromIndex(i);
                Drawable cell = activity.findViewById(id).getBackground();
                setColorFilter(activity, cell, 3);
            }

        }

        if (isOnCol) {
            for (Integer i : checkCol) {
                int id = GridPosition.getIdFromIndex(i);
                Drawable cell = activity.findViewById(id).getBackground();
                setColorFilter(activity, cell, 3);
            }
        }

        // Mark conflicting cells.
        for (Integer i : conflictIndexes) {
            int id = GridPosition.getIdFromIndex(i);
            Drawable cell = activity.findViewById(id).getBackground();
            setColorFilter(activity, cell, 4);
        }

    }


    public void showConflicts(Activity activity, int index, int activeKey) {
        int[] position = GridPosition.getPositionFromIndex(index);
        int row = position[0];
        int col = position[1];
        showConflicts(activity, row, col, activeKey);
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


    public TextView createAnswerSlot(Activity activity, int row, int col) {
        TextView answerView = new TextView(activity);
        // IDs
        int answerId = GridPosition.getCellId(row, col, answerView);
        answerView.setId(answerId);
        // LayoutParams
        answerView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        // Appearance
        answerView.setGravity(Gravity.CENTER);
        answerView.setTypeface(Typeface.DEFAULT_BOLD);
        answerView.setTextSize(30);

        return answerView;
    }


    public TableLayout createPencilSlot(Activity activity, int row, int col) {
        TableLayout pencilView = new TableLayout(activity);
        // ID
        int pencilId = GridPosition.getCellId(row, col, pencilView);
        pencilView.setId(pencilId);
        // LayoutParams
        pencilView.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        TableRow tableRow = new TableRow(activity);
        for (int n = 1; n <= 9; n++) {
            // LayoutParams
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));

                TextView textView = new TextView(activity);
                // ID
                int positionId = GridPosition.getPencilId(row, col, n);
                textView.setId(positionId);
                // LayoutParams
                textView.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                1.0f));
                // Appearance
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(10);
                textView.setText(String.valueOf(n));
                ColorStateList mTransparent = ColorStateList.valueOf(Color.TRANSPARENT);
                textView.setTextColor(mTransparent);

                tableRow.addView(textView);

            if (n % 3 == 0) {
                pencilView.addView(tableRow);
                tableRow = new TableRow(activity);
            }
        }

        return pencilView;
    }


    public void setIntensityColor(Activity activity, int index, int highlightLevel) {
        int id = GridPosition.getIdFromIndex(index);
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
            mColor = ContextCompat.getColor(activity, R.color.colorHighlightArea);
        } else if (highlightLevel == 2) {
            mColor = ContextCompat.getColor(activity, R.color.colorHighlight);
        } else if (highlightLevel == 3) {
            mColor = ContextCompat.getColor(activity, R.color.colorWarnArea);
        } else {
            mColor = ContextCompat.getColor(activity, R.color.colorWarn);
        }
        mDrawable.setColorFilter(
                new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
    }


    //////////  LIST CREATERS //////////

    public List<Integer> createAnswerList() {
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


    public List<Boolean> createBooleanGrid(int size, Boolean booleanToFill) {
        Boolean[] array = new Boolean[size];
        Arrays.fill(array, booleanToFill);
        return Arrays.asList(array);
    }


    public List<Integer> createHighlightList() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            list.add(0);
        }
        return list;
    }


    public List<List<Integer>> createPencilList() {
        List<List<Integer>> pencilList = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            pencilList.add(new ArrayList<Integer>());
        }
        return pencilList;
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


    public int createDebugEmptyCells() {
        return 51;
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
