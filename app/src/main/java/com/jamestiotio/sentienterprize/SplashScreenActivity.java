package com.jamestiotio.sentienterprize;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        Intent intentSplashToMain = new Intent(SplashScreenActivity.this, SignInActivity.class);
        startActivity(intentSplashToMain);
        finish();
    }
}