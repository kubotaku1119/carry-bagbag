package spajam2016.haggy.carrybagbag;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import spajam2016.haggy.carrybagbag.bluetooth.BleWrapper;
import spajam2016.haggy.carrybagbag.fragments.SearchFragment;
import spajam2016.haggy.carrybagbag.util.MyPrefs;

/**
 * 接続先きゃりー選択画面
 */
public class SearchActivity extends AppCompatActivity
        implements SearchFragment.OnTargetCarrySelectedListener {

    /**
     * リクエストコード：Bluetoothの有効化
     */
    private static final int REQUEST_ENABLE_BT = 100;

    /**
     * リクエストコード：位置情報許可
     */
    private static final int REQUEST_LOCATION_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ActionBarにアイコン表示
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        setContentView(R.layout.activity_search);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Bluetoothが無効か確認
        if (!BleWrapper.isBluetoothEnable(this)) {
            requestEnableBT();
            return;
        }

        // AndroidM対応、位置情報が有効か確認（BLEのスキャンに位置情報パーミッションが必要なため）
        if (!checkPermissions()) {
            requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSIONS);
            return;
        }

        initViews();
    }

    private void initViews() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag(SearchFragment.TAG) == null) {
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.view_holder, SearchFragment.newInstance(), SearchFragment.TAG);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void Selected(String macAddress) {
        // 接続対象のきゃりーが選択された
        MyPrefs.setTargetCarry(this, macAddress);

        // メイン画面へ
        final Intent intent = new Intent(this, CarryActivity.class);
        startActivity(intent);

        // 画面閉じる
        finish();
    }

    // --------------------------------------------


    /**
     * 必要なパーミッションの確認を行う
     * <p>
     * AndroidM対応
     * </p>
     *
     * @return パーミッションが与えられている
     */
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    /**
     * 指定したパーミッションをユーザーにリクエストする.
     *
     * @param permissions パーミッションリスト
     * @param requestCode リクエエストコード
     */
    private void requestPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(
                this,
                permissions,
                requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            if (!checkPermissions()) {
                Toast.makeText(this, "位置情報のリクエストを許可してください", Toast.LENGTH_LONG).show();
                finish();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Bluetooth設定用画面を起動する.
     */
    private void requestEnableBT() {
        Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(i, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // まだOFFになっているなら、再度呼び出し
                if (resultCode == Activity.RESULT_CANCELED) {
                    requestEnableBT();
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
