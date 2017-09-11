package com.example.leapfrog.movielisting.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.leapfrog.movielisting.R;


public class SplashActivity extends AppCompatActivity {

    public int DELAY = 3500;
//    public int DELAY = 500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        Thread showsplash = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(DELAY);
                    Intent intent = new Intent(getApplicationContext(), MovieListActivity.class);
                    startActivity(intent);
                } catch (Exception ex) {
                }
            }
        });
        showsplash.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}
