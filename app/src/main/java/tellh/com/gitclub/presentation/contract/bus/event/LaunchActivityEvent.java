package tellh.com.gitclub.presentation.contract.bus.event;

import java.util.Map;

/**
 * Created by tlh on 2016/9/19 :)
 */
public class LaunchActivityEvent {
    public enum TargetActivity {
        PersonalHomePageActivity,
        BrowserActivity;
    }

    public Map<String,String> params;
    public TargetActivity targetActivity;

    public LaunchActivityEvent(Map<String, String> params, TargetActivity targetActivity) {
        this.params = params;
        this.targetActivity = targetActivity;
    }
}
