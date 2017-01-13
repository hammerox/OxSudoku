package com.example.hammerox.oxsudoku.ui.views;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.utils.GameTools;


public class SudokuKeyboard {

    private static int activeKey = 0;
    private static Boolean pencilMode = false;
    private static Boolean eraseMode = false;

    private Activity activity;
    private View rootView;
    private SudokuGrid sudokuGrid;
    private float defaultSize;

    public static ColorStateList mColorPrimaryLight;
    public static ColorStateList mColorAccent;
    public static ColorStateList mColorBackground;

    public SudokuKeyboard(Activity activity, View rootView, SudokuGrid sudokuGrid) {
        this.activity = activity;
        this.rootView = rootView;
        this.sudokuGrid = sudokuGrid;
        defaultSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, activity.getResources().getDisplayMetrics());

        mColorAccent = ContextCompat.getColorStateList(activity, R.color.accent);
        mColorPrimaryLight = ContextCompat.getColorStateList(activity, R.color.primary_light);
        mColorBackground = ContextCompat.getColorStateList(activity, R.color.background);
    }


    public void drawKeyboard() {
        // Getting each button from keyboard's view.
        for (int key = 1; key <= 9; key++) {
            String idString = "key_" + key;
            int id = activity.getResources()
                    .getIdentifier(idString, "id", activity.getPackageName());
            Button keyButton = (Button) rootView.findViewById(id);

            // Changing appearance and behavior
            keyButton.setOnClickListener(keyboardListener());
            setKeyAppearance(keyButton, key);
        }
    }


    public void setKeyAppearance(Button keyButton, int key) {
        ColorStateList mColor = ContextCompat.getColorStateList(activity, R.color.primary_light);
        ViewCompat.setBackgroundTintList(keyButton, mColor);
        keyButton.setText(String.valueOf(key));
        keyButton.setGravity(Gravity.CENTER);
        keyButton.setTypeface(Typeface.DEFAULT_BOLD);
        keyButton.setTextSize(defaultSize);
    }


    public void setSideTools() {

        GameTools tools = new GameTools(activity, rootView, sudokuGrid);

        Button leftButton1 = (Button) rootView.findViewById(R.id.left_button_1);
        ViewCompat.setBackgroundTintList(leftButton1, SudokuKeyboard.mColorBackground);
        leftButton1.setOnClickListener(tools.getPencil());

        Button leftButton2 = (Button) rootView.findViewById(R.id.left_button_2);
        ViewCompat.setBackgroundTintList(leftButton2, SudokuKeyboard.mColorBackground);
        leftButton2.setOnClickListener(tools.getEraser());

        Button rightButton1 = (Button) rootView.findViewById(R.id.right_button_1);
        ViewCompat.setBackgroundTintList(rightButton1, SudokuKeyboard.mColorBackground);
        rightButton1.setOnClickListener(tools.getCheckAnswer());

        Button rightButton2 = (Button) rootView.findViewById(R.id.right_button_2);
        ViewCompat.setBackgroundTintList(rightButton2, SudokuKeyboard.mColorBackground);
        rightButton2.setOnClickListener(tools.getUndo());
    }


    public View.OnClickListener keyboardListener() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button pressedKey = (Button)v;
                int pressedKeyNumber = Integer.valueOf(pressedKey.getText().toString());

                if (pressedKeyNumber != activeKey) {    // If clicked key is different from active key...
                    // Make last active key back to default's appearance....
                    if (activeKey != 0) {
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
                        // BUG - Below is the only call that doesn't work correctly on API 21.
                        // See comment inside setButtonTint() for more info.
                        setButtonTint(lastKey, mColorPrimaryLight);
                    }

                    // And changes active key's color.
                    showButton(pressedKey);
                    setButtonTint(pressedKey, mColorAccent);

                    // Highlight grid's keys
                    sudokuGrid.showPencilHighligh(activity, activeKey, pressedKeyNumber);
                    sudokuGrid.showHighlight(activity, pressedKeyNumber);

                    activeKey = pressedKeyNumber;

                } else {        // If clicked key is the same as the active key...
                    // change button's color to default, ...
                    setButtonTint(pressedKey, mColorPrimaryLight);

                    // hide it if it's complete, ...
                    int listIndex = activeKey - 1;
                    Boolean isComplete = sudokuGrid.getIsNumberComplete().get(listIndex);
                    if (isComplete) hideButton(activity, activeKey);

                    // undo the highlights...
                    sudokuGrid.clearPencilHighlight(activity, activeKey);
                    sudokuGrid.clearPuzzleHighlight(activity);

                    // and set no active key.
                    activeKey = 0;
                }
            }
        };
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

    public static void setButtonTint(Button button, ColorStateList tint) {
        /* For some reason, setBackgroundTintList() does not work correctly on API 21.
         * Some calls to it works OK, but on a few it does not apply tint until something is triggered.
         * I found out two triggers to get it to work:
         * It works when onPause() is called or when the button toggle setEnabled().
         * The only call that does not work on this program is commented beside setButtonTint().
         */
        ViewCompat.setBackgroundTintList(button, tint);
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
            button.setEnabled(false);
            button.setEnabled(true);
        }
    }


    public static int getActiveKey() {
        return activeKey;
    }

    public static void setActiveKey(int activeKey) {
        SudokuKeyboard.activeKey = activeKey;
    }

    public static Boolean getEraseMode() {
        return eraseMode;
    }

    public static void setEraseMode(Boolean eraseMode) {
        SudokuKeyboard.eraseMode = eraseMode;
    }

    public static Boolean getPencilMode() {
        return pencilMode;
    }

    public static void setPencilMode(Boolean pencilMode) {
        SudokuKeyboard.pencilMode = pencilMode;
    }
}
