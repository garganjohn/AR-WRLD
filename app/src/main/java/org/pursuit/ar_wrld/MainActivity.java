package org.pursuit.ar_wrld;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import org.pursuit.ar_wrld.login.UserHomeScreenActivity;


public class MainActivity extends AppCompatActivity {
    private SpaceARFragment spaceARFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSpaceFragment();
    }

    private void initSpaceFragment() {
        spaceARFragment = new SpaceARFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.main_activity_container, spaceARFragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MainActivity.this, UserHomeScreenActivity.class);
            startActivity(intent);
            this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        getSupportFragmentManager().beginTransaction().remove(spaceARFragment).commit();
        spaceARFragment = null;
        this.finish();
        super.onStop();
    }
}
