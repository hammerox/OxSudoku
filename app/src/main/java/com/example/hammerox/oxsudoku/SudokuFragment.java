package com.example.hammerox.oxsudoku;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SudokuFragment extends Fragment {

    private SudokuGrid sudokuGrid;

    private OnFragmentInteractionListener mListener;

    public SudokuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sudokuGrid = new SudokuGrid();      // Generates new puzzle
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.sudoku_fragment, container, false);
        sudokuGrid.drawPuzzle(getActivity(), rootView);
        SudokuKeyboard keyboard = new SudokuKeyboard();
        keyboard.drawKeyboard(getActivity(), rootView);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sudoku, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_check:     // 'Check progress' option
                int wrongCount = 0; int correctCount = 0; int totalCount = 0;
                List<List> wrongArray = new ArrayList<>();
                List<Boolean> puzzleMask = sudokuGrid.getPuzzleMask();
                List<Boolean> puzzleCorrectAnswers = sudokuGrid.getPuzzleCorrectAnswers();
                List<Boolean> puzzleUserInput = sudokuGrid.getPuzzleUserInput();
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
                        /*Todo BUG - puzzleMask is changing while playing the game*/
                        if (!puzzleMask.get(index)) totalCount++;
                    }
                }
                // Printing on screen if there are mistakes or not.
                getProgress(wrongCount, correctCount, totalCount);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void getProgress(int wrongCount, int correctCount, int totalCount){
        if (wrongCount != 0) {
            if (wrongCount == 1) {
                Toast.makeText(getActivity(), "There is 1 mistake",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "There are " + wrongCount + " mistakes",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), "Everything is alright! \n Progress: "
                    + correctCount + "/" + totalCount, Toast.LENGTH_LONG).show();
        }
    }
}
