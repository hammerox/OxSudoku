package com.example.hammerox.oxsudoku.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.services.PuzzleLoaderService;


public class LoadingFragment extends Fragment {

    private final int minimumProgressValue = 15;
    private RoundCornerProgressBar progressBar;
    String serviceName = PuzzleLoaderService.class.getName();
    ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);

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

        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // If there is already an intentService running, stop and kill it
        if (findPuzzleLoader()) {
            PuzzleLoaderService.shouldStop = true;
            Log.v(PuzzleLoaderService.LOG_SERVICE, "Trying to kill PuzzleLoader");

            // This ensures that the intentService is killed before continuing
            while (findPuzzleLoader()) {}
        }

        // Start generating a new puzzle
        startPuzzleLoader();
    }


    private boolean findPuzzleLoader() {
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    private void startPuzzleLoader() {
        int level = getArguments().getInt(MainActivity.KEY_LEVEL);

        Intent intent = new Intent(getActivity(), PuzzleLoaderService.class);
        intent.putExtra(MainActivity.KEY_LEVEL, level);
        intent.putExtra(MainActivity.KEY_USER_IS_WAITING, true);
        getActivity().startService(intent);
    }


    public RoundCornerProgressBar getProgressBar() {
        return progressBar;
    }


    public void setProgressBar(RoundCornerProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
