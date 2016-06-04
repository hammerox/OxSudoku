package com.example.hammerox.oxsudoku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        DificultyFragment dificultyFragment = new DificultyFragment();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.activity_sudoku_container, dificultyFragment)
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
}
