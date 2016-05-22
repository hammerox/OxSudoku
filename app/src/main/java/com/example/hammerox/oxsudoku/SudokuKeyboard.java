package com.example.hammerox.oxsudoku;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SudokuKeyboard {

    private static int activeKey = 0;

    public SudokuKeyboard() {
    }

/*Todo - Add Undo button*/
/*Todo - Add Pencil button*/
/*Todo - Show counters with remaining numbers*/

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
                    Button pressedKey = (Button)v;
                    int pressedKeyNumber = Integer.valueOf(pressedKey.getText().toString());

                    if (pressedKeyNumber != activeKey) {    // If clicked key is different from active key...
                        // On click, make last active key back to default's appearance.
                        if (activeKey != 0) {       // activeKey = 0 means no key was clicked previously.
                            String idString = "key_" + activeKey;
                            int id = activity.getResources()
                                    .getIdentifier(idString, "id", activity.getPackageName());
                            Button lastKey = (Button) rootView.findViewById(id);
                            int listIndex = activeKey - 1;
                            Boolean isComplete = sudokuGrid.getIsNumberComplete().get(listIndex);
                            if (isComplete) {
                                hideButton(lastKey);
                            } else {
                                showButton(lastKey);
                            }
                            ColorStateList mReleaseColor = ContextCompat
                                    .getColorStateList(activity, R.color.colorMediumGray);
                            ViewCompat.setBackgroundTintList(lastKey, mReleaseColor);
                        }

                        // Changes active key's color.
                        showButton(pressedKey);
                        ColorStateList mPressedColor = ContextCompat
                                .getColorStateList(activity, R.color.colorAccent);
                        ViewCompat.setBackgroundTintList(pressedKey, mPressedColor);
                        activeKey = pressedKeyNumber;

                        // Highlight grid's keys
                        sudokuGrid.updatePuzzleHighlight(activity, activeKey);

                    } else {        // If clicked key is the same as the active key...
                        // change button's color to default, ...
                        ColorStateList mReleaseColor = ContextCompat
                                .getColorStateList(activity, R.color.colorMediumGray);
                        ViewCompat.setBackgroundTintList(pressedKey, mReleaseColor);

                        // hide it if it's complete, ...
                        int listIndex = activeKey - 1;
                        Boolean isComplete = sudokuGrid.getIsNumberComplete().get(listIndex);
                        if (isComplete) hideButton(activity, activeKey);

                        // undo the highlights...
                        sudokuGrid.clearPuzzleHighlight(activity);

                        // and set no active key.
                        activeKey = 0;
                    }
                }
            });
        }
    }

    public void setClickListeners(final Activity activity, final View rootView, final SudokuGrid sudokuGrid) {
        ImageButton leftButton1 = (ImageButton) rootView.findViewById(R.id.left_button_1);
        leftButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageButton leftButton2 = (ImageButton) rootView.findViewById(R.id.left_button_2);
        leftButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ImageButton rightButton1 = (ImageButton) rootView.findViewById(R.id.right_button_1);
        rightButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int wrongCount = 0; int correctCount = 0; int totalCount = 0;
                List<List> wrongArray = new ArrayList<>();
                List<Boolean> puzzleMask = sudokuGrid.getHasSolution();
                List<Boolean> puzzleCorrectAnswers = sudokuGrid.getIsAnswerCorrect();
                List<Boolean> puzzleUserInput = sudokuGrid.getHasUserInput();
                // Compares all user inputs with correct answers. If wrong, it stores the position
                // of the wrong answer into wrongArray. Counters are used to calculate puzzle's
                // progression.
                for (int row = 1; row <= 9; row++) {
                    for (int col = 1; col <= 9; col++) {
                        int index = 9 * (row - 1) + col - 1;
                        if (!puzzleCorrectAnswers.get(index) && puzzleUserInput.get(index)) {
                            List<Integer> toAdd = new ArrayList<>();
                            toAdd.add(row);
                            toAdd.add(col);
                            wrongArray.add(toAdd);
                            wrongCount++;
                        } else if (puzzleCorrectAnswers.get(index) && puzzleUserInput.get(index)) {
                            correctCount++;
                        }
                        if (!puzzleMask.get(index)) totalCount++;
                    }
                }
                // Printing on screen if there are mistakes or not.
                getProgress(activity, wrongCount, correctCount, totalCount);
            }
        });

        ImageButton rightButton2 = (ImageButton) rootView.findViewById(R.id.right_button_2);
        rightButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void getProgress(Activity activity, int wrongCount, int correctCount, int totalCount){
        if (wrongCount != 0) {
            if (wrongCount == 1) {
                Toast.makeText(activity, "There is 1 mistake",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(activity, "There are " + wrongCount + " mistakes",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(activity, "Everything is alright! \n Progress: "
                    + correctCount + "/" + totalCount, Toast.LENGTH_LONG).show();
        }
    }


    public static void showButton(Button key) {
        Drawable mDrawable = key.getBackground();
        mDrawable.setAlpha(255);
    }

    public static void hideButton(Button key) {
        Drawable mDrawable = key.getBackground();
        mDrawable.setAlpha(0);
    }

    public static void showButton(Activity activity, int number) {
        String idString = "key_" + number;
        int id = activity.getResources()
                .getIdentifier(idString, "id", activity.getPackageName());
        Button key = (Button) activity.findViewById(id);
        Drawable mDrawable = key.getBackground();
        mDrawable.setAlpha(255);
    }

    public static void hideButton(Activity activity, int number) {
        String idString = "key_" + number;
        int id = activity.getResources()
                .getIdentifier(idString, "id", activity.getPackageName());
        Button key = (Button) activity.findViewById(id);
        Drawable mDrawable = key.getBackground();
        mDrawable.setAlpha(0);
    }

    public static int getActiveKey() {
        return activeKey;
    }

    public static void setActiveKey(int activeKey) {
        SudokuKeyboard.activeKey = activeKey;
    }
}
