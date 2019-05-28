package org.pursuit.ar_wrld.login;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.pursuit.ar_wrld.R;
import org.pursuit.ar_wrld.database.DatabaseActivity;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private Button signInButton;
    private Button createNewAcct;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private EditText inputEmail;
    private EditText inputPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

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

        signInButton = findViewById(R.id.button_login);

        createNewAcct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        button = findViewById(R.id.sign_in_google);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signInButton.setOnClickListener(v -> {
            String email = inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Please enter email id", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (progressBar.getVisibility() == View.GONE) progressBar.setVisibility(View.VISIBLE);


            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignInActivity.this, task -> {


                       if (progressBar.getVisibility() == View.VISIBLE) progressBar.setVisibility(View.GONE);


                        if (task.isSuccessful()) {
                            // there was an error
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(SignInActivity.this, UserHomeScreenActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Log.d(TAG, "singInWithEmail:Fail");
                            Toast.makeText(SignInActivity.this, getString(R.string.failed), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        firebaseAuthListener = firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() != null) {
                startActivity(new Intent(SignInActivity.this, UserHomeScreenActivity.class));
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

    private void signIn() {
        Intent signInIntent =
                Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Intent goToIntent = new Intent(SignInActivity.this, DatabaseActivity.class);
            startActivity(goToIntent);
            Toast.makeText(getApplicationContext(), "Hello " + acct.getDisplayName(), Toast.LENGTH_SHORT).show();
            //statusTextView.setText("Hello, " + acct.getDisplayName());
        } else {
        }
    }
}
