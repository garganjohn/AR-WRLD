package org.pursuit.ar_wrld;

import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSpaceFragment();
    }

    private void initSpaceFragment() {
        SpaceARFragment spaceARFragment = new SpaceARFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.main_activity_container, spaceARFragment)
                .disallowAddToBackStack()
                .commit();

    }
}
