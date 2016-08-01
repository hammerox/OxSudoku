package com.example.hammerox.oxsudoku.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.services.PuzzleLoaderService;
import com.example.hammerox.oxsudoku.services.SudokuGenerator;


public class LoadingFragment extends Fragment {

    private final int minimumProgressValue = 15;
    private RoundCornerProgressBar progressBar;

    /*Todo - Remove LoadingFragment listener*/
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

        int level = getArguments().getInt(MainActivity.KEY_LEVEL);

        Intent intent = new Intent(getActivity(), PuzzleLoaderService.class);
        intent.putExtra(MainActivity.KEY_LEVEL, level);
        intent.putExtra(MainActivity.KEY_USER_IS_WAITING, true);
        getActivity().startService(intent);

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


    public RoundCornerProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(RoundCornerProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
