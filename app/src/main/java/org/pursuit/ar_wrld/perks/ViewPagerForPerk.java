package org.pursuit.ar_wrld.perks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.pursuit.ar_wrld.GameInformation;
import org.pursuit.ar_wrld.R;

public class ViewPagerForPerk extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PERK_DESCRIPTION_KEY = "param1";
    private static final String IMAGE_DRAWABLE_KEY = "param2";
    private static final String WHICH_PERK_KEY = "PARAM3";
    private static final String PERK_NAME_KEY = "PERK NAME";

    // TODO: Rename and change types of parameters
    private String perkInfo;
    private String whichGamePerk;
    private String perkName;
    private int perkImageRes;
    private TextView perkNameTv, perkDescriptionTv, levelOneTv, levelTwoTv, levelThreeTv, levelFourTv,levelOneExpTv,levelTwoExpTv,levelThreeExpTv,levelFourExpTv;
    private ImageView imageView;
    private ViewPagerListener vpl;
    private View rootView;
    private int levelCurrentExp;
    private int level1xpMax = 10000;
    private int level2xpMax = 25000;
    private int level3xpMax = 50000;
    private int level4xpMax = 100000;
    private float lockedPerk = 0.600f;


    public ViewPagerForPerk() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null){
            vpl = (ViewPagerListener) context;
        }
    }

    // TODO: Rename and change types and number of parameters
    public static ViewPagerForPerk newInstance(String perkDescription, int imageDrawable, String whichPerk, String perkName) {
        ViewPagerForPerk fragment = new ViewPagerForPerk();
        Bundle args = new Bundle();
        args.putString(PERK_DESCRIPTION_KEY, perkDescription);
        args.putInt(IMAGE_DRAWABLE_KEY, imageDrawable);
        args.putString(WHICH_PERK_KEY, whichPerk);
        args.putString(PERK_NAME_KEY, perkName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            perkInfo = getArguments().getString(PERK_DESCRIPTION_KEY);
            perkImageRes = getArguments().getInt(IMAGE_DRAWABLE_KEY);
            whichGamePerk = getArguments().getString(WHICH_PERK_KEY);
            perkName = getArguments().getString(PERK_NAME_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_view_pager_for_class, container, false);
        findViews();
        return rootView;
    }

    private void findViews() {
        levelOneTv = rootView.findViewById(R.id.perk_level_1);
        levelTwoTv = rootView.findViewById(R.id.perk_level_2);
        levelThreeTv = rootView.findViewById(R.id.perk_level_3);
        levelFourTv = rootView.findViewById(R.id.perk_level_4);

        levelOneExpTv = rootView.findViewById(R.id.perk_level_1_exp);
        levelTwoExpTv = rootView.findViewById(R.id.perk_level_2_exp);
        levelThreeExpTv = rootView.findViewById(R.id.perk_level_3_exp);
        levelFourExpTv = rootView.findViewById(R.id.perk_level_4_exp);

        perkNameTv = rootView.findViewById(R.id.perk_name);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(GameInformation.SHARED_PREF_KEY, Context.MODE_PRIVATE);

        perkDescriptionTv = view.findViewById(R.id.perk_description);
        imageView = view.findViewById(R.id.perk_image);
        perkDescriptionTv.setText(perkInfo);
        imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), perkImageRes));
        setLevelDescriptionBasedOnPerk();
        perkNameTv.setText(perkName);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString(GameInformation.GAME_PERK_KEY, whichGamePerk).apply();
                vpl.goToUserHome();
            }
        });
    }

    public void setLevelDescriptionBasedOnPerk(){
        switch (whichGamePerk){
            case GameInformation.MORE_TIME_PERK:
                levelCurrentExp = 7832;
                setLevelDescription(getString(R.string.more_time_level_1), getString(R.string.more_time_level_2), getString(R.string.more_time_level_3), getString(R.string.more_time_level_4));
                break;
            case GameInformation.MORE_DAMAGE_PERK:
                levelCurrentExp = 6323;
                setLevelDescription(getString(R.string.more_damage_level_1), getString(R.string.more_damage_level_2), getString(R.string.more_damage_level_3), getString(R.string.more_damage_level_4));
                break;
            case GameInformation.MORE_AMMO_PERK:
                levelCurrentExp = 9854;
                setLevelDescription(getString(R.string.more_ammo_level_1), getString(R.string.more_ammo_level_2), getString(R.string.more_ammo_level_3), getString(R.string.more_ammo_level_4));
                break;
            case GameInformation.SLOW_TIME_PERK:
                levelCurrentExp = 232;
                setLevelDescription(getString(R.string.slow_time_level_1), getString(R.string.slow_time_level_2), getString(R.string.slow_time_level_3), getString(R.string.slow_time_level_4));
                break;

        }
    }

    private void setLevelDescription(String level1, String level2, String level3, String level4){
        levelOneTv.setText(level1);
        levelOneExpTv.setText(getString(R.string.level_exp, levelCurrentExp, level1xpMax));
        levelTwoTv.setText(level2);
        levelTwoExpTv.setText(getString(R.string.level_exp,levelCurrentExp, level2xpMax));
        levelThreeTv.setText(level3);
        levelThreeExpTv.setText(getString(R.string.level_exp,levelCurrentExp, level3xpMax));
        levelFourTv.setText(level4);
        levelFourExpTv.setText(getString(R.string.level_exp,levelCurrentExp, level4xpMax));

        if (levelCurrentExp < level1xpMax){
            levelOneTv.setAlpha(lockedPerk);
        }
        if (levelCurrentExp < level3xpMax){
            levelTwoTv.setAlpha(lockedPerk);
        }
        if (levelCurrentExp < level3xpMax){
            levelThreeTv.setAlpha(lockedPerk);
        }
        if (levelCurrentExp < level4xpMax){
            levelFourTv.setAlpha(lockedPerk);
        }
    }


}
