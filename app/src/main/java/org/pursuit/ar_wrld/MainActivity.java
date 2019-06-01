package org.pursuit.ar_wrld;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ankushgrover.hourglass.Hourglass;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.animation.ModelAnimator;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.AnimationData;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import org.pursuit.ar_wrld.Effects.AudioLoader;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;
import org.pursuit.ar_wrld.modelObjects.ModelLives;
import org.pursuit.ar_wrld.movement.ModelCoordinates;
import org.pursuit.ar_wrld.usermodel.UserTitleInformation;
import org.pursuit.ar_wrld.util.ModelLocationIndicator;
import org.pursuit.ar_wrld.movement.MovementNode;
import org.pursuit.ar_wrld.weaponsInfo.WeaponsAvailable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showActionBar();
        initSpaceFragment();
    }

    private void initSpaceFragment() {
        SpaceARFragment spaceARFragment = SpaceARFragment.getInstance(getIntent().getStringExtra(GameInformation.GAME_DIFFICULTY));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.background_for_ar_view, spaceARFragment)
                .addToBackStack("main")
                .commit();
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
    }
}
