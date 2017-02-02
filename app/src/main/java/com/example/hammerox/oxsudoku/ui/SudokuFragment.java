package com.example.hammerox.oxsudoku.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.example.hammerox.oxsudoku.services.PuzzleLoaderService;
import com.example.hammerox.oxsudoku.ui.views.SudokuGrid;
import com.example.hammerox.oxsudoku.ui.views.SudokuKeyboard;
import com.example.hammerox.oxsudoku.utils.FileManager;
import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.services.SudokuGenerator;
import com.example.hammerox.oxsudoku.utils.Level;


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

        SudokuGenerator puzzle = FileManager.loadCurrentPuzzle(getActivity());
        sudokuGrid = new SudokuGrid(getActivity(), puzzle);      // Generates new puzzle
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        /*Todo - Adjust fragment for horizontal orientation*/
        return inflater.inflate(R.layout.fragment_sudoku, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sudokuGrid.drawPuzzle(getActivity(), getView());
        SudokuKeyboard keyboard = new SudokuKeyboard(getActivity(), sudokuGrid);
        keyboard.drawKeyboard(getView());
        keyboard.setSideTools(getView());
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
            case android.R.id.home:
                mListener.onFragmentUpPressed();
                return true;
            case R.id.action_autofill:
                sudokuGrid.takeSnapshot(true);
                sudokuGrid.autoFill(getActivity());
                return true;
        }

        /*Todo - Add 'new game' option*/
        /*Todo - Add 'reset game' option*/
        return super.onOptionsItemSelected(item);
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
        void onFragmentUpPressed();
    }


    @Override
    public void onStart() {
        // Create puzzles for backup while user plays a game
        createBackupPuzzles();
        super.onStart();
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


    public void createBackupPuzzles() {
        boolean backupPuzzleIsNull;

        // Put every level on intentService's queue
        for (Level level : Level.values()) {
            backupPuzzleIsNull = !FileManager.hasSavedPuzzle(getActivity(), level);
            if (backupPuzzleIsNull) {
                storeNewPuzzle(level);
            }
        }
    }


    public static void chronoStart() {
        // on first start
        if ( mLastStopTime == 0 )
            chronometer.setBase( SystemClock.elapsedRealtime() );
            // on resume after pause
        else {
            long intervalOnPause = (SystemClock.elapsedRealtime() - mLastStopTime);
            chronometer.setBase( chronometer.getBase() + intervalOnPause );
        }

        chronometer.start();
    }


    public static void chronoPause() {
        if (chronometer != null) {
            chronometer.stop();
            mLastStopTime = SystemClock.elapsedRealtime();
        }
    }


    public void storeNewPuzzle(Level level) {
        Intent intent = new Intent(getActivity(), PuzzleLoaderService.class);
        intent.putExtra(MainActivity.KEY_LEVEL, level.name());
        intent.putExtra(MainActivity.KEY_USER_IS_WAITING, false);
        getActivity().startService(intent);
    }
}
