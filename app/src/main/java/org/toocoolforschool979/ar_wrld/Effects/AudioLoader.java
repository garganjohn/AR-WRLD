package org.toocoolforschool979.ar_wrld.Effects;

import android.content.Context;
import android.media.MediaPlayer;


import org.toocoolforschool979.ar_wrld.R;

/**
 * MediaPlayer.create needs context, and R.raw.nameofsound
 * Store all audio in raw folder, for now use MP3
 * Example of how to make audio sound,
 * EX: getShootingSound().start
 */

public class AudioLoader {
    private Context context;
    private MediaPlayer laserSound;
    private MediaPlayer explosionSound;
    private MediaPlayer backgroundMusic;

    public AudioLoader(Context context) {
        this.context = context;
    }

    public void laserSound() {
        laserSound = MediaPlayer.create(context, R.raw.laserbeam);
        laserSound.seekTo(0);
        laserSound.setVolume(20f, 20f);
        laserSound.start();
    }

    public void backGroundMusic() {
        backgroundMusic = MediaPlayer.create(context, R.raw.backgroundmusic);
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(20f, 20f);
        backgroundMusic.start();
    }

    public void explosionSound(){
        explosionSound = MediaPlayer.create(context, R.raw.explosion8bit);
        explosionSound.seekTo(0);
        explosionSound.setVolume(20f, 20f);
        explosionSound.start();
    }
  
    public void stopAudio(){
        backgroundMusic.stop();
        backgroundMusic.release();
    }

    public void nullMediaPlayer(){
        backgroundMusic = null;
    }
}

