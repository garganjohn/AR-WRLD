package org.pursuit.ar_wrld;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.pursuit.ar_wrld.gameEndsFragments.GameOverFragment;
import org.pursuit.ar_wrld.login.UserHomeScreenActivity;
import org.pursuit.ar_wrld.modelObjects.ModelLives;
import org.pursuit.ar_wrld.movement.ModelCoordinates;
import org.pursuit.ar_wrld.movement.MovementNode;
import org.pursuit.ar_wrld.util.ModelLocationIndicator;
import org.pursuit.ar_wrld.weaponsInfo.WeaponsAvailable;

import java.util.ArrayList;
import java.util.Collection;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpaceARFragment extends Fragment {
    private static SpaceARFragment instance;
    private String difficulty;
    public static final String DIFFCIULTY_KEY = "DIFFICULTY";
    private ArFragment arFragment;
    public static final String TAG = "FINDME";
    private AnchorNode sceneNode;
    private ModelLocationIndicator mli;
    private ImageView leftArrow;
    private ImageView rightArrow;
    private TextView scorekeepingTv;
    private TextView msgForUser;
    private TextView countDownText;
    private boolean timerRunning;
    private CountDownTimer countDownTimer;
    private long timeLeftInMilliseconds = 10000;
    int numOfModels = 0;
    private int scoreNumber;
    private int scoreTillClockModel = 2000;
    private String scoreString;
    private String aliensLeftString;
    private String medAmmoCounter;
    private String medDamageCounter;
    private String medDamageString;
    private String medAmmoString;
    private SharedPreferences sharedPreferences;
    private CountDownTimer alienAppearanceRate;
    private Vector3 vector;
    private TextView numOfAliensTv;
    private TextView medWeaponAmmoTv;
    private Hourglass easyAlienSpawn;
    private Hourglass medAlienSpawn;
    private Hourglass hardAlienSpawn;
    private Hourglass startGame;
    private ImageView weakWeapon;
    private ImageView medWeapon;
    private WeaponsAvailable weaponSelection;
    private TextView gameInfoTv;
    private boolean isUserTimeWarned = false;
    private int weaponDamage;
    private boolean isWeakWeaponChosen;
    private boolean isMedWeaponChosen;
    private Animation startFromBottom;
    private Animation exitToBottom;
    private CountDownTimer exitAnimationTimer;
    private CountDownTimer hitChangeColor;
    private CountDownTimer backToOriginalColor;
    private int repitionForColors = 0;
    private AudioLoader audioLoader;
    private View mainActBG;
    private ModelCoordinates modelCoordinates;

    // Controls animation playback.
    private ModelAnimator animator;
    // Index of the current animation playing.
    private MovementNode anchorNode;
    private int nextAnimation;
    private int firstPointThreshold;
    private int startingMedAmmo = 25;
    private int increaseScoreTillClockModelEasy = 5000;
    private int increaseScoreTillClockModelMed = 15000;

    public SpaceARFragment() {
        // Required empty public constructor
    }

    public static SpaceARFragment getInstance(String diff) {
        if (instance == null) {
            Bundle b = new Bundle();
            b.putString(DIFFCIULTY_KEY, diff);
            instance = new SpaceARFragment();
            instance.setArguments(b);
        }
        return instance;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            difficulty = getArguments().getString(DIFFCIULTY_KEY);
        }
        modelCoordinates = new ModelCoordinates();

        sharedPreferences = getActivity().getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE);
        //sharedPreferences = getActivity().getSharedPreferences(UserTitleInformation.TITLE_SHAREDPREF_KEY, MODE_PRIVATE);

        vector = new Vector3();

        // If user misses their shot account here

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_space_ar, container, false);
        setUpAR();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActBG = view.findViewById(R.id.background_for_ar_view);
        findViews(view);
        audioSetup();
        weaponSetup();
        getStringRes();
        setupGameInfo();
        scorekeepingTv.setText(scoreString);
        numOfAliensTv.setText(aliensLeftString);
        medWeaponAmmoTv.setText(medAmmoCounter);
        onTapForMissInteraction();
        if (difficulty.equals(UserHomeScreenActivity.BOSS_LEVEL)) {
            gameInfoPopup(R.string.boss_level, false);
            spawningAliens(true);
        } else {
            gameInfoPopup(R.string.game_intro, false);
            spawningAliens(false);
        }
        applyPerkToUser(sharedPreferences.getString(GameInformation.GAME_PERK_KEY, null));
    }

    private void setUpAR() {
        try {
            arFragment = (ArFragment) getChildFragmentManager().findFragmentById(R.id.sceneform_fragment);
        } catch (NullPointerException n) {
            Log.d(TAG, "setUpAR: " + n.toString());
        }
        arFragment.getPlaneDiscoveryController().hide();
        arFragment.getPlaneDiscoveryController().setInstructionView(null);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdate);

    }

    private void applyPerkToUser(String whichPerk) {
        if (whichPerk == null) {
            return;
        }
        switch (whichPerk) {
            case GameInformation.MORE_AMMO_PERK:
                weaponSelection.setMedWeaponAmmo(startingMedAmmo + (startingMedAmmo / 2));
                setMedAmmoTv();
                break;
            case GameInformation.MORE_DAMAGE_PERK:
                weaponSelection.setMedWeaponDamage(4);
                setMedAmmoTv();
                break;
            case GameInformation.MORE_TIME_PERK:
                startGame.pauseTimer();
                startGame = null;
                timeLeftInMilliseconds += 20000;
                startGameTimer();
                break;
            case GameInformation.SLOW_TIME_PERK:
                increaseScoreTillClockModelEasy = 2500;
                increaseScoreTillClockModelMed = 7500;
                break;
        }
    }

    private void setupGameInfo() {
        startFromBottom = new TranslateAnimation(0, 0, 600f, 0);
        startFromBottom.setDuration(1000);

        exitToBottom = new TranslateAnimation(0, 0, 0, 600f);
        exitToBottom.setDuration(2000);

        startFromBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                exitAnimationTimer.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        exitToBottom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gameInfoTv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        exitAnimationTimer = new CountDownTimer(6000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                gameInfoTv.startAnimation(exitToBottom);
            }
        };

        hitChangeColor = new CountDownTimer(20, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mainActBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.warningColor));
            }

            @Override
            public void onFinish() {
                backToOriginalColor.start();
            }
        };

        backToOriginalColor = new CountDownTimer(20, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mainActBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.neutral_hit));
            }

            @Override
            public void onFinish() {
                repitionForColors++;
                if (repitionForColors < 5)
                    hitChangeColor.start();
                else {
                    repitionForColors = 0;
                    mainActBG.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.neutral_hit));
                }
            }
        };
    }

    private void gameInfoPopup(int stringToDisplay, boolean isWarning) {
        gameInfoTv.setText(stringToDisplay);
        if (gameInfoTv.getVisibility() == View.INVISIBLE) gameInfoTv.setVisibility(View.VISIBLE);
        if (isWarning)
            gameInfoTv.setTextColor(ContextCompat.getColor(getContext(), R.color.warningColor));
        gameInfoTv.startAnimation(startFromBottom);

    }

    private void audioSetup() {
        audioLoader = new AudioLoader(getContext());

    }

    private void onTapForMissInteraction() {
        arFragment.getArSceneView().getScene().setOnTouchListener((hitTestResult, motionEvent) -> {
            if (!isOutOfAmmo() && isMedWeaponChosen) {
                shootMedWeapon();
                setMedAmmoTv();
            }

            if (isMedWeaponChosen && isOutOfAmmo()) {
                isWeakWeaponChosen = true;
                isMedWeaponChosen = false;
                weaponSwitch();
            }

            hitChangeColor.start();

            return false;
        });
    }

    @SuppressLint("StringFormatMatches")
    private void setMedAmmoTv() {
        medDamageString = Integer.toString(weaponSelection.getMedWeaponDamage());
        medAmmoString = Integer.toString(weaponSelection.getMedWeaponAmmo());
        medAmmoCounter = getString(R.string.med_weapon_info, weaponSelection.getMedWeaponAmmo());
        medWeaponAmmoTv.setText(medAmmoCounter);
    }

    private boolean isOutOfAmmo() {
        return weaponSelection.getMedWeaponAmmo() == 0;
    }

    private void shootMedWeapon() {
        weaponSelection.setMedWeaponAmmo(weaponSelection.getMedWeaponAmmo() - 1);
    }

    private void weaponSetup() {
        medWeapon.setAlpha(0.125f);
        setWeaponListener();
        weaponSelection = new WeaponsAvailable(startingMedAmmo);
        weaponDamage = weaponSelection.getWeakWeaponDamage();
    }

    private void weaponSwitch() {
        if (!isWeakWeaponChosen) {
            weakWeapon.setAlpha(0.125f);
        } else {
            weaponDamage = weaponSelection.getWeakWeaponDamage();
            weakWeapon.setAlpha(1f);
        }
        if (!isMedWeaponChosen) {
            medWeapon.setAlpha(0.125f);
        } else {
            weaponDamage = weaponSelection.getMedWeaponDamage();
            medWeapon.setAlpha(1f);
        }
    }

    private void setWeaponListener() {
        weakWeapon.setOnClickListener(v -> {
            isWeakWeaponChosen = true;
            isMedWeaponChosen = false;
            weaponSwitch();
        });

        medWeapon.setOnClickListener(v -> {
            if (weaponSelection.getMedWeaponAmmo() > 0) {
                isMedWeaponChosen = true;
                isWeakWeaponChosen = false;
                weaponSwitch();
            }
        });
    }


    private void findViews(View v) {
        msgForUser = v.findViewById(R.id.msg_for_user);
        countDownText = v.findViewById(R.id.timer_textview);
        scorekeepingTv = v.findViewById(R.id.scorekeeping_textview);
        numOfAliensTv = v.findViewById(R.id.number_of_aliens_textview);
        medWeaponAmmoTv = v.findViewById(R.id.damage_for_med_weapon);
        weakWeapon = v.findViewById(R.id.weak_weapon);
        medWeapon = v.findViewById(R.id.med_weapon);

        rightArrow = v.findViewById(R.id.left_marker);
        leftArrow = v.findViewById(R.id.right_marker);
        mli = new ModelLocationIndicator(rightArrow, leftArrow);

        gameInfoTv = v.findViewById(R.id.game_info_textview);

    }

    private void spawningAliens(boolean isBoss) {

        sceneNode = new AnchorNode();
        sceneNode.setWorldPosition(new Vector3(0, 0, 0));

        if (isBoss) {
            Log.d(TAG, "spawningAliens: ");
            loadModel(Uri.parse(GameInformation.BOSS_ENEMY), GameInformation.BOSS_ENEMY);
        } else {
            final boolean[] isMedEnemyAdded = {false};
            final boolean[] isHardEnemyAdded = {false};

            easyAlienSpawn = new Hourglass(4000, 1000) {
                @Override
                public void onTimerTick(long timeRemaining) {

                }


                @Override
                public void onTimerFinish() {
                    loadModel(Uri.parse(GameInformation.EASY_ENEMY), GameInformation.EASY_ENEMY);

                    easyAlienSpawn.startTimer();

                    if (scoreNumber > 5000 && !isMedEnemyAdded[0]) {
                        isMedEnemyAdded[0] = true;
                        medAlienSpawn.startTimer();
                    }
                }
            };

            medAlienSpawn = new Hourglass(5000, 1000) {
                @Override
                public void onTimerTick(long timeRemaining) {

                }

                @Override
                public void onTimerFinish() {
                    loadModel(Uri.parse(GameInformation.MEDIUM_ENEMY), GameInformation.MEDIUM_ENEMY);
                    medAlienSpawn.startTimer();

                    if (scoreNumber > 10000 && !isHardEnemyAdded[0]) {
                        isHardEnemyAdded[0] = true;
                        hardAlienSpawn.startTimer();
                    }
                }
            };

            hardAlienSpawn = new Hourglass(8000, 1000) {
                @Override
                public void onTimerTick(long timeRemaining) {

                }

                @Override
                public void onTimerFinish() {
                    loadModel(Uri.parse(GameInformation.HARD_ENEMY), GameInformation.HARD_ENEMY);
                    hardAlienSpawn.startTimer();
                }
            };

            switch (difficulty) {
                case UserHomeScreenActivity.EASY_STRING:
                    easyAlienSpawn.startTimer();
                    break;
                case UserHomeScreenActivity.MEDIUM_STRING:
                    medAlienSpawn.startTimer();
                    break;
                case UserHomeScreenActivity.HARD_STRING:
                    hardAlienSpawn.setTime(2000);
                    hardAlienSpawn.startTimer();
                    break;
            }
        }

        startGameTimer();
    }


    @SuppressLint({"StringFormatInvalid", "StringFormatMatches"})
    private void getStringRes() {
        scoreString = getString(R.string.score_text, scoreNumber);
        aliensLeftString = getString(R.string.aliens_remaining_string, numOfModels);
        medAmmoCounter = getString(R.string.med_weapon_info, weaponSelection.getMedWeaponAmmo());
    }

    private void playAnimation(ModelRenderable modelRenderable) {
        if (animator == null || !animator.isRunning()) {
            AnimationData data = modelRenderable.getAnimationData(nextAnimation);
            nextAnimation = (nextAnimation + 1);
            animator = new ModelAnimator(data, modelRenderable);
            animator.start();
        }
    }


    public void onUpdate(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<Plane> planes = frame.getUpdatedTrackables(Plane.class);
        for (Plane plane : planes) {
            if (plane.getTrackingState() == TrackingState.TRACKING) {
                break;
            }
        }
        if (anchorNode != null) {
            ArrayList<Node> overlappedNodes = arFragment.getArSceneView().getScene().overlapTestAll(anchorNode);
            for (Node node : overlappedNodes) {
                if (node instanceof MovementNode) {
                    Toast.makeText(getContext(), "Collision!", Toast.LENGTH_SHORT).show();
                    // May want to use a flag to check that the node wasn't overlapping the previous frame.
                    // Play sound if overlapping started.
                }
            }
        }
    }

    public void addNodeToScene(ModelRenderable renderable, String whichEnemy) {
        numOfModels++;

        switch (difficulty) {
            case UserHomeScreenActivity.EASY_STRING:
                if (numOfModels > 9) {
                    goToResultPage();
                }
            case UserHomeScreenActivity.MEDIUM_STRING:
                if (numOfModels > 6) {
                    goToResultPage();
                }
            case UserHomeScreenActivity.HARD_STRING:
                Log.d(TAG, "setNodeListener: " + numOfModels);
                if (numOfModels > 4) {
                    goToResultPage();
                }
        }

        anchorNode = new MovementNode();
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        getStringRes();
        numOfAliensTv.setText(aliensLeftString);
        node.setRenderable(renderable);

        node.setLocalScale(new Vector3(0.25f, 0.5f, 1.0f));
        node.setParent(anchorNode);
        vector.set(modelCoordinates.randomCoordinates(true), modelCoordinates.randomCoordinates(false), modelCoordinates.randomZCoordinates());

        Quaternion rotate = Quaternion.axisAngle(new Vector3(0, 1f, 0), 90f);

        anchorNode.randomMovement();
        node.setWorldRotation(rotate);
        node.setLocalPosition(vector);
        mli.indicate(vector);

        ModelLives modelLives = new ModelLives();
        boolean isTimerModel = false;

        switch (whichEnemy) {
            case GameInformation.EASY_ENEMY:
                modelLives.setNumofLivesModel0(2);
                break;
            case GameInformation.MEDIUM_ENEMY:
                modelLives.setNumofLivesModel0(3);
                break;
            case GameInformation.HARD_ENEMY:
                modelLives.setNumofLivesModel0(4);
                break;
            case GameInformation.TIME_INCREASE_MODEL:
                modelLives.setNumofLivesModel0(1);
                isTimerModel = true;
                Log.d(TAG, "addNodeToScene: " + node.getLocalScale());
                break;
            case GameInformation.BOSS_ENEMY:
                modelLives.setNumofLivesModel0(30);
                break;
        }

        arFragment.getArSceneView().getScene().addChild(anchorNode);
        //Rotates the model every frame
        //Second parameter in Quaternion.axisAngle() measures speed of rotation
        arFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {
            Quaternion startQ = node.getLocalRotation();
            Quaternion rotateQ = Quaternion.axisAngle(new Vector3(0, 1f, 0), 5f);
            node.setLocalRotation(Quaternion.multiply(startQ, rotateQ));
        });

        setNodeListener(node, anchorNode, modelLives, isTimerModel, whichEnemy);
        playAnimation(renderable);
    }


    private void setNodeListener(TransformableNode node, AnchorNode anchorNode, ModelLives modelLives, boolean isTimerModel, String whichEnemy) {
        node.setOnTapListener(((hitTestResult, motionEvent) -> {

            if (!isOutOfAmmo() && isMedWeaponChosen) {
                shootMedWeapon();
                setMedAmmoTv();
            }

            if (isMedWeaponChosen && isOutOfAmmo()) {
                isWeakWeaponChosen = true;
                isMedWeaponChosen = false;
                weaponSwitch();
            }

            modelLives.setNumofLivesModel0(modelLives.getNumofLivesModel0() - weaponDamage);
            if (0 < modelLives.getNumofLivesModel0()) {
                laserSound();
                Toast.makeText(getContext(), "Lives left: " + modelLives.getNumofLivesModel0(), Toast.LENGTH_SHORT).show();
            } else {
                anchorNode.removeChild(node);
                mli.cancelAnimator();


                switch (whichEnemy) {
                    case GameInformation.EASY_ENEMY:
                        scoreNumber += 1000;
                        break;
                    case GameInformation.MEDIUM_ENEMY:
                        scoreNumber += 2500;
                        break;
                    case GameInformation.HARD_ENEMY:
                        scoreNumber += 5000;
                        break;
                    case GameInformation.BOSS_ENEMY:
                        scoreNumber += 25000;
                        break;
                }

                if (isTimerModel) {
                    Log.d(TAG, "setNodeListener: TIME LEFT BEFORE CHANGE: " + timeLeftInMilliseconds);
                    timeLeftInMilliseconds += 2000;
                    scoreNumber += 500;
                    startGame.pauseTimer();
                    startGame = null;
                    startGameTimer();
                    Log.d(TAG, "setNodeListener: TIME LEFT AFTER CHANGE:" + timeLeftInMilliseconds);
                    Toast.makeText(getContext(), "Time Extended by 5 sec", Toast.LENGTH_SHORT).show();
                }

                if (scoreNumber >= scoreTillClockModel) {
                    if (scoreTillClockModel <= 20000) {
                        scoreTillClockModel += 5000;
                    } else {
                        scoreTillClockModel += 10000;
                    }
                    loadModel(Uri.parse(GameInformation.TIME_INCREASE_MODEL), GameInformation.TIME_INCREASE_MODEL);
                }

                if (scoreNumber >= scoreTillClockModel) {
                    firstPointThreshold = 20000;
                    if (scoreTillClockModel <= firstPointThreshold) {
                        scoreTillClockModel += increaseScoreTillClockModelEasy;
                    } else {
                        scoreTillClockModel += increaseScoreTillClockModelMed;
                    }
                    loadModel(Uri.parse(GameInformation.TIME_INCREASE_MODEL), GameInformation.TIME_INCREASE_MODEL);
                }

                numOfModels--;
                shootSound();
                getStringRes();
                sharedPreferences.edit().putInt(GameInformation.USER_SCORE_KEY, scoreNumber).apply();
                Log.d(TAG, "setNodeListener: " + scoreString);
                Log.d(TAG, "setNodeListener: " + scorekeepingTv.getText().toString());
                scorekeepingTv.setText(scoreString);
                numOfAliensTv.setText(aliensLeftString);

            }
        }));
        node.select();
    }

    public void loadModel(Uri uri, String whichEnemy) {
        ModelRenderable.builder()
                .setSource(getContext(), uri)
                .build()
                .thenAccept(modelRenderable -> {
                    addNodeToScene(modelRenderable, whichEnemy);
                });
        return;
    }

    public void startGameTimer() {
        backgroundMusic();

        startGame = new Hourglass(timeLeftInMilliseconds, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {
                timeLeftInMilliseconds = timeRemaining;
                updateTimer();
                if (timeLeftInMilliseconds < 10000 && !isUserTimeWarned) {
                    isUserTimeWarned = true;
                    gameInfoPopup(R.string.timer_warning, true);
                }
            }

            @Override
            public void onTimerFinish() {
                countDownText.setText("Time's Up");
                stopAudio();
                sharedPreferences.edit().putInt(GameInformation.USER_SCORE_KEY, scoreNumber).apply();
                goToResultPage();
            }
        };
        startGame.startTimer();
    }

    public void updateTimer() {
        int minutes = (int) timeLeftInMilliseconds / 60000;
        int seconds = (int) timeLeftInMilliseconds % 60000 / 1000;

        String timeLeftText;

        timeLeftText = "0" + minutes;
        timeLeftText += ":";
        if (seconds < 10) {
            timeLeftText += "0";
        }
        timeLeftText += seconds;

        countDownText.setText(timeLeftText);
    }

//    public void gameOver(String gameOverMessage) {
//
//        arFragment.onDestroy();
//
//        sharedPreferences.edit().putInt(GameInformation.USER_SCORE_KEY, scoreNumber).apply();
//
//        if (easyAlienSpawn != null && easyAlienSpawn.isRunning()) easyAlienSpawn.pauseTimer();
//        if (medAlienSpawn != null && medAlienSpawn.isRunning()) medAlienSpawn.pauseTimer();
//        if (hardAlienSpawn != null && hardAlienSpawn.isRunning()) hardAlienSpawn.pauseTimer();
//        if (startGame != null && startGame.isRunning()) startGame.pauseTimer();
//
//        GameOverFragment gameOverFragment = GameOverFragment.newInstance(gameOverMessage);
//        //TODO ensure this transaction happens
//        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.left_to_right, R.anim.mid_to_right).replace(R.id.background_for_ar_view, gameOverFragment).commit();
//        new CountDownTimer(5000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                //TODO ensure intent moves to next activity from fragment
//                try {
//                    arFragment.onDestroy();
//                } catch (Exception e) {
//                    Log.d(TAG, "onFinish: " + e.toString());
//                }
//                goToResultPage();
//            }
//        }.start();
//    }

    public void goToResultPage() {
        audioLoader = null;
        instance.onDestroy();
        instance.onDetach();
        instance = null;
        Intent goToResultPageIntent = new Intent(getContext(), ResultPage.class);
        startActivity(goToResultPageIntent);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (easyAlienSpawn != null && easyAlienSpawn.isRunning()) easyAlienSpawn.pauseTimer();
        if (medAlienSpawn != null && medAlienSpawn.isRunning()) medAlienSpawn.pauseTimer();
        if (hardAlienSpawn != null && hardAlienSpawn.isRunning()) hardAlienSpawn.pauseTimer();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (easyAlienSpawn != null && easyAlienSpawn.isPaused()) easyAlienSpawn.resumeTimer();
        if (medAlienSpawn != null && medAlienSpawn.isPaused()) easyAlienSpawn.resumeTimer();
        if (hardAlienSpawn != null && hardAlienSpawn.isPaused()) easyAlienSpawn.resumeTimer();

    }


    public void shootSound() {
        audioSetup();
        audioLoader.explodeSound();
    }

    public void laserSound() {
        audioSetup();
        audioLoader.laserSound();
    }

    public void backgroundMusic() {
        audioSetup();
        audioLoader.backGroundMusic();
    }

    public void stopAudio() {
        audioLoader.stopAudio();
    }

}