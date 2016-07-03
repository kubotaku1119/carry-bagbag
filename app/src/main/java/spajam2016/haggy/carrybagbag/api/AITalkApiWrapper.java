package spajam2016.haggy.carrybagbag.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 人工知能APIラッパークラス
 */
public class AITalkApiWrapper {

    private static final String END_POINT = "https://chatbot-api.userlocal.jp/api/chat?message=%1$s&key=%2$s";

    private OkHttpClient okHttpClient = new OkHttpClient();

    private String apiKey;

    public AITalkApiWrapper(final String apiKey) {
        this.apiKey = apiKey;
    }

    public String getResponseFromAI(final String text) throws IOException, JSONException {

        String url = String.format(Locale.getDefault(), END_POINT, text, apiKey);

        final Request.Builder builder = new Request.Builder().url(url);

        builder.get();

        final Request request = builder.build();

        final Response response = okHttpClient.newCall(request).execute();
        if (response.code() == 200) {
            final String resText = response.body().string();
            final JSONObject jsonObject = new JSONObject(resText);
            if (jsonObject.has("result")) {
                String result = (String) jsonObject.get("result");
                return result;
            }
        }

        return "";
    }

}
