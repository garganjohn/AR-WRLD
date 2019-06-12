package org.pursuit.ar_wrld.tutorial;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import org.pursuit.ar_wrld.R;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends FragmentActivity {
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private TutorialFragment tutorialFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        viewPager = findViewById(R.id.tutorial_viewpager);
        fragmentList.add(TutorialFragment.newInstance(R.drawable.tutorial_background,R.layout.tutorial_page));
        fragmentList.add(TutorialFragment.newInstance(R.drawable.tutorial_secondpage,R.layout.tutorial_noviews));

        viewPager.setAdapter(new TutorialViewAdapter(getSupportFragmentManager(),fragmentList));




    }

    private void initTutorialFragment() {
        TutorialFragment tutorialFragment = new TutorialFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.tutorial_container, tutorialFragment)
                .commit();
    }
}
