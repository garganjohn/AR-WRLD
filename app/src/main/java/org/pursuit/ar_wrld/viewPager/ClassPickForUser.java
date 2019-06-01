package org.pursuit.ar_wrld.viewPager;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import org.pursuit.ar_wrld.GameInformation;
import org.pursuit.ar_wrld.R;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;

import java.util.ArrayList;
import java.util.List;

public class ClassPickForUser extends FragmentActivity implements ViewPagerListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_pick_for_user);

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(ViewPagerForClass.newInstance(getResources().getString(R.string.more_ammo), R.drawable.ammo_perk, GameInformation.MORE_AMMO_PERK));
        fragmentList.add(ViewPagerForClass.newInstance(getResources().getString(R.string.quick_time), R.drawable.clock_perk, GameInformation.MORE_TIME_PERK));
        fragmentList.add(ViewPagerForClass.newInstance(getResources().getString(R.string.slow_time), R.drawable.slow_time_perk, GameInformation.SLOW_TIME_PERK));
        fragmentList.add(ViewPagerForClass.newInstance(getResources().getString(R.string.more_damage), R.drawable.more_power, GameInformation.MORE_DAMAGE_PERK));

        ViewPager viewPager = findViewById(R.id.mainActivity_viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList));
    }

    @Override
    public void goToUserHome() {
        startActivity(new Intent(this, UserHomeScreenActivity.class));
    }
}
