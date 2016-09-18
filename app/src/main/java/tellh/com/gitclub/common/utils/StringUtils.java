package tellh.com.gitclub.common.utils;

import android.graphics.Typeface;
import android.util.Base64;
import android.widget.TextView;

import tellh.com.gitclub.common.AndroidApplication;

/**
 * Created by tlh on 2016/8/24 :)
 */
public class StringUtils {
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static String base64Decode(String originalString) {
        if (isEmpty(originalString)) return "";
        return new String(Base64.decode(originalString, 0));
    }

    public static String append(CharSequence... arr) {
        StringBuilder builder = new StringBuilder();
        for (CharSequence s : arr) {
            builder.append(s);
        }
        return builder.toString();
    }

    public static void changeFontStype(String fontType, TextView tv) {
        Typeface custom_font = Typeface.createFromAsset(AndroidApplication.getInstance().getAssets(), fontType);
        tv.setTypeface(custom_font);
    }
}
