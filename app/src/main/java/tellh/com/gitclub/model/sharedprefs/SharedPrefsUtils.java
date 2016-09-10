package tellh.com.gitclub.model.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.utils.LogUtils;

/**
 * Created by tlh on 2016/8/25 :)
 */
public class SharedPrefsUtils {
    private static SharedPreferences mSharedPreferences;// 单例

    public static synchronized void init(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
        }
    }

    public static void saveData(String key, Object data) {
        if (data == null) {
            LogUtils.e("save data in SharePrefs is null.");
            return;
        }
        String type = data.getClass().getSimpleName();
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) data);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) data);
        } else if ("String".equals(type)) {
            editor.putString(key, (String) data);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) data);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) data);
        }

        editor.apply();
        TextView tv=new TextView(AndroidApplication.getInstance());
    }

    public static Object getData(String key, Object defValue) {
        if (defValue == null) {
            LogUtils.e("defValue in SharePrefs is null.");
            return null;
        }
        String type = defValue.getClass().getSimpleName();
        if ("Integer".equals(type)) {
            return mSharedPreferences.getInt(key, (Integer) defValue);
        } else if ("Boolean".equals(type)) {
            return mSharedPreferences.getBoolean(key, (Boolean) defValue);
        } else if ("String".equals(type)) {
            return mSharedPreferences.getString(key, (String) defValue);
        } else if ("Float".equals(type)) {
            return mSharedPreferences.getFloat(key, (Float) defValue);
        } else if ("Long".equals(type)) {
            return mSharedPreferences.getLong(key, (Long) defValue);
        }
        return null;
    }
}
