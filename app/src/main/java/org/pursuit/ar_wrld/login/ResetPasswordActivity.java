package org.pursuit.ar_wrld.login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.pursuit.ar_wrld.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button resetPasswordButton;
    private TextView logInTextView;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPasswordButton = findViewById(R.id.reset_button);
        logInTextView = findViewById(R.id.login_text);
    }
}
