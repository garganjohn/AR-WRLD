package org.pursuit.ar_wrld.Effects;

import android.media.MediaPlayer;
import android.widget.Button;

public class AudioLoader {
    final MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);

    Button play_button = (Button)this.findViewById(R.id.button);
        play_button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            Log.v(TAG, "Playing sound...");
            mp.start();
        }
    })
}
