package org.pursuit.ar_wrld;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import org.pursuit.ar_wrld.login.SignInActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        hideActionBar();
        changeStatusBarColor();

        new Handler().postDelayed(() -> {
            Intent mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }, SPLASH_LENGTH);
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void changeStatusBarColor() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); //Makes both status and navbar transparent
    }
}