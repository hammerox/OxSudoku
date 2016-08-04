package com.example.hammerox.oxsudoku.ui.views;

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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hammerox.oxsudoku.ui.SudokuFragment;
import com.example.hammerox.oxsudoku.utils.PuzzleSnapshot;
import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.utils.GridPosition;
import com.example.hammerox.oxsudoku.services.SudokuGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SudokuGrid {

    final static int GRID_SIZE = 81;
    final static int KEYBOARD_SIZE = 9;

    private int lastInputId = -1;
    private Boolean lastInputIsPencil;
    private int emptyCells;
    private Boolean isPuzzleComplete = false;

    private List<Integer> puzzleSolution;
    private List<Boolean> hasSolution;
    private List<Integer> puzzleAnswers;
    private List<Boolean> isAnswerCorrect;
    private List<Boolean> hasUserInput;
    private List<Integer> puzzleHighlight;
    private List<List<Integer>> puzzlePencil;
    private List<Boolean> isNumberComplete;
    private List<List<Boolean>> hasPencil;

    private List<PuzzleSnapshot> puzzleHistory;


    public SudokuGrid(SudokuGenerator puzzle) {
        /* The puzzle is generated on class creation.
        * puzzleSolution = Integer. Contains all correct answers.
        * hasSolution = Boolean. Show respective solution if true. False will demand user input.
        * isAnswerCorrect = Boolean. Is true if respective user's input matches solution.
        * hasUserInput = Boolean. Is true if contains user value.
        * puzzleHightlight = Integer. 0 shows no highlight, 1 shows partial and 2 shows full highlight.
        * */

        /*Todo LOW - Don't forget to delete these lines when debugging is finished.*/
/*        this.emptyCells = createDebugEmptyCells();
        puzzleSolution = createDebugSolution();
        hasSolution = createDebugMask();*/
        /**/

        this.emptyCells = puzzle.getEmptyCellsCounter();
        puzzleSolution = puzzle.getBoard();
        hasSolution = puzzle.getMask();
        puzzleAnswers = createAnswerList();
        puzzleHighlight = createHighlightList();
        puzzlePencil = createPencilIntList();

        isAnswerCorrect = createBooleanGrid(GRID_SIZE, false);
        hasUserInput = createBooleanGrid(GRID_SIZE, false);
        isNumberComplete = createBooleanGrid(KEYBOARD_SIZE, false);
        hasPencil = createPencilBolList(false);

        puzzleHistory = new ArrayList<>();
    }


    public void drawPuzzle(final Activity activity, final View rootView) {
        float defaultSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, activity.getResources().getDisplayMetrics());
        float pencilSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                6.66f, activity.getResources().getDisplayMetrics());

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
                TextView answerView = createAnswerSlot(activity, row, col, defaultSize);
                TableLayout pencilView = createPencilSlot(activity, row, col, pencilSize);

                int index = GridPosition.getIndexFromPosition(row, col);
                // If hasSolution is true, show number. Otherwise, demand user input;
                if (hasSolution.get(index)) {
                    answerView.setText(String.valueOf(puzzleSolution.get(index)));
                } else {
                    // User's TextView should be clickable and have a different color.
                    int mColor = ContextCompat.getColor(activity, R.color.text_primary);
                    answerView.setTextColor(mColor);
                    answerView.isClickable();
                        // Interaction
                    setCellTouchListener(activity, answerView);
                    setCellTouchListener(activity, pencilView);
                    setClickOnAnswerCell(activity, cellView, answerView, pencilView);
                    setClickOnPencilCell(activity, cellView, answerView, pencilView);
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


    public void setClickOnAnswerCell(final Activity activity,
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

                if (eraseMode) {
                    eraserClick(activity, v, activeKey);
                } else {
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
            }
        });
    }


    public void setClickOnPencilCell(final Activity activity,
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

                if (eraseMode) {
                    eraserClick(activity, v, activeKey);
                } else {
                    if (activeKey != 0) {       // A key from keyboard must be selected.
                        if (pencilMode) {
                            pencilClick(activity, v, cellLayout, pencilView, indexOfClick, activeKey);
                        } else {
                            answerClick(activity, v, cellLayout, answerView, indexOfClick, activeKey);
                        }
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
        showHighlight(activity, activeKey);
        if (isValid) {
            Boolean needsToSwap = needsToSwapViews(clickedView, answerView);
            if (needsToSwap) {
                swapViews(clickedView, answerView);
            }
            takeSnapshot();
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
        Boolean isNewPencilNumber = isNewPencilNumber(indexOfClick, activeKey).first;

        takeSnapshot();
        if (isNewPencilNumber) {
            updateLastInput(activity, sketchView, true);
            Boolean needsToSwap = needsToSwapViews(clickedView, pencilView);
            if (needsToSwap) {
                swapViews(clickedView, pencilView);
            }
            addPencilNumber(activity, sketchView, indexOfClick, activeKey);
            sketchView.setTextColor(Color.BLUE);

        } else {
            removePencilNumber(activity, indexOfClick, activeKey);
            if (sketchId == lastInputId) {
                lastInputId = -1;
            }
        }
    }


    public void eraserClick(Activity activity, View clickedView, int activeKey) {
        int index = GridPosition.getIndexFromView(clickedView);
        if (clickedView instanceof TextView) {
            TextView viewToErase = (TextView) clickedView;
            String oldNumberString = viewToErase.getText().toString();

            if (oldNumberString.length() > 0) {
                int oldNumber = Integer.valueOf(viewToErase.getText().toString());
                int listsIndex = oldNumber - 1;

                // Updating puzzle.
                takeSnapshot();
                puzzleAnswers.set(index, 0);
                hasUserInput.set(index, false);
                isAnswerCorrect.set(index, false);
                puzzleHighlight.set(index, 0);
                isNumberComplete.set(listsIndex, false);
                SudokuKeyboard.showButton(activity, oldNumber);
                viewToErase.setText("");
                if (oldNumber == activeKey) {
                    showHighlight(activity, activeKey);
                }
            }

        } else if (clickedView instanceof TableLayout) {
            takeSnapshot();
            clearPencilCell(activity, index);
        }
    }


    //////////  ACTIONS  //////////

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
                removePencilNumber(activity, i, activeKey);
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
        List<Integer> wrongArray = new ArrayList<>();
        int correctCount = 0;
        int wrongCount = 0;
        int totalCount = 0;
        for (int index = 0; index < GRID_SIZE; index++) {
            if (!isAnswerCorrect.get(index) && hasUserInput.get(index)) {
                wrongArray.add(index);
                wrongCount++;
            } else if (isAnswerCorrect.get(index) && hasUserInput.get(index)) {
                correctCount++;
            }
            if (!hasSolution.get(index)) totalCount++;
        }
        if (correctCount == totalCount) {
            isPuzzleComplete = true;
            SudokuFragment.chronoPause();
            Toast toast = Toast.makeText(activity, "CONGRATULATIONS!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            Toast toast2 = Toast.makeText(activity, "You completed the puzzle!", Toast.LENGTH_LONG);
            toast2.setGravity(Gravity.CENTER, 0, 0);
            toast2.show();
        }
    }


    public void clearPencilCell(Activity activity, int index) {
        List<Integer> pencilList = puzzlePencil.get(index);
        Boolean containsSomething = !pencilList.isEmpty();
        if (containsSomething) {
            int size = pencilList.size();
            for (int i = 0; i < size; i++) {
                int number = pencilList.get(0);
                removePencilNumber(activity, index, number);
            }
        }
    }


    public void updateLastInput(Activity activity, View newView, Boolean isNewInputPencil) {
        // Updating lastInputId.
        if (lastInputId >= 0) {
            TextView lastInputCell = (TextView) activity.findViewById(lastInputId);
            int mColorOld;
            if (lastInputIsPencil) {
                mColorOld = ContextCompat.getColor(activity, R.color.text_secondary);
            } else {
                mColorOld = ContextCompat.getColor(activity, R.color.warn_primary);
            }
            lastInputCell.setTextColor(mColorOld);
        }
        lastInputId = newView.getId();
        this.lastInputIsPencil = isNewInputPencil;
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
        List<Integer> checkBox = GridPosition.getBoxList(clickedRow, clickedCol, false);
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


    public void showHighlight(Activity activity, int activeKey) {
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


    public void showPencilHighligh(Activity activity, int lastKey, int activeKey) {
        clearPencilHighlight(activity, lastKey);

        for (int i = 0; i < GRID_SIZE; i++) {
            if (hasPencil.get(i).get(activeKey - 1)) {
                int cellId = GridPosition.getCellId(i);
                FrameLayout cell = (FrameLayout) activity.findViewById(cellId);

                int activeKeyId = GridPosition.getPencilId(i, activeKey);
                TextView activeView = (TextView) cell.findViewById(activeKeyId);

                activeView.setTextColor(Color.RED);
            }
        }
    }


    public void takeSnapshot() {
        PuzzleSnapshot snapshot
                = new PuzzleSnapshot(emptyCells, hasSolution, hasUserInput,
                isAnswerCorrect, isNumberComplete, lastInputId, lastInputIsPencil,
                puzzleAnswers, puzzlePencil, hasPencil, puzzleSolution);
        puzzleHistory.add(snapshot);
    }


    public void takeSnapshot(Boolean lastInputWasFill) {
        PuzzleSnapshot snapshot
                = new PuzzleSnapshot(emptyCells, hasSolution, hasUserInput,
                isAnswerCorrect, isNumberComplete, lastInputId, lastInputIsPencil,
                puzzleAnswers, puzzlePencil, hasPencil, puzzleSolution, lastInputWasFill);
        puzzleHistory.add(snapshot);
    }


    //////////  FEATURES  //////////

    public void undoLastInput(Activity activity) {
        if (!puzzleHistory.isEmpty()) {
            int listIndex = puzzleHistory.size() - 1;
            PuzzleSnapshot lastSnapshot = puzzleHistory.get(listIndex);
            Boolean lastInputWasFill = lastSnapshot.getLastInputWasFill();

                                                // Check if user used Auto Fill.
            if (lastInputWasFill) {             // If used AutoFill, undo the whole grid.
                // Find all the cells that has changed...
                for (int i = 0; i < GRID_SIZE; i++) {
                    List<Integer> actualPencilList = getPuzzlePencil().get(i);
                    List<Integer> oldPencilList = lastSnapshot.getPuzzlePencil().get(i);
                    Boolean pencilHasChanged = !oldPencilList.equals(actualPencilList);
                    if (pencilHasChanged) {     // and and replace its pencil list.
                        replacePencilList(activity, lastSnapshot, i);
                    }
                }


            } else {    // If user didn't used AutoFill, undo only a few cells.
                    // Find if answer has changed.
                    Boolean answerHasChanged = false;
                int changedIndex = -1;
                List<Integer> oldAnswerGrid = lastSnapshot.getPuzzleAnswers();
                for (int i = 0; i < GRID_SIZE; i++) {               // For each cell...
                    int actualAnswer = puzzleAnswers.get(i);
                    int oldAnswer = oldAnswerGrid.get(i);
                    if (actualAnswer != oldAnswer) {                // Check if number has changed.
                        answerHasChanged = true;
                        changedIndex = i;                           // If true, get index of changed cell.
                        break;
                    }
                }

                if (answerHasChanged) {         // If answer has changed...
                    // Get all cell views, ...
                    int cellId = GridPosition.getIdFromIndex(changedIndex);
                    FrameLayout cell = (FrameLayout) activity.findViewById(cellId);
                    TextView answerView = (TextView) cell.getChildAt(0);
                    TableLayout pencilParentView = (TableLayout) cell.getChildAt(1);

                    // Set answer's view to old answer...
                    int oldAnswer = oldAnswerGrid.get(changedIndex);
                    if (oldAnswer != 0) {
                        answerView.setText(String.valueOf(oldAnswer));
                    } else {
                        answerView.setText("");
                    }
                    int answerColor = ContextCompat.getColor(activity, R.color.text_primary);
                    answerView.setTextColor(answerColor);

                    // And compare if pencil has changed on the same cell.
                    List<Integer> actualPencilList = getPuzzlePencil().get(changedIndex);
                    List<Integer> oldPencilList = lastSnapshot.getPuzzlePencil().get(changedIndex);
                    Boolean pencilHasChanged = !oldPencilList.equals(actualPencilList);

                    if (pencilHasChanged) {     // If pencil has changed...
                        // Swap views ...
                        swapViews(answerView, pencilParentView);
                        // And add all missing numbers.
                        List<Integer> numberToAdd = comparePencilLists(oldPencilList, actualPencilList);
                        if (!numberToAdd.isEmpty()) {
                            for (Integer number : numberToAdd) {
                                addPencilNumber(activity, changedIndex, number);
                            }
                        }
                    }
                    // Finally, show all remaining changed pencil views.
                    int actualAnswer = puzzleAnswers.get(changedIndex);
                    for (int i = 0; i < GRID_SIZE; i++) {
                        if (i != changedIndex) {
                            actualPencilList = getPuzzlePencil().get(i);
                            oldPencilList = lastSnapshot.getPuzzlePencil().get(i);
                            pencilHasChanged = !oldPencilList.equals(actualPencilList);
                            if (pencilHasChanged) {
                                addPencilNumber(activity, changedIndex, actualAnswer);
                            }
                        }
                    }

                } else {                // If answer has not changed...
                    // Find for the only cell that has changed...
                    for (int i = 0; i < GRID_SIZE; i++) {
                        List<Integer> actualPencilList = getPuzzlePencil().get(i);
                        List<Integer> oldPencilList = lastSnapshot.getPuzzlePencil().get(i);
                        Boolean pencilHasChanged = !oldPencilList.equals(actualPencilList);
                        if (pencilHasChanged) {     // and get its index.
                            changedIndex = i;
                            break;
                        }
                    }
                    replacePencilList(activity, lastSnapshot, changedIndex);
                }
            }

            // Show user's last input if possible.
            int oldLastInputId = lastSnapshot.getLastInputId();
            if (oldLastInputId > 0) {
                TextView lastInputView = (TextView) activity.findViewById(oldLastInputId);
                lastInputView.setTextColor(Color.BLUE);
            }

            lastInputId = lastSnapshot.getLastInputId();
            lastInputIsPencil = lastSnapshot.getLastInputIsPencil();
            puzzleAnswers = lastSnapshot.getPuzzleAnswers();
            puzzlePencil = lastSnapshot.getPuzzlePencil();
            isAnswerCorrect = lastSnapshot.getIsAnswerCorrect();
            hasUserInput = lastSnapshot.getHasUserInput();
            isNumberComplete = lastSnapshot.getIsNumberComplete();

            // Update highlight.
            int activeKey = SudokuKeyboard.getActiveKey();
            if (activeKey > 0) {
                showHighlight(activity, activeKey);
            }

            // Update history.
            puzzleHistory.remove(listIndex);
        }
    }


    public void autoFill(Activity activity) {
        for (int index = 0; index < GRID_SIZE; index++) {
            Boolean isEmpty = !hasSolution.get(index) && !hasUserInput.get(index);
            if (isEmpty) {
                clearPencilCell(activity, index);
                int cellId = GridPosition.getCellId(index);
                FrameLayout cell = (FrameLayout) activity.findViewById(cellId);
                TextView answerView = (TextView) cell.getChildAt(0);
                TableLayout pencilView = (TableLayout) cell.getChildAt(1);

                Boolean needsToSwap = needsToSwapViews(cell, pencilView);
                if (needsToSwap) swapViews(answerView, pencilView);

                for (int number = 1; number <= KEYBOARD_SIZE; number++) {
                    Boolean isValid = isValidAnswer(index, number);
                    if (isValid) {
                        addPencilNumber(activity, index, number);

                    }
                }
            }
        }
    }


    //////////  TOOLS //////////

    public void addPencilNumber(Activity activity, TextView pencilView, int index, int number) {
        int pencilColor = ContextCompat.getColor(activity, R.color.text_secondary);
        pencilView.setTextColor(pencilColor);
        getPuzzlePencil().get(index).add(number);
        hasPencil.get(index).set(number - 1, true);
    }


    public void addPencilNumber(Activity activity, int index, int number) {
        int pencilId = GridPosition.getPencilId(index, number);
        TextView pencilView = (TextView) activity.findViewById(pencilId);

        addPencilNumber(activity, pencilView, index, number);
    }


    public void removePencilNumber(Activity activity, int index, int number) {
        int pencilId = GridPosition.getPencilId(index, number);
        TextView pencilView = (TextView) activity.findViewById(pencilId);
        pencilView.setTextColor(Color.TRANSPARENT);
        List<Integer> pencilList = puzzlePencil.get(index);
        int listSize = pencilList.size();
        for (int position = 0; position < listSize; position++) {
            if (pencilList.get(position) == number) {
                pencilList.remove(position);
                break;
            }
        }
        hasPencil.get(index).set(number - 1, false);
    }


    public void replacePencilList(Activity activity, PuzzleSnapshot lastSnapshot, int index) {
        List<Integer> actualPencilList = getPuzzlePencil().get(index);
        List<Integer> oldPencilList = lastSnapshot.getPuzzlePencil().get(index);
        // Show all missing numbers...
        List<Integer> numberToAdd = comparePencilLists(oldPencilList, actualPencilList);
        if (!numberToAdd.isEmpty()) {
            for (Integer number : numberToAdd) {
                addPencilNumber(activity, index, number);
            }
        }
        // And hide all left over numbers.
        List<Integer> numberToRemove = comparePencilLists(actualPencilList, oldPencilList);
        if (!numberToRemove.isEmpty()) {
            for (Integer number : numberToRemove) {
                removePencilNumber(activity, index, number);
            }
        }
    }


    public List<Integer> comparePencilLists(List<Integer> firstList, List<Integer> secondList) {
        List<Integer> missingNumbers = new ArrayList<>();
        for (Integer number : firstList) {
            Boolean isMissing = true;
            for (Integer n : secondList) {      // Check if number is missing from the second list
                if (n == number) {
                    isMissing = false;
                    break;
                }
            }
            if (isMissing) {                    // If number is missing...
                missingNumbers.add(number);     // Add it to return list.
            }
        }
        return missingNumbers;
    }


    public Boolean needsToSwapViews(View clickedView, View viewToCompare) {
        Object clickedClass = clickedView.getClass();
        Object compareToClass = viewToCompare.getClass();
        return !clickedClass.equals(compareToClass);
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
            mColor = ContextCompat.getColor(activity, R.color.accent_light);
        } else if (highlightLevel == 2) {
            mColor = ContextCompat.getColor(activity, R.color.accent);
        } else if (highlightLevel == 3) {
            mColor = ContextCompat.getColor(activity, R.color.warn_secondary);
        } else {
            mColor = ContextCompat.getColor(activity, R.color.warn_primary);
        }
        mDrawable.setColorFilter(
                new PorterDuffColorFilter(mColor, PorterDuff.Mode.MULTIPLY));
    }


    //////////  CREATERS AND EDITTERS //////////

    public TextView createAnswerSlot(Activity activity, int row, int col, float numSize) {
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
        answerView.setTextSize(numSize);

        return answerView;
    }


    public TableLayout createPencilSlot(Activity activity, int row, int col, float numSize) {
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
            textView.setTextSize(numSize);
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


    public void setHighlightLevel1(int row, int col) {
        List<Integer> allIndexes = GridPosition.getRowColBoxIndexes(row, col, false);
        for (Integer i : allIndexes) {
            int intensity = puzzleHighlight.get(i);
            if (intensity == 0) puzzleHighlight.set(i, 1);
        }
    }


    public void clearPuzzleHighlight(Activity activity) {
        // This makes puzzleHighlight as default.
        for (int i = 0; i < GRID_SIZE; i++) {
            setIntensityColor(activity, i, 0);
            puzzleHighlight.set(i, 0);
        }
    }


    public void clearPencilHighlight(Activity activity, int lastKey) {
        if (lastKey > 0) {
            for (int i = 0; i < GRID_SIZE; i++) {
                if (hasPencil.get(i).get(lastKey - 1)) {
                    int cellId = GridPosition.getCellId(i);
                    FrameLayout cell = (FrameLayout) activity.findViewById(cellId);

                    int lastKeyId = GridPosition.getPencilId(i, lastKey);
                    TextView lastView = (TextView) cell.findViewById(lastKeyId);

                    int pencilColor = ContextCompat.getColor(activity, R.color.text_secondary);
                    lastView.setTextColor(pencilColor);
                }
            }
        }
    }


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


    public List<List<Integer>> createPencilIntList() {
        List<List<Integer>> pencilList = new ArrayList<>();
        for (int i = 0; i < GRID_SIZE; i++) {
            pencilList.add(new ArrayList<Integer>());
        }
        return pencilList;
    }


    public List<List<Boolean>> createPencilBolList(Boolean booleanToFill) {
        List<List<Boolean>> pencilList = new ArrayList<>();
        for (int index = 0; index < GRID_SIZE; index++) {
            Boolean[] array = new Boolean[KEYBOARD_SIZE];
            Arrays.fill(array, booleanToFill);
            pencilList.add(Arrays.asList(array));
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

    public int getEmptyCells() {
        return emptyCells;
    }

    public void setEmptyCells(int emptyCells) {
        this.emptyCells = emptyCells;
    }

    public static int getGridSize() {
        return GRID_SIZE;
    }

    public static int getKeyboardSize() {
        return KEYBOARD_SIZE;
    }

    public int getLastInputId() {
        return lastInputId;
    }

    public void setLastInputId(int lastInputId) {
        this.lastInputId = lastInputId;
    }

    public Boolean getLastInputIsPencil() {
        return lastInputIsPencil;
    }

    public void setLastInputIsPencil(Boolean lastInputIsPencil) {
        this.lastInputIsPencil = lastInputIsPencil;
    }

    public List<Integer> getPuzzleAnswers() {
        return puzzleAnswers;
    }

    public void setPuzzleAnswers(List<Integer> puzzleAnswers) {
        this.puzzleAnswers = puzzleAnswers;
    }

    public List<Integer> getPuzzleHighlight() {
        return puzzleHighlight;
    }

    public void setPuzzleHighlight(List<Integer> puzzleHighlight) {
        this.puzzleHighlight = puzzleHighlight;
    }

    public List<List<Integer>> getPuzzlePencil() {
        return puzzlePencil;
    }

    public void setPuzzlePencil(List<List<Integer>> puzzlePencil) {
        this.puzzlePencil = puzzlePencil;
    }

    public List<List<Boolean>> getHasPencil() {
        return hasPencil;
    }

    public void setHasPencil(List<List<Boolean>> hasPencil) {
        this.hasPencil = hasPencil;
    }

    public Boolean getPuzzleComplete() {
        return isPuzzleComplete;
    }

    public void setPuzzleComplete(Boolean puzzleComplete) {
        isPuzzleComplete = puzzleComplete;
    }

    public List<PuzzleSnapshot> getPuzzleHistory() {
        return puzzleHistory;
    }

    public void setPuzzleHistory(List<PuzzleSnapshot> puzzleHistory) {
        this.puzzleHistory = puzzleHistory;
    }
}
