package com.example.moviemania;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int SPLASH_TIME = 3900;  //Delay time before going to the second activity.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        //To create the new intent to show the loading page
        new Handler().postDelayed(() -> {
            Intent homeIntent = new Intent(MainActivity.this,MenuActivity.class);
            startActivity(homeIntent);
            finish();
        },SPLASH_TIME);
    }
}