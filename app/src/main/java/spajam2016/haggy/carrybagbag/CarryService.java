package spajam2016.haggy.carrybagbag;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import spajam2016.haggy.carrybagbag.bluetooth.BleWrapper;
import spajam2016.haggy.carrybagbag.bluetooth.CarryGattAttributes;
import spajam2016.haggy.carrybagbag.talk.TalkHandler;
import spajam2016.haggy.carrybagbag.music.ChakuMelo;
import spajam2016.haggy.carrybagbag.util.MyPrefs;

public class CarryService extends Service {

    // 歩き終わり
    private static final int EVENT_OMATASE = 1;

    // 歩き始めの手を伸ばすところ
    private static final int EVENT_ODEKAKE = 2;

    private static final int EVENT_OWAKARE = 3;

    // 音を消すイベント
    private static final int EVENT_OMUKAE = 4;

    private static final int EVENT_PAIN = 5;

    private static final int EVENT_PLEASE_WAIT = 6;

    private static final int EVENT_OHANASHI = 7;

    private static final String TAG = CarryService.class.getSimpleName();

    private BleWrapper bleWrapper;

    private Thread carryWatchThread;

    private Context context;

    private ChakuMelo chakuMelo;

    private Messenger messenger;

    public CarryService() {
        context = this;
    }


    private class IncommingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                stopChakuMelo();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        messenger = new Messenger(new IncommingHandler());
        return messenger.getBinder();
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            if (bleWrapper == null) {
                bleWrapper = BleWrapper.getsInstance(this);
                bleWrapper.initialize();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.stopSelf();
        }
    }

    private boolean isRunning = false;

    @Override
    public void onDestroy() {
        super.onDestroy();

        try {
            if (carryWatchThread != null) {
                carryWatchThread.interrupt();
                isRunning = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            carryWatchThread = null;
        }

        if (bleWrapper != null) {
            bleWrapper.close();
            bleWrapper.terminate();
            bleWrapper = null;
        }

        stopForeground(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.getAction().equals(ACTION_START)) {
            startForeground();

            isRunning = true;
            carryWatchThread = new Thread(new CarryTask());
            carryWatchThread.start();

        } else {
            this.stopSelf();
        }


        return START_STICKY;
    }


    private class CarryTask
            implements Runnable, BleWrapper.IBleScannerListener, BleWrapper.IBleGattListener {

        private boolean isConnected = false;

        private boolean isAitai = false;

        private BluetoothGattCharacteristic targerCharacteristic;

        private BluetoothDevice targetDevice;

        @Override
        public void run() {

            bleWrapper.startScan(this);

            while (isRunning) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (bleWrapper != null) {
                bleWrapper.removeGattListener();
                bleWrapper.stopScan();
            }
        }

        @Override
        public void onConnected(BluetoothDevice device) {
            bleWrapper.discoverServices();
        }

        @Override
        public void onDisconnected(BluetoothDevice device) {
            isConnected = false;
            targerCharacteristic = null;
            bleWrapper.stopScan();
            bleWrapper.startScan(this);
        }

        @Override
        public void onServiceDiscovered(BluetoothDevice device, List<BluetoothGattService> supportedGattServices) {
            for (BluetoothGattService service : supportedGattServices) {
                if (service.getUuid().equals(CarryGattAttributes.UUID_HEART_RATE_SERVICE)) {
                    targerCharacteristic = service.getCharacteristic(CarryGattAttributes.UUID_HEART_RATE_MEASUREMENT);
                    if (targerCharacteristic != null) {
                        bleWrapper.setCharacteristicNotification(targerCharacteristic, true);
                    }
                }
            }
        }

        @Override
        public void onReadCharacteristic(BluetoothDevice device, BluetoothGattCharacteristic characteristic) {
            final int event = BleWrapper.getCarryStatusChangedEvent(characteristic);
            Log.d(TAG, "--- イベント : " + event + " ---");

            // TODO:きゃりーイベント変更処理呼び出し
            onGetCarryStatusChangeEvent(event);
        }

        @Override
        public void onWriteCharacteristic(BluetoothDevice device) {
        }

        @Override
        public void onScanResult(BluetoothDevice device, int rssi, byte[] data) {

            if (device.getName() == null) {
                return;
            }

            Log.d(TAG, "---- Scan BLE device : " + device.getName() + ", (" + rssi + ") ----");

            if (isConnected) {
                return;
            }

            final String address = device.getAddress();
            if (MyPrefs.getTargetCarry(CarryService.this).equals(address)) {
                if (rssi >= -65) {
                    isConnected = true;

                    // 着メロ開始
                    startOmukaeSound();

                    // きゃりーと接続して状態通知待ち
                    bleWrapper.connect(device, this);
                }
            }
        }
    }

    // -----------------------------

    private int oldEvent;

    private void onGetCarryStatusChangeEvent(int event) {
        // TODO;ここできゃりーの状態に応じた処理

        switch (event) {
            case EVENT_OMATASE:
                // お迎えの直後のお待たせは無視する
                if (oldEvent != EVENT_OMUKAE) {
                    doTalkRest();
                }
                break;

            case EVENT_ODEKAKE:
                doTalkStartWalk();
                break;

            case EVENT_OWAKARE:
                break;

            case EVENT_OMUKAE:
                stopOmukaeSound();
                break;

            case EVENT_PAIN:
                doTalkPain();
                break;

            case EVENT_PLEASE_WAIT:
                doTalkPleaseWait();
                break;

            case EVENT_OHANASHI:
                doOhanshi();
                break;
        }

        oldEvent = event;
    }

    private void startOmukaeSound() {
        //HACK:到着メロディを鳴らす
        if (chakuMelo == null) {
            chakuMelo = new ChakuMelo(context);
            chakuMelo.select();
            chakuMelo.play();
        }
    }

    private void stopOmukaeSound() {
        stopChakuMelo();
    }

    private void doTalkRest() {
        final TalkHandler talkHandler = new TalkHandler(this);
        talkHandler.talkDoRest();
    }

    private void doTalkStartWalk() {
        final TalkHandler talkHandler = new TalkHandler(this);
        talkHandler.talkStartWalk();
    }

    private void doTalkPleaseWait() {
        final TalkHandler talkHandler = new TalkHandler(this);
        talkHandler.talkPleaseWait();
    }

    private void doTalkPain() {
        final TalkHandler talkHandler = new TalkHandler(this);
        talkHandler.talkPain();
    }

    private void doOhanshi() {
        final TalkHandler talkHandler = new TalkHandler(this);
        talkHandler.talkRandom(this);
    }


    private void stopChakuMelo() {
        if (chakuMelo != null) {
            if (chakuMelo.isPlaying()) {
                chakuMelo.stop();
                chakuMelo = null;
            }
        }
    }
    // -----------------------------

    private static final int NOTIFY_FOREGROUND_ID = 201607;

    private void startForeground() {
        startForeground(NOTIFY_FOREGROUND_ID, createNotification());
    }

    private Notification createNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(getText(R.string.app_name));
        builder.setContentText("おでかけ中");

        final PendingIntent stopIntent = PendingIntent.getService(this, 0, createStopIntent(this), 0);
        builder.setContentIntent(stopIntent);
//        builder.
        return builder.build();
    }

    // -----------------------------

    private static final String ACTION_START = "action_start";

    private static final String ACTION_STOP = "action_stop";

    public static Intent createStartIntent(Context context) {
        final Intent intent = new Intent(context, CarryService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createStopIntent(Context context) {
        final Intent intent = new Intent(context, CarryService.class);
        intent.setAction(ACTION_STOP);
        return intent;
    }
}
