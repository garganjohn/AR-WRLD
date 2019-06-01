package org.pursuit.ar_wrld.viewPager;

import android.content.Context;
import android.content.Intent;
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

public class ViewPagerForClass extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_PARAM3 = "PARAM3";

    // TODO: Rename and change types of parameters
    private String perkInfo;
    private String whichGamePerk;
    private int perkImageRes;
    private TextView textView;
    private ImageView imageView;
    private ViewPagerListener vpl;

    public ViewPagerForClass() {
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
    public static ViewPagerForClass newInstance(String param1, int imageDrawable, String whichPerk) {
        ViewPagerForClass fragment = new ViewPagerForClass();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, imageDrawable);
        args.putString(ARG_PARAM3, whichPerk);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            perkInfo = getArguments().getString(ARG_PARAM1);
            perkImageRes = getArguments().getInt(ARG_PARAM2);
            whichGamePerk = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_pager_for_class, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(GameInformation.SHARED_PREF_KEY, Context.MODE_PRIVATE);

        textView = view.findViewById(R.id.class_description);
        imageView = view.findViewById(R.id.class_image);
        textView.setText(perkInfo);
        imageView.setImageDrawable(ContextCompat.getDrawable(getContext(), perkImageRes));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString(GameInformation.GAME_PERK_KEY, whichGamePerk).apply();
                vpl.goToUserHome();
            }
        });
    }

}
