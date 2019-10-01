package org.toocoolforschool979.ar_wrld.login;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import org.toocoolforschool979.ar_wrld.GameInformation;
import org.toocoolforschool979.ar_wrld.R;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private Button signInButton;
    private Button createNewAcct;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "FINDME";
    private static final int RC_SIGN_IN = 9001;

    private EditText inputEmail;
    private EditText inputPassword;
    private TextView errorMessage;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private TextView forgotTextview;

    private SignInButton button;
    FirebaseAuth.AuthStateListener firebaseAuthListener;


    @Override
    protected void onStart() {
        FirebaseApp.initializeApp(this);
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        createNewAcct = findViewById(R.id.sign_in_text);
        progressBar = findViewById(R.id.sign_in_progressbar);
        forgotTextview = findViewById(R.id.forgot_password);
        errorMessage = findViewById(R.id.error_message);

        signInButton = findViewById(R.id.button_login);

        if (firebaseAuth.getCurrentUser() != null) {
            getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE).edit().putString(GameInformation.USERNAME_KEY,firebaseAuth.getCurrentUser().getDisplayName()).apply();
            startActivity(new Intent(SignInActivity.this, UserHomeScreenActivity.class));
            finish();
        }

        forgotTextview.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, ResetPasswordActivity.class)));

        createNewAcct.setOnClickListener(v -> startActivity(new Intent(SignInActivity.this, SignUpActivity.class)));

        signInButton.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();
            Snackbar snackbar = Snackbar.make(errorMessage, "Error Message", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.RED);

            if (TextUtils.isEmpty(email)){
                snackbar.setText("E-mail field is empty");
                snackbar.show();
                return;
            }
            if (TextUtils.isEmpty(password)){
                snackbar.setText("Password field is empty");
                snackbar.show();
                return;
            }

            if (progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignInActivity.this, task -> {

                        if (progressBar.getVisibility() == View.VISIBLE)
                            progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            // there was an error
                            Log.d(TAG, "signInWithEmail:success"+firebaseAuth.getCurrentUser().getDisplayName());
                            getSharedPreferences(GameInformation.SHARED_PREF_KEY, MODE_PRIVATE).edit().putString(GameInformation.USERNAME_KEY, firebaseAuth.getCurrentUser().getDisplayName()).apply();
                            Intent intent = new Intent(SignInActivity.this, UserHomeScreenActivity.class);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            try {
                                throw task.getException();
                            } catch (Exception e){
                                snackbar.setText(e.getMessage());
                                snackbar.show();
                            }

                        }
                    });
        });

        firebaseAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                SignInActivity.this.startActivity(new Intent(SignInActivity.this, UserHomeScreenActivity.class));
            }

        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_text:
                signIn();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result =
                    Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);

    }

    public void signIn() {
        Intent signInIntent =
                Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Intent goToIntent = new Intent(SignInActivity.this, UserHomeScreenActivity.class);
            startActivity(goToIntent);
            Toast.makeText(getApplicationContext(), getString(R.string.hello) + acct.getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.failure_msg) + getString(R.string.try_msg) , Toast.LENGTH_SHORT).show();
        }
    }
}
