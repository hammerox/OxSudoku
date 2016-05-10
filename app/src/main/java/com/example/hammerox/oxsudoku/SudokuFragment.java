package com.example.hammerox.oxsudoku;

import android.app.Fragment;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hammerox.oxsudoku.Tools.MetricConverter;

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
        sudokuGrid = new SudokuGrid();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.sudoku_fragment, container, false);
        sudokuGrid.createGrid(getActivity(), rootView);
        SudokuKeyboard keyboard = new SudokuKeyboard();
        keyboard.createKeyboard(getActivity(), rootView);

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
            case R.id.action_check:
                List<Boolean> puzzleMask = sudokuGrid.getPuzzleMask();
                List<Boolean> puzzleCorrectAnswers = sudokuGrid.getPuzzleCorrectAnswers();
                List<Boolean> puzzleUserInput = sudokuGrid.getPuzzleUserInput();

                int total = 0;
                int correctCount = 0;
                int wrongCount = 0;
                List<List> wrongArray = new ArrayList<>();
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
                        if (!puzzleMask.get(index)) total++;
                    }
                }

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
                            + correctCount + "/" + total, Toast.LENGTH_LONG).show();
                }

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
}
