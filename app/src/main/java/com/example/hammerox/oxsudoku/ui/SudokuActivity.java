package com.example.hammerox.oxsudoku.ui;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hammerox.oxsudoku.R;

public class SudokuActivity extends AppCompatActivity
    implements SudokuFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_sudoku_container, new SudokuFragment())
                    .commit();
        }
        setResult(RESULT_OK);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
