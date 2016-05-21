package com.example.hammerox.oxsudoku;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SudokuFragment extends Fragment {

    private SudokuGrid sudokuGrid;

    private OnFragmentInteractionListener mListener;

    public SudokuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        for (int i = 0; i < 50; i++) {
            // This is only for debugging the puzzle generator
            long start = System.nanoTime();
            SudokuGenerator sdk = new SudokuGenerator();
            List<Integer> puzzle = sdk.board;
            List<Boolean> mask = sdk.mask;
            Boolean isValid = sdk.isValidGame(puzzle);
            long end = System.nanoTime();
            long elapsedInNanos = end - start;
            long elapsedInMilliSeconds = TimeUnit.MILLISECONDS.convert(elapsedInNanos, TimeUnit.NANOSECONDS);
            Log.d("onCreate", "elapsedInMilliSeconds: " + elapsedInMilliSeconds + "ms, isValid: " + isValid.toString());
        }*/

        sudokuGrid = new SudokuGrid();      // Generates new puzzle
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*Todo - Adjust fragment for horizontal orientation*/
        View rootView = inflater.inflate(R.layout.sudoku_fragment, container, false);
        sudokuGrid.drawPuzzle(getActivity(), rootView);

        SudokuKeyboard keyboard = new SudokuKeyboard();
        keyboard.drawKeyboard(getActivity(), rootView, sudokuGrid);
        keyboard.setClickListeners(getActivity(), rootView, sudokuGrid);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sudoku, menu);
        super.onCreateOptionsMenu(menu, inflater);
        /*Todo - Add timer*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_check:
                break;
        }

        /*Todo - Add 'new game' option*/
        /*Todo - Add 'reset game' option*/
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
