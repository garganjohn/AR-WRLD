package org.pursuit.ar_wrld.viewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import org.pursuit.ar_wrld.R;

import java.util.ArrayList;
import java.util.List;

public class ClassPickForUser extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_pick_for_user);

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(ViewPagerForClass.newInstance("More Ammo", R.drawable.ammo_perk));
        fragmentList.add(ViewPagerForClass.newInstance("Quick Time", R.drawable.clock_perk));
        fragmentList.add(ViewPagerForClass.newInstance("Slow Time", R.drawable.slow_time_perk));
        fragmentList.add(ViewPagerForClass.newInstance("More damage", R.drawable.more_power));

        ViewPager viewPager = findViewById(R.id.mainActivity_viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));
    }
}
