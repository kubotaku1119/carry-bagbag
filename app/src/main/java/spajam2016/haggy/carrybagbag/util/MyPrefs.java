package spajam2016.haggy.carrybagbag.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences Wrapper class
 */
public class MyPrefs {

    private static final String KEY_INITIAL_FLAG = "key_initial_flag";

    private static final String KEY_TARGET_CARRY = "key_target_carry";

    private static final String KEY_STATE_OWAKARE = "key_state_owakare";

    /**
     * 初回起動時の説明画面が終了したフラグを記録する.
     *
     * @param context コンテキスト
     */
    public static void setFinishHelloActivity(Context context) {
        final SharedPreferences prefs = getPrefs(context);
        final SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(KEY_INITIAL_FLAG, true);
        edit.commit();
    }

    public static void setSongPath(Context context, String path) {
        final SharedPreferences prefs = getPrefs(context);
        final SharedPreferences.Editor edit = prefs.edit();
        edit.putString("Song_Path", path);
        edit.commit();
    }

    public static String getSongPath(Context context) {
        final SharedPreferences prefs = getPrefs(context);
        return prefs.getString("Song_Path", "");
    }

    /**
     * 初回起動時の説明画面が表示済みか確認する.
     *
     * @param context コンテキスト
     * @return
     */
    public static boolean isFnishedHelloActivity(Context context) {
        final SharedPreferences prefs = getPrefs(context);
        return prefs.getBoolean(KEY_INITIAL_FLAG, false);
    }

    /**
     * 初回起動時の説明画面終了フラグをクリアする（デバッグ用）
     *
     * @param context コンテキスト
     */
    public static void clearFinishedHelloActivityFlag(Context context) {
        final SharedPreferences prefs = getPrefs(context);
        final SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(KEY_INITIAL_FLAG, false);
        edit.commit();
    }

    /**
     * 接続対象のきゃりーを設定する。
     *
     * @param context    　コンテキスト。
     * @param macAddress 　きゃりーのMacAddress
     */
    public static void setTargetCarry(Context context, String macAddress) {
        final SharedPreferences prefs = getPrefs(context);
        final SharedPreferences.Editor edit = prefs.edit();
        edit.putString(KEY_TARGET_CARRY, macAddress);
        edit.commit();
    }

    /**
     * 接続対象のきゃりーが設定済みか確認する
     *
     * @param context 　コンテキスト
     * @return true : 設定済み
     */
    public static boolean existTargetCarry(Context context) {
        final SharedPreferences prefs = getPrefs(context);
        if (prefs.getString(KEY_TARGET_CARRY, null) == null) {
            return false;
        }
        return true;
    }

    /**
     * お別れ状態 or お迎え状態をセットする.
     *
     * @param context コンテキスト
     * @param owakare true : おわかれ, false : お迎え
     */
    public static void setStateOwakare(Context context, boolean owakare) {
        final SharedPreferences prefs = getPrefs(context);
        final SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(KEY_STATE_OWAKARE, owakare);
        edit.commit();
    }

    /**
     * 現在の状態がおわかれかどうかを取得する。
     *
     * @param context コンテキスト.
     * @return true : おわかれ, false : おむかえ
     */
    public static boolean isOwakareState(Context context) {
        final SharedPreferences prefs = getPrefs(context);
        return prefs.getBoolean(KEY_STATE_OWAKARE, false);
    }

    /**
     * 接続対象のきゃりーを取得する。
     *
     * @param context コンテキスト。
     * @return きゃりーのMacAddress
     */
    public static String getTargetCarry(Context context) {
        final SharedPreferences prefs = getPrefs(context);
        return prefs.getString(KEY_TARGET_CARRY, null);
    }


    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }
}
