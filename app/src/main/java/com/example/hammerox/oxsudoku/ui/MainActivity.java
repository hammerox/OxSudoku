package com.example.hammerox.oxsudoku.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hammerox.oxsudoku.R;
import com.example.hammerox.oxsudoku.services.SudokuGenerator;
import com.example.hammerox.oxsudoku.utils.FileManager;
import com.example.hammerox.oxsudoku.utils.Levels;

public class MainActivity extends AppCompatActivity
        implements LoadingFragment.OnFragmentInteractionListener,
        IntroFragment.OnFragmentInteractionListener {

    public final static String BUNDLE_NAME = "emptyCells";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_sudoku_container, new IntroFragment())
                    .commit();
        }
    }

    public void onLevelButtonClick(View v) {
        Bundle bundle = new Bundle();
        int id = v.getId();
        switch (id) {
            case R.id.level_easy:
                bundle.putInt(BUNDLE_NAME, Levels.LEVEL_EASY);
                break;
            case R.id.level_medium:
                bundle.putInt(BUNDLE_NAME, Levels.LEVEL_MEDIUM);
                break;
            case R.id.level_hard:
                bundle.putInt(BUNDLE_NAME, Levels.LEVEL_HARD);
                break;
        }

        loadPuzzle(bundle);
    }

    public void loadPuzzle(Bundle bundle) {
        String key = Levels.getTag(bundle.getInt(BUNDLE_NAME));

        /* Search for saved puzzle.
        *  If exists, put it into current puzzle and launch game screen.
        *  If not, go to loading screen */
        if (FileManager.hasSavedPuzzle(this, key)) {
            SudokuGenerator sudokuGenerator = FileManager.loadPuzzle(this, key);
            openPuzzle(sudokuGenerator);
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            LoadingFragment loadingFragment = new LoadingFragment();
            loadingFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.activity_sudoku_container, loadingFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void openPuzzle(SudokuGenerator sudokuGenerator) {
        if (sudokuGenerator != null) {
            FileManager.savePuzzle(this, sudokuGenerator, FileManager.CURRENT_PUZZLE);
        }

        Intent intent = new Intent(this, SudokuActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onIntroInteraction() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DifficultyFragment difficultyFragment = new DifficultyFragment();
        fragmentTransaction.replace(R.id.activity_sudoku_container, difficultyFragment);
        fragmentTransaction.commit();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == 0) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DifficultyFragment difficultyFragment = new DifficultyFragment();
                fragmentTransaction.replace(R.id.activity_sudoku_container, difficultyFragment);
                fragmentTransaction.commit();
            }
        }
    }
}
