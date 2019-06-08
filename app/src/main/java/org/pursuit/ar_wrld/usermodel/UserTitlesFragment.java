package org.pursuit.ar_wrld.usermodel;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.pursuit.ar_wrld.R;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;
import org.pursuit.ar_wrld.perks.ViewPagerListener;

//public class UserTitlesFragment extends Fragment implements ViewPagerListener {
//
//    private ViewPager viewPager;
//
//    public UserTitlesFragment() {
//        // Required empty public constructor
//    }
//
//
//    public static UserTitlesFragment newInstance(String param1, String param2) {
//        UserTitlesFragment fragment = new UserTitlesFragment();
//        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_title_for_user);
//        if (getArguments() != null) {
////            mParam1 = getArguments().getString(ARG_PARAM1);
////            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_user_titles, container, false);
//    }
//    @Override
//    public void goToUserHome() {
//        startActivity(new Intent(this, UserHomeScreenActivity.class));
//        this.finish();
//    }
//
//}
