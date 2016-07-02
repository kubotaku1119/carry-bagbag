package spajam2016.haggy.carrybagbag;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import spajam2016.haggy.carrybagbag.util.MyPrefs;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {

                Class clazz;
                if (MyPrefs.isFnishedHelloActivity(SplashActivity.this)) {
                    if (MyPrefs.existTargetCarry(SplashActivity.this)) {
                        // メイン画面へ
                        clazz = CarryActivity.class;
                    } else {
                        // 接続対象検索へ
                        clazz = SearchActivity.class;
                    }
                } else {
                    // 使い方表示へ
                    clazz = HelloActivity.class;
                }

                // TODO:テスト
                MyPrefs.setTargetCarry(SplashActivity.this, null);
                clazz = SearchActivity.class;

                final Intent intent = new Intent(SplashActivity.this, clazz);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
