package spajam2016.haggy.carrybagbag.music;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

/**
 * Created by a-inoue on 2016/07/03.
 * ゴミ
 */
public class Ohanashi {
    private AudioAttributes audioAttributes;
    private SoundPool soundPool;


    public Ohanashi(){
        int poolMax = 10;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool(poolMax, AudioManager.STREAM_MUSIC, 0);
        }
        else {

            audioAttributes = new AudioAttributes.Builder()
                    // USAGE_MEDIA
                    // USAGE_GAME
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    // CONTENT_TYPE_MUSIC
                    // CONTENT_TYPE_SPEECH, etc.
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    // ストリーム数に応じて
                    .setMaxStreams(2)
                    .build();
        }

    }



}
