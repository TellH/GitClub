package tellh.com.gitclub.common.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.DrawableRes;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.presentation.widget.AnimationListenerAdapter;

/**
 * Created by tlh on 2016/8/24 :)
 */
public class Utils {

    public static String getString(int resId) {
        return AndroidApplication.getInstance().getString(resId);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    public static void leakWatch(Object o) {
        AndroidApplication.getInstance().getRefWatcher().watch(o);
    }

    public static int getScreenWidth() {
        return AndroidApplication.getInstance().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return AndroidApplication.getInstance().getResources().getDisplayMetrics().heightPixels;
    }

    public static void setImageWithFade(final ImageView img, @DrawableRes final int resId) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(800);
        final Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(800);

        fadeOut.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationEnd(Animation animation) {
                img.startAnimation(fadeIn);
            }
        });
        fadeIn.setAnimationListener(new AnimationListenerAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                img.setImageResource(resId);
            }
        });
        img.startAnimation(fadeOut);
    }

    public static void copyDataToClipBoard(String data) {
        ClipboardManager clipboardManager;
        clipboardManager = (ClipboardManager) AndroidApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText("text", data));
    }
}
