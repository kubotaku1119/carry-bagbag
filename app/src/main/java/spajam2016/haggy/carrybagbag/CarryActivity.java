package spajam2016.haggy.carrybagbag;

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

import spajam2016.haggy.carrybagbag.music.ChakuMelo;

public class CarryActivity extends AppCompatActivity implements ServiceConnection {

    private Messenger messenger;
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
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        messenger = new Messenger(iBinder);

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }
}
