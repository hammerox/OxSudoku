package com.example.hammerox.oxsudoku;

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
        int id = v.getId();
        switch (id) {
            case R.id.dificulty_easy:
                break;
            case R.id.dificulty_medium:
                break;
            case R.id.dificulty_hard:
                break;
        }
    }
}
