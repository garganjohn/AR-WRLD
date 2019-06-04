package org.pursuit.ar_wrld;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showActionBar();
        initSpaceFragment();
    }

    private void initSpaceFragment() {
    SpaceARFragment spaceARFragment = SpaceARFragment.getInstance(getIntent().getStringExtra(GameInformation.GAME_DIFFICULTY));
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.main_activity_container, spaceARFragment)
                .disallowAddToBackStack()
                .commit();
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
    }

}