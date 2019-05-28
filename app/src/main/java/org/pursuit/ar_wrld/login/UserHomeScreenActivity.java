package org.pursuit.ar_wrld.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import org.pursuit.ar_wrld.MainActivity;
import org.pursuit.ar_wrld.R;

public class UserHomeScreenActivity extends AppCompatActivity {

    private Spinner levelSpinner;
    private Button practiceButton;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home_screen);

        levelSpinner = findViewById(R.id.level_spinner);
        practiceButton = findViewById(R.id.practice_button);
        playButton = findViewById(R.id.play_button);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinnerValue = levelSpinner.getSelectedItem().toString();
                startActivity(new Intent(UserHomeScreenActivity.this, MainActivity.class));

            }
        });
    }
}
