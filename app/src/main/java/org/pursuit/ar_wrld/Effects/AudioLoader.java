package org.pursuit.ar_wrld.Effects;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Button;

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
    private MediaPlayer miss;

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    private MediaPlayer mediaPlayer;

    public AudioLoader(Context context) {
        this.context = context;
    }

    public void setShootingSound(int rawNameSound) {
        shootingSound = MediaPlayer.create(context, rawNameSound);
    }

    public void setPointUp(int rawNameSound){
        pointUp = MediaPlayer.create(context, rawNameSound);
    }

    public void setMiss(int rawNameSound){
        miss = MediaPlayer.create(context, rawNameSound);
    }

    public MediaPlayer getShootingSound() {
        return shootingSound;
    }

    public MediaPlayer getPointUp() {
        return pointUp;
    }

    public MediaPlayer getMiss() {
        return miss;
    }


    public void explodeSound(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(context, R.raw.explosion8bit);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(20f,20f);
        mediaPlayer.start();


    }
    public void laserSound(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(context, R.raw.laserbeam);
        mediaPlayer.seekTo(0);
        mediaPlayer.setVolume(20f,20f);
        mediaPlayer.start();


    }
}
