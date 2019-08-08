package org.pursuit.ar_wrld.Effects;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import org.pursuit.ar_wrld.R;

public class CustomSounds {
    private MediaPlayer backgroundMusic;
    private SoundPool soundPool;
    private int laserSound, explosionSound;
    private Context context;

    public CustomSounds(Context context) {
        this.context = context;
        createBuilder();
        setSounds();
    }

    public MediaPlayer getBackgroundMusic() {
        return backgroundMusic;
    }

    public void setBackgroundMusic(MediaPlayer backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public void setSoundPool(SoundPool soundPool) {
        this.soundPool = soundPool;
    }

    private void createBuilder() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(audioAttributes)
                .build();
    }

    private void setSounds() {
        laserSound = soundPool.load(context, R.raw.laserbeam, 1);
        explosionSound = soundPool.load(context, R.raw.explosion8bit, 1);
        backgroundMusic = MediaPlayer.create(context, R.raw.backgroundmusic);
    }

    public void playLaser() {
        soundPool.play(laserSound, 1, 1, 0, 0, 1);
    }

    public void playExplosion() {
        soundPool.play(explosionSound, 1, 1, 0, 0, 1);
    }



    public void playBackground() {
        backgroundMusic.start();
        backgroundMusic.setLooping(true);
        backgroundMusic.setOnCompletionListener(mp -> {
            backgroundMusic.release();
        });
    }
}
