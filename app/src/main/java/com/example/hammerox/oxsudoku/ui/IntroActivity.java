package com.example.hammerox.oxsudoku.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hammerox.oxsudoku.R;

public class IntroActivity extends AppCompatActivity
        implements IntroFragment.OnFragmentInteractionListener {

    public final static long DELAY = 3000L;

    private Handler mHandler;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            startApp();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, DELAY);
    }


    @Override
    public void onIntroInteraction() {
        mHandler.removeCallbacks(mRunnable);
        startApp();
    }


    public void startApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
