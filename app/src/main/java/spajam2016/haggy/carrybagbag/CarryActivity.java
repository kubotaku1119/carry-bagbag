package spajam2016.haggy.carrybagbag;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import spajam2016.haggy.carrybagbag.fragments.TsuitayoFragment;
import spajam2016.haggy.carrybagbag.util.MyPrefs;

public class CarryActivity extends AppCompatActivity {

    private boolean owakareState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ActionBarにアイコン表示
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setContentView(R.layout.activity_carry);

        owakareState = MyPrefs.isOwakareState(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (owakareState) {
            // いまついたよ画面を表示する
            showTsuitayoView();
        } else {
            // おわかれ画面を表示する
            showOwakareView();
        }
    }

    private void showOwakareView() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(TsuitayoFragment.TAG) == null) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.view_holder, TsuitayoFragment.newInstance(), TsuitayoFragment.TAG);
            transaction.commit();
        }
    }

    private void showTsuitayoView() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(TsuitayoFragment.TAG) == null) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.view_holder, TsuitayoFragment.newInstance(), TsuitayoFragment.TAG);
            transaction.commit();
        }
    }

}
