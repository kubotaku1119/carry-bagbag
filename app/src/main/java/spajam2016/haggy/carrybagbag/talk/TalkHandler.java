package spajam2016.haggy.carrybagbag.talk;

import android.content.Context;

import org.json.JSONException;

import java.io.IOException;
import java.util.Random;

import spajam2016.haggy.carrybagbag.R;
import spajam2016.haggy.carrybagbag.api.AITalkApiWrapper;
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

    public void talkPain() {
        (new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    speechApi.talk("いったぁー",  SpeechApiWrapper.SAD);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })).start();
    }

    public void talkPleaseWait() {
        (new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    speechApi.talk("まってよー",  SpeechApiWrapper.SAD);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })).start();
    }

    public void talkDoRest() {
        (new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    speechApi.talk("疲れたね。ちょっと休もうか",  SpeechApiWrapper.SAD);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })).start();
    }

    public void talkStartWalk() {
        (new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    speechApi.talk("さぁいこう",  SpeechApiWrapper.SAD);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        })).start();
    }

    private static final String[] TALK_LIST = {
            "こんにちは",
            "おはよう",
            "いい天気だね",
            "お腹すいたね",
            "おでかけ楽しいね",
            "好きだよ"
    };

    public void talkRandom(final Context context) {

        final Random random = new Random();
        int talkIndex = random.nextInt(TALK_LIST.length);
        final String text = TALK_LIST[talkIndex];

        (new Thread(new Runnable() {
            @Override
            public void run() {

                final AITalkApiWrapper aiTalkApiWrapper = new AITalkApiWrapper(context.getString(R.string.ai_bot_api_key));
                try {
                    final String responseFromAI = aiTalkApiWrapper.getResponseFromAI(text);
                    if (!responseFromAI.isEmpty()) {
                        speechApi.talk(responseFromAI, SpeechApiWrapper.HAPPINESS);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        })).start();

    }

}
