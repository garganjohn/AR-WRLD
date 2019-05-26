package org.pursuit.ar_wrld.Effects;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.Button;

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
}
