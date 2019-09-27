package org.pursuit.ar_wrld.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.pursuit.ar_wrld.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private Button resetPasswordButton;
    private TextView logInTextView;
    private EditText useremailInput;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        firebaseAuth = FirebaseAuth.getInstance();

        resetPasswordButton = findViewById(R.id.reset_button);
        logInTextView = findViewById(R.id.login_text);
        useremailInput = findViewById(R.id.insert_email_to_reset);

        resetPasswordButton.setOnClickListener(v -> {
            String userEmail = useremailInput.getText().toString();

            if (TextUtils.isEmpty(userEmail)) {
                Toast.makeText(ResetPasswordActivity.this, getString(R.string.insert_email_message), Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ResetPasswordActivity.this, getString(R.string.reset_password_text), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResetPasswordActivity.this, SignInActivity.class));
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(ResetPasswordActivity.this, getString(R.string.error_string) + message, Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }
}

