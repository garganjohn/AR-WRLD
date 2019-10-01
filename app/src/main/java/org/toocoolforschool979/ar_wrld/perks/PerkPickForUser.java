package org.toocoolforschool979.ar_wrld.perks;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.toocoolforschool979.ar_wrld.GameInformation;
import org.toocoolforschool979.ar_wrld.R;
import org.toocoolforschool979.ar_wrld.login.UserHomeScreenActivity;

import java.util.ArrayList;
import java.util.List;

public class PerkPickForUser extends AppCompatActivity implements PerkListener {

    private RecyclerView recyclerView;
    private PerkAdapter perkAdapter;
    private List<PerkInfoModel> perkInfoModelList;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_pick_for_user);
        sharedPreferences = getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);

        recyclerView = findViewById(R.id.perk_recyclerview);
        perkInfoModelList = new ArrayList<>();

        makePerkToAdd(R.string.more_ammo_perk_name, R.string.more_ammo_perk, R.drawable.ammo_perk, GameInformation.MORE_AMMO_PERK);
        makePerkToAdd(R.string.more_time_perk_name, R.string.more_time_perk, R.drawable.more_time_perk_image, GameInformation.MORE_TIME_PERK);
        makePerkToAdd(R.string.more_clocks_perk_name, R.string.more_clocks_perk, R.drawable.slow_time_perk, GameInformation.MORE_CLOCKS);
        makePerkToAdd(R.string.more_damage_perk_name, R.string.more_damage_park, R.drawable.more_damage_perk_image, GameInformation.MORE_DAMAGE_PERK);


        perkAdapter = new PerkAdapter(perkInfoModelList, this);
        recyclerView.setAdapter(perkAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }

    private void makePerkToAdd(int perkName, int perkDesc, int perkImage, String whichPerk) {
        perkInfoModelList.add(new PerkInfoModel(getString(perkName), getString(perkDesc), getDrawable(perkImage), whichPerk));
    }

    @Override
    public void savePerkToSharedPref(String gamePerk) {
        sharedPreferences.edit().putString(GameInformation.GAME_PERK_KEY, gamePerk).apply();
        startActivity(new Intent(this, UserHomeScreenActivity.class));
        this.finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, UserHomeScreenActivity.class));
        super.onBackPressed();
    }
}

