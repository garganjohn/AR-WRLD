package org.toocoolforschool979.ar_wrld.gameEndsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.toocoolforschool979.ar_wrld.R;

public class GameOverFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private View rootView;
    private TextView messageForUserTv;

    private String messageForUser;

    public GameOverFragment() {
        // Required empty public constructor
    }
    public static GameOverFragment newInstance(String gameOverMessage) {
        GameOverFragment fragment = new GameOverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, gameOverMessage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            messageForUser = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_game_over, container, false);
        messageForUserTv = rootView.findViewById(R.id.game_over_textview);
        messageForUserTv.setText(messageForUser);
        return rootView;
    }
}

