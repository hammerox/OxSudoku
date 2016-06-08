package com.example.hammerox.oxsudoku;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
        implements LoadingFragment.OnFragmentInteractionListener,
        DificultyFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_sudoku_container, new LoadingFragment())
                    .commit();
        }
    }

    public void selectDificulty(View v) {
        Intent intent = new Intent(this, SudokuActivity.class);

        int id = v.getId();
        switch (id) {
            case R.id.dificulty_easy:
                intent.putExtra("emptyCells", 45);
                break;
            case R.id.dificulty_medium:
                intent.putExtra("emptyCells", 55);
                break;
            case R.id.dificulty_hard:
                intent.putExtra("emptyCells", 80);
                break;
        }

        startActivity(intent);
    }

    @Override
    public void openLoader() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.activity_sudoku_container, new LoadingFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void openPuzzle(SudokuGenerator sudokuGenerator) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DificultyFragment dificultyFragment = new DificultyFragment();
        fragmentTransaction.replace(R.id.activity_sudoku_container, dificultyFragment);
        fragmentTransaction.commit();

        Intent intent = new Intent(this, SudokuActivity.class);
        startActivity(intent);
    }
}
