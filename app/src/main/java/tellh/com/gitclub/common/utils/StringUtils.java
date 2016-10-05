package tellh.com.gitclub.common.utils;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static void changeFontStyle(String fontType, TextView tv) {
        Typeface custom_font = Typeface.createFromAsset(AndroidApplication.getInstance().getAssets(), fontType);
        tv.setTypeface(custom_font);
    }

    public static String formatNumber2Thousand(int num) {
        if (num / 1000 <= 0)
            return String.valueOf(num);
        return append(String.format("%.1f", (float) num / 1000f), "k");
    }

    public static String checkRepoNameLength(String fullName, String repoName) {
        if (fullName.length() < 25)
            return fullName;
        return StringUtils.append("…/", repoName);
    }

    public static String[] parseGithubUrl(String rawUrl) {
        Matcher m = Pattern
//                .compile("https://github\\.com/(.*?)/(((.*?)/.*)|(.*))")
//                .compile("https://github\\.com/(.*?)/(?:(?:(.*?)/.*)|(.*))")
                .compile("https://github\\.com/([^/]*)/([^/]*)")
                .matcher(rawUrl);
        String[] result = new String[2];
        if (m.find()) {
            try {
                if (!TextUtils.isEmpty(m.group(1)))
                    result[0] = m.group(1);
                else return null;
                if (!TextUtils.isEmpty(m.group(2)))
                    result[1] = m.group(2);
                else return null;
            } catch (Exception e) {
                return null;
            }
        } else return null;
        return result;
    }
}
