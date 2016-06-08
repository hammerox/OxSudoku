package com.example.hammerox.oxsudoku;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.List;


public class LoadingFragment extends Fragment {

    private final int minimumProgressValue = 15;
    private RoundCornerProgressBar progressBar;

    private OnFragmentInteractionListener mListener;

    public LoadingFragment() {
        // Required empty public constructor
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
        LoadPuzzleTask puzzleLoader = new LoadPuzzleTask();
        puzzleLoader.execute(new SudokuGenerator(50));
        return v;
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

    public class LoadPuzzleTask extends AsyncTask<SudokuGenerator, Float, SudokuGenerator> {
        @Override
        protected SudokuGenerator doInBackground(SudokuGenerator... params) {
            SudokuGenerator sudokuGenerator = params[0];
            sudokuGenerator.preparePuzzle();
            int size = SudokuGenerator.GRID_SIZE;
            for (int i = 0; i < size; i++) {
                sudokuGenerator.tryToRemoveCell(i);
                float completePercentage = 100 * (float) i / (float) size;
                publishProgress(completePercentage);
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
            List<Boolean> fillList = sudokuGenerator.emptyCellList;
            sudokuGenerator.fillToMask(fillList);
            super.onPostExecute(sudokuGenerator);
        }
    }
}
