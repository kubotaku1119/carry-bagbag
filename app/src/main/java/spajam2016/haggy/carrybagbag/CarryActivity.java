package spajam2016.haggy.carrybagbag;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import spajam2016.haggy.carrybagbag.fragments.TsuitayoFragment;
import spajam2016.haggy.carrybagbag.util.MyPrefs;
import spajam2016.haggy.carrybagbag.music.ChakuMelo;

public class CarryActivity extends AppCompatActivity implements ServiceConnection {

    private boolean owakareState;

    private Messenger messenger;

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

    private void setupTestButton() {
//        final Intent startIntent = CarryService.createStartIntent(this);
//        startService(startIntent);
//        bindService(startIntent, this, this.BIND_AUTO_CREATE);
//
//        Button testPlay = (Button)findViewById(R.id.test_play);
//        testPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Message msg = Message.obtain(null, 1, 0, 0);
//                try {
//                    messenger.send(msg);
//                }catch (RemoteException e){
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        messenger = new Messenger(iBinder);

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }


}
