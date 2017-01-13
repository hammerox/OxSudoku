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
import com.example.hammerox.oxsudoku.utils.Level;


public class LoadingFragment extends Fragment {

    private final int minimumProgressValue = 15;

    private RoundCornerProgressBar progressBar;
    private Level level;
    private String serviceName = PuzzleLoaderService.class.getName();
    private ActivityManager manager;

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
        manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        level = Level.valueOf(getArguments().getString(MainActivity.KEY_LEVEL));

        // If there is already an intentService running, check if the puzzle generating is the
        // same as the user selected
        // If not, kill the service and start a new
        if (findPuzzleLoader()) {
            if (level.fileName.equals(PuzzleLoaderService.mLevelName)) {
                PuzzleLoaderService.userIsWaiting = true;
                Log.v(PuzzleLoaderService.LOG_SERVICE, "PuzzleLoader is now updating progressBar");
            } else {
                getActivity().stopService(new Intent(getActivity(), PuzzleLoaderService.class));
                Log.v(PuzzleLoaderService.LOG_SERVICE, "PuzzleLoader Killed");
                startPuzzleLoader();
            }
        } else {
            // If no service is running, start a new one anyway
            startPuzzleLoader();
        }
    }


    private boolean findPuzzleLoader() {
        // Search
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            // Check
            if (serviceName.equals(service.service.getClassName())) {
                // If found, return true
                Log.v(PuzzleLoaderService.LOG_SERVICE, "Found PuzzleLoader running");
                return true;
            }
        }

        // If not found, return false
        return false;
    }


    private void startPuzzleLoader() {
        Intent intent = new Intent(getActivity(), PuzzleLoaderService.class);
        intent.putExtra(MainActivity.KEY_LEVEL, level.name());
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
