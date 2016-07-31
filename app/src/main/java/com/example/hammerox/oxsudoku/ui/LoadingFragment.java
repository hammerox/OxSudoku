package com.example.hammerox.oxsudoku.ui;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.hammerox.oxsudoku.utils.FileManager;
import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.services.SudokuGenerator;

import java.util.List;


public class LoadingFragment extends Fragment {

    private final int minimumProgressValue = 15;
    private RoundCornerProgressBar progressBar;

    private OnFragmentInteractionListener mListener;

    public LoadingFragment() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_loading, container, false);
        progressBar = (RoundCornerProgressBar) v.findViewById(R.id.loading_progressbar);
        progressBar.setProgress(minimumProgressValue);

        int level = getArguments().getInt(MainActivity.BUNDLE_NAME);
        LoadPuzzleTask puzzleLoader = new LoadPuzzleTask();
        puzzleLoader.execute(level);
        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void openPuzzle(SudokuGenerator sudokuGenerator);
    }

    public class LoadPuzzleTask extends AsyncTask<Integer, Float, SudokuGenerator> {
        @Override
        protected SudokuGenerator doInBackground(Integer... params) {
            SudokuGenerator sudokuGenerator = new SudokuGenerator(params[0]);
            sudokuGenerator.preparePuzzle();
            int size = SudokuGenerator.GRID_SIZE;
            for (int i = 0; i < size; i++) {
                sudokuGenerator.tryToRemoveCell(i);
                float completePercentage = 100 * (float) i / (float) size;
                publishProgress(completePercentage);
                if (sudokuGenerator.getEmptyCellsCounter() == sudokuGenerator.getMaxEmptyCells()) {
                    break;                                  // Stop if the number of empty cells is enough...
                }
            }
            return sudokuGenerator;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            if (values[0] >= (float)minimumProgressValue) {
                progressBar.setProgress(values[0]);
            }
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(SudokuGenerator sudokuGenerator) {
            progressBar.setProgress(100);
            List<Boolean> fillList = sudokuGenerator.getEmptyCellList();
            sudokuGenerator.fillToMask(fillList);

            mListener.openPuzzle(sudokuGenerator);
            super.onPostExecute(sudokuGenerator);
        }
    }
}
