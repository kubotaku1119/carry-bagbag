package spajam2016.haggy.carrybagbag.music;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

import spajam2016.haggy.carrybagbag.R;
import spajam2016.haggy.carrybagbag.util.MyPrefs;

/**
 * Created by a-inoue on 2016/07/03.
 */
public class ChakuMelo {
    private MediaPlayer mediaPlayer;
    private Context context;

    public ChakuMelo(Context context){
        this.context = context;
    }

    public void select(){
        String path = MyPrefs.getSongPath(context);

        try {
            // assetsのファイルをオープン
            AssetFileDescriptor afdescripter = context.getResources().getAssets().openFd(path);

            // MediaPlayer のインスタンス生成
            mediaPlayer = new MediaPlayer();

            // 音楽ファイルをmediaplayerに設定
            mediaPlayer.setDataSource(afdescripter.getFileDescriptor(), afdescripter.getStartOffset(), afdescripter.getLength());
//            mediaPlayer = MediaPlayer.create(context, R.raw.kyary_anan);

            //ループ再生オン
            mediaPlayer.setLooping(true);

            // 再生準備、再生可能状態になるまでブロック
            mediaPlayer.prepare();
        }catch (IOException e){
            e.getStackTrace();
        }
    }

    public void play(){
        // 再生開始
        mediaPlayer.start();

    }

    public void stop(){
        // 再生終了
        mediaPlayer.stop();

        // リセット
        mediaPlayer.reset();

        // リソースの解放
        mediaPlayer.release();
    }

    public boolean isPlaying(){
        if(mediaPlayer == null) return false;
        return mediaPlayer.isPlaying();
    }

}
