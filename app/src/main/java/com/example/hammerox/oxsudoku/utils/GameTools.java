package com.example.hammerox.oxsudoku.utils;

import android.app.Activity;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hammerox.oxsudoku.ui.views.SudokuGrid;
import com.example.hammerox.oxsudoku.ui.views.SudokuKeyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 04-Aug-16.
 */
public class GameTools {

    private float defaultSize;
    private float smallSize;

    private Activity activity;
    private View rootView;
    private SudokuGrid sudokuGrid;

    public GameTools(Activity activity, View rootView, SudokuGrid sudokuGrid) {
        this.activity = activity;
        this.rootView = rootView;
        this.sudokuGrid = sudokuGrid;

        defaultSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, activity.getResources().getDisplayMetrics());
        smallSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                10, activity.getResources().getDisplayMetrics());
    }


    public View.OnClickListener getPencil() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean pencilMode = SudokuKeyboard.getPencilMode();
                boolean eraseMode = SudokuKeyboard.getEraseMode();

                pencilMode = !pencilMode;
                SudokuKeyboard.setPencilMode(pencilMode);
                for (int i = 1; i <= 9; i++) {
                    String idString = "key_" + i;
                    int id = activity.getResources()
                            .getIdentifier(idString, "id", activity.getPackageName());
                    Button keyButton = (Button) rootView.findViewById(id);
                    if (pencilMode) {
                        if (!eraseMode) {
                            keyButton.setTextColor(Color.BLUE);
                        }
                        keyButton.setTextSize(smallSize);

                    } else {
                        if (!eraseMode) {
                            keyButton.setTextColor(Color.BLACK);
                        }
                        keyButton.setTextSize(defaultSize);
                    }
                }
            }
        };
    }


    public View.OnClickListener getEraser() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean pencilMode = SudokuKeyboard.getPencilMode();
                boolean eraseMode = SudokuKeyboard.getEraseMode();

                eraseMode = !eraseMode;
                SudokuKeyboard.setEraseMode(eraseMode);
                for (int i = 1; i <= 9; i++) {
                    String idString = "key_" + i;
                    int id = activity.getResources()
                            .getIdentifier(idString, "id", activity.getPackageName());
                    Button keyButton = (Button) rootView.findViewById(id);
                    if (eraseMode) {
                        keyButton.setTextColor(Color.WHITE);
                    } else {
                        if (pencilMode) {
                            keyButton.setTextColor(Color.BLUE);
                        } else {
                            keyButton.setTextColor(Color.BLACK);
                        }
                    }
                }
            }
        };
    }


    public View.OnClickListener getCheckAnswer() {
        return new View.OnClickListener() {
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
        };
    }


    public View.OnClickListener getUndo() {
            return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sudokuGrid.undoLastInput(activity);
            }
        };
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
}
