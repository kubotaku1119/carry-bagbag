package spajam2016.haggy.carrybagbag;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import spajam2016.haggy.carrybagbag.fragments.OwakareFragment;
import spajam2016.haggy.carrybagbag.fragments.SearchFragment;
import spajam2016.haggy.carrybagbag.music.ChakuMelo;

public class CarryActivity extends AppCompatActivity implements ServiceConnection, OwakareFragment.OnTargetOwakareSelectedListener{

    private Messenger messenger;
    private AssetFileDescriptor afd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carry);
        Button testPlay = (Button)findViewById(R.id.test_play);

//HACK:テストコード
        final Intent startIntent = CarryService.createStartIntent(this);
        startService(startIntent);
        bindService(startIntent, this, this.BIND_AUTO_CREATE);

        testPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = Message.obtain(null, 1, 0, 0);
                try {
                    messenger.send(msg);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private void initViews(){
        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(OwakareFragment.TAG) == null) {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.owakare_omukae, OwakareFragment.newInstance(), OwakareFragment.TAG);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        messenger = new Messenger(iBinder);

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }

    @Override
    public void Selected(AssetFileDescriptor afd) {
        this.afd = afd;
        new AlertDialog.Builder(this)
                .setMessage("それじゃあ、またね♡")
                .setPositiveButton("またね♡", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();



    }
}
