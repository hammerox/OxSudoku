package com.example.hammerox.oxsudoku;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;


public class SudokuFragment extends Fragment {

    private SudokuGrid sudokuGrid;
    static Chronometer chronometer;
    static long mLastStopTime = 0;

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
        Intent intent = getActivity().getIntent();
        int emptyCells = intent.getIntExtra("emptyCells", 0);

        sudokuGrid = new SudokuGrid(emptyCells);      // Generates new puzzle
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*Todo - Adjust fragment for horizontal orientation*/
        View rootView = inflater.inflate(R.layout.fragment_sudoku, container, false);
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

        MenuItem timerItem = menu.findItem(R.id.chronometer);
        chronometer = (Chronometer) MenuItemCompat.getActionView(timerItem);
        chronometer.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_autofill:
                sudokuGrid.takeSnapshot(true);
                sudokuGrid.autoFill(getActivity());
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


    @Override
    public void onResume() {
        if (chronometer != null) {
            Boolean puzzleIsIncomplete = !sudokuGrid.getPuzzleComplete();
            if (puzzleIsIncomplete) {
                chronoStart();
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        chronoPause();
        super.onPause();
    }

    public static void chronoStart()
    {
        // on first start
        if ( mLastStopTime == 0 )
            chronometer.setBase( SystemClock.elapsedRealtime() );
            // on resume after pause
        else
        {
            long intervalOnPause = (SystemClock.elapsedRealtime() - mLastStopTime);
            chronometer.setBase( chronometer.getBase() + intervalOnPause );
        }

        chronometer.start();
    }


    public static void chronoPause()
    {
        chronometer.stop();
        mLastStopTime = SystemClock.elapsedRealtime();
    }
}
