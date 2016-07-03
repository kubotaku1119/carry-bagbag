package spajam2016.haggy.carrybagbag.talk;

import android.content.Context;

import java.io.IOException;

import spajam2016.haggy.carrybagbag.R;
import spajam2016.haggy.carrybagbag.api.SpeechApiWrapper;

/**
 * きゃりーのおしゃべりを制御する
 */
public class TalkHandler {

    private final SpeechApiWrapper speechApi;

    public TalkHandler(Context context) {
        speechApi = new SpeechApiWrapper(context.getString(R.string.text_api_key));
    }

    public void talkGoodbye() {
        (new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    speechApi.talk("またね",  SpeechApiWrapper.SAD);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })).start();
    }

    public void talkRandom() {

    }

}
