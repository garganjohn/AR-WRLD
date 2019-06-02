package org.pursuit.ar_wrld.gameEndsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.pursuit.ar_wrld.R;

public class GameOverFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private View rootView;
    private TextView messageForUserTv;

    // TODO: Rename and change types of parameters
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
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_game_over, container, false);
        messageForUserTv = rootView.findViewById(R.id.game_over_textview);
        messageForUserTv.setText(messageForUser);
        return rootView;
    }
}
