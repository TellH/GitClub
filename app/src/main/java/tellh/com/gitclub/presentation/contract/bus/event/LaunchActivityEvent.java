package tellh.com.gitclub.presentation.contract.bus.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

/**
 * Created by tlh on 2016/9/19 :)
 */
public class LaunchActivityEvent {
    public static final int PERSONAL_HOME_PAGE_ACTIVITY = 357;
    public static final int BROWSER_ACTIVITY = 987;
    public static final int REPO_PAGE_ACTIVITY = 544;

    @IntDef({PERSONAL_HOME_PAGE_ACTIVITY, BROWSER_ACTIVITY, REPO_PAGE_ACTIVITY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TargetActivity {
    }

    public Map<String, String> params;
    @TargetActivity
    public int targetActivity;

    public LaunchActivityEvent(Map<String, String> params, @TargetActivity int targetActivity) {
        this.params = params;
        this.targetActivity = targetActivity;
    }
}
