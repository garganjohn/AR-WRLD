package org.toocoolforschool979.ar_wrld;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.toocoolforschool979.ar_wrld.login.SignInActivity;

import io.supercharge.shimmerlayout.ShimmerLayout;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_LENGTH = 4500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        changeStatusBarColor();
        ShimmerLayout shimmerText = (ShimmerLayout) findViewById(R.id.shimmer_text);
        shimmerText.startShimmerAnimation();
        new Handler().postDelayed(() -> {

            Intent mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(mainIntent);
            SplashActivity.this.finish();
        }, SPLASH_LENGTH);
    }

    private void changeStatusBarColor() {
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.purple_app_color)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.purple_app_color)); //status bar or the time bar at the top
    }


}