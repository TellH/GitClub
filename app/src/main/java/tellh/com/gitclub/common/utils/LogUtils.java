package tellh.com.gitclub.common.utils;

import com.orhanobut.logger.Logger;

import tellh.com.gitclub.BuildConfig;
import tellh.com.gitclub.R;

/**
 * Created by tlh on 2016/8/24 :)
 */
public class LogUtils {
    private static final String TAG = Utils.getString(R.string.app_name);

    public static void init() {
        Logger.init(TAG);
    }

    public static void i(String msg) {
        if (BuildConfig.DEBUG) {
            Logger.i(msg);
        }
    }

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Logger.d(msg);
        }
    }

    public static void w(String msg) {
        if (BuildConfig.DEBUG) {
            Logger.w(msg);
        }
    }

    public static void e(String msg) {
        Logger.e(msg);
    }

    public static void e(Throwable e) {
        Logger.e(e, "");
    }

}
