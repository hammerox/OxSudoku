package com.example.hammerox.oxsudoku.ui;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hammerox.oxsudoku.R;

public class SudokuActivity extends AppCompatActivity
    implements SudokuFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_sudoku_container, new SudokuFragment())
                    .commit();
        }
        setResult(RESULT_OK);
    }

    @Override
    public void onFragmentUpPressed() {
        showExitDialog();
    }


    @Override
    public void onBackPressed() {
        showExitDialog();
    }


    public void showExitDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Your game will be counted as a defeat")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {

                        SudokuActivity.super.onBackPressed();
                    }
                }).create().show();
    }
}
