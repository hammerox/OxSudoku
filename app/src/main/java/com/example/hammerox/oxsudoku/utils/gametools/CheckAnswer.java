package com.example.hammerox.oxsudoku.utils.gametools;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.hammerox.oxsudoku.ui.views.SudokuGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio on 01-Feb-17.
 */

public class CheckAnswer extends GameTool {

    public CheckAnswer(Context context, SudokuGrid sudokuGrid) {
        super(context, sudokuGrid);
    }

    @Override
    public void onClick(View view) {
        int wrongCount = 0;
        int correctCount = 0;
        int totalCount = 0;
        List<Boolean> puzzleMask = getSudokuGrid().getHasSolution();
        List<Boolean> puzzleCorrectAnswers = getSudokuGrid().getIsAnswerCorrect();
        List<Boolean> puzzleUserInput = getSudokuGrid().getHasUserInput();
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
                    wrongCount++;
                } else if (puzzleCorrectAnswers.get(index) && puzzleUserInput.get(index)) {
                    correctCount++;
                }
                if (!puzzleMask.get(index)) totalCount++;
            }
        }
        // Printing on screen if there are mistakes or not.
        getProgress(view.getContext(), wrongCount, correctCount, totalCount);
    }

    private void getProgress(Context context, int wrongCount, int correctCount, int totalCount){
        if (wrongCount != 0) {
            if (wrongCount == 1) {
                Toast.makeText(context, "There is 1 mistake",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "There are " + wrongCount + " mistakes",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "Everything is alright! \n Progress: "
                    + correctCount + "/" + totalCount, Toast.LENGTH_LONG).show();
        }
    }
}
