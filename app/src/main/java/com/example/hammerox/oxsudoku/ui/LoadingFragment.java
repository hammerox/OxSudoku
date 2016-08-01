package com.example.hammerox.oxsudoku.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.services.PuzzleLoaderService;


public class LoadingFragment extends Fragment {

    private final int minimumProgressValue = 15;
    private RoundCornerProgressBar progressBar;


    public LoadingFragment() {
        // Required empty public constructor
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


    public RoundCornerProgressBar getProgressBar() {
        return progressBar;
    }


    public void setProgressBar(RoundCornerProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
