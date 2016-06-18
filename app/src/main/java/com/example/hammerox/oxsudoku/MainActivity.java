package com.example.hammerox.oxsudoku;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements LoadingFragment.OnFragmentInteractionListener,
        IntroFragment.OnFragmentInteractionListener {

    public final static String BUNDLE_NAME = "emptyCells";
    public final static int DIFICULTY_EASY = 45;
    public final static int DIFICULTY_MEDIUM = 55;
    public final static int DIFICULTY_HARD = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_sudoku_container, new DificultyFragment())
                    .commit();
        }
    }

    public void selectDificulty(View v) {
        Bundle bundle = new Bundle();
        int id = v.getId();
        switch (id) {
            case R.id.dificulty_easy:
                bundle.putInt(BUNDLE_NAME, DIFICULTY_EASY);
                break;
            case R.id.dificulty_medium:
                bundle.putInt(BUNDLE_NAME, DIFICULTY_MEDIUM);
                break;
            case R.id.dificulty_hard:
                bundle.putInt(BUNDLE_NAME, DIFICULTY_HARD);
                break;
        }
        openLoader(bundle);
    }

    public void openLoader(Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        LoadingFragment loadingFragment = new LoadingFragment();
        loadingFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.activity_sudoku_container, loadingFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void openPuzzle(SudokuGenerator sudokuGenerator) {
        Intent intent = new Intent(this, SudokuActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onIntroInteraction(Uri uri) {
        
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if(requestCode == 0) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DificultyFragment dificultyFragment = new DificultyFragment();
                fragmentTransaction.replace(R.id.activity_sudoku_container, dificultyFragment);
                fragmentTransaction.commit();
            }
        }
    }
}
