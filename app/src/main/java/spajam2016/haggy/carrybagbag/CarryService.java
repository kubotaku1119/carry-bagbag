package spajam2016.haggy.carrybagbag;

import android.app.Notification;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.List;

import spajam2016.haggy.carrybagbag.bluetooth.BleWrapper;
import spajam2016.haggy.carrybagbag.bluetooth.CarryGattAttributes;
import spajam2016.haggy.carrybagbag.util.MyPrefs;

public class CarryService extends Service {

    private static final String TAG = CarryService.class.getSimpleName();

    private BleWrapper bleWrapper;

    private Thread carryWatchThread;


    public CarryService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
                isConnected = true;
                bleWrapper.connect(device, this);
            }
        }
    }

    // -----------------------------

    private void onGetCarryStatusChangeEvent(int acceleration) {
        // TODO;ここできゃりーの状態に応じた処理
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
        builder.setContentText("テスト中...");
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
