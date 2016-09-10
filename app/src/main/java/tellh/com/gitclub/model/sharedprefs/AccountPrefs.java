package tellh.com.gitclub.model.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import tellh.com.gitclub.model.entity.UserInfo;

/**
 * Created by tlh on 2016/8/25 :)
 */
public class AccountPrefs {
    private static UserInfo user;
    private static final String KEY_LOGIN_TOKEN = "login_token";
    private static final String KEY_LOGON_USER = "login_user";
    private static final String KEY_LOGON_USER_NAME = "login_user_name";

    private static SharedPreferences getPreference(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences("AccountPrefs", Context.MODE_PRIVATE);
    }

    public static void saveLoginToken(Context context, String loginToken) {
        getPreference(context).edit().putString(KEY_LOGIN_TOKEN, loginToken).apply();
    }

    public static String getLoginToken(Context context) {
        return getPreference(context).getString(KEY_LOGIN_TOKEN, "");
    }

    public static void saveLoginUser(Context context, UserInfo user) {
        String userJson = new Gson().toJson(user);
        getPreference(context).edit()
                .putString(KEY_LOGON_USER, userJson)
                .putString(KEY_LOGON_USER_NAME, user.getLogin())
                .apply();
    }

    public static void removeLoginUser(Context context) {
        user = null;
        getPreference(context).edit().remove(KEY_LOGON_USER).apply();
    }

    public static UserInfo getLoginUser(Context context) {
        if (user != null) {
            return user;
        }
        String userJson = getPreference(context).getString(KEY_LOGON_USER, "");
        if (!TextUtils.isEmpty(userJson)) {
            user = new Gson().fromJson(userJson, UserInfo.class);
        }
        return user;
    }

    public static String getLoginUserName(Context context){
        return getPreference(context).getString(KEY_LOGON_USER_NAME, "");
    }
    public static boolean isLogin(Context context) {
        return !TextUtils.isEmpty(getLoginToken(context)) && getLoginUser(context) != null;
    }
}
