package com.example.hammerox.oxsudoku.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.services.PuzzleLoaderService;
import com.example.hammerox.oxsudoku.services.SudokuGenerator;
import com.example.hammerox.oxsudoku.utils.FileManager;
import com.example.hammerox.oxsudoku.utils.Level;

public class MainActivity extends AppCompatActivity {

    public final static String KEY_LEVEL = "emptyCells";
    public final static String KEY_USER_IS_WAITING = "userIsWaiting";

    private LoadingFragment loadingFragment;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            boolean loadingIsComplete = intent
                    .getBooleanExtra(PuzzleLoaderService.KEY_IS_COMPLETE, false);
            if (loadingIsComplete) {
                openPuzzle();
            } else {
                try {
                    float percentage = intent.getFloatExtra(PuzzleLoaderService.KEY_UPDATE, 15);
                    RoundCornerProgressBar progressBar = loadingFragment.getProgressBar();
                    progressBar.setProgress(percentage);
                } catch (NullPointerException e) {
                    Log.e("onReceive()", e.getMessage());
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_container, new DifficultyFragment())
                    .commit();
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(PuzzleLoaderService.BROADCAST_SERVICE));

        if (FileManager.hasCurrentPuzzle(this)) {
            openPuzzle();
        }
    }


    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


    public void onLevelButtonClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.level_easy:
                loadPuzzle(Level.EASY);
                break;
            case R.id.level_medium:
                loadPuzzle(Level.MEDIUM);
                break;
            case R.id.level_hard:
                loadPuzzle(Level.HARD);
                break;
            case R.id.level_insane:
                loadPuzzle(Level.HARD);
                break;
        }
    }


    public void loadPuzzle(Level level) {
        /* Search for saved puzzle.
        *  If exists, put it into current puzzle and launch game screen.
        *  If not, go to loading screen */
        if (FileManager.hasSavedPuzzle(this, level)) {
            SudokuGenerator sudokuGenerator = FileManager.loadPuzzle(this, level);
            FileManager.saveCurrentPuzzle(this, sudokuGenerator, level);
            FileManager.clearPuzzle(this, level);
            openPuzzle();
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_LEVEL, level.name());
            launchLoader(bundle);
        }
    }


    public void openPuzzle() {
        Intent intent = new Intent(this, SudokuActivity.class);
        startActivityForResult(intent, 0);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DifficultyFragment difficultyFragment = new DifficultyFragment();
                fragmentTransaction.replace(R.id.activity_main_container, difficultyFragment);
                fragmentTransaction.commit();
            }
        }
    }


    public void launchLoader(Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        loadingFragment = new LoadingFragment();
        loadingFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.activity_main_container, loadingFragment);
        fragmentTransaction.commit();
    }
}
