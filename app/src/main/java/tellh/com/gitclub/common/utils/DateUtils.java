package tellh.com.gitclub.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import tellh.com.gitclub.R;

/**
 * Created by tlh on 2016/9/9 :)
 */
public class DateUtils {

    public static final List<Long> times = Arrays.asList(
            TimeUnit.DAYS.toMillis(365),
            TimeUnit.DAYS.toMillis(30),
            TimeUnit.DAYS.toMillis(1),
            TimeUnit.HOURS.toMillis(1),
            TimeUnit.MINUTES.toMillis(1),
            TimeUnit.SECONDS.toMillis(1));
    public static final List<String> timesString = Arrays.asList(Utils.getString(R.string.date_year),
            Utils.getString(R.string.date_month), Utils.getString(R.string.date_day), Utils.getString(R.string.date_hour),
            Utils.getString(R.string.date_minute), Utils.getString(R.string.date_sec));

    private static SimpleDateFormat format;
    static {
        format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //fix the time different 时差
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    public static Date getDate(String date) {
        try {
            return format.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String timeAgo(String rawDate) {
        Date from = getDate(rawDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        long past = cal.getTimeInMillis();
        cal.setTime(new Date());
        long now = cal.getTimeInMillis();
        return timeAgo(now - past);
    }

    public static String timeAgo(long duration) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times.size(); i++) {
            Long current = times.get(i);
            long temp = duration / current;
            if (temp > 0) {
                result.append(temp).append(" ").append(timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
                break;
            }
        }
        if ("".equals(result.toString()))
            return "0 second ago";
        else
            return result.toString();
    }
}
