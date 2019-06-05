package org.pursuit.ar_wrld.Effects;

import android.content.Context;
import android.media.MediaPlayer;


import org.pursuit.ar_wrld.R;

/**
 * MediaPlayer.create needs context, and R.raw.nameofsound
 * Store all audio in raw folder, for now use MP3
 * Example of how to make audio sound,
 * EX: getShootingSound().start
 */

public class AudioLoader {
    private Context context;
    private MediaPlayer shootingSound;
    private MediaPlayer pointUp;
    private MediaPlayer explosionSound;
    private MediaPlayer miss;
    private MediaPlayer mediaPlayer;

    public AudioLoader(Context context) {
        this.context = context;
    }

    public MediaPlayer getShootingSound() {
        return shootingSound;
    }

    public void laserSound() {
        mediaPlayer = MediaPlayer.create(context, R.raw.laserbeam);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(20f, 20f);
        mediaPlayer.start();
    }

    public void backGroundMusic() {
        mediaPlayer = MediaPlayer.create(context, R.raw.backgroundmusic);
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(20f, 20f);
        mediaPlayer.start();
    }

    public void explosionSound(){
        mediaPlayer = MediaPlayer.create(context, R.raw.explosion8bit);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(20f, 20f);
        mediaPlayer.start();
    }
  
    public void stopAudio(){
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    public void nullMediaPlayer(){
        mediaPlayer = null;
    }
}
