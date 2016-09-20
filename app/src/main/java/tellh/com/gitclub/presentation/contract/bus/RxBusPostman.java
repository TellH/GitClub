package tellh.com.gitclub.presentation.contract.bus;

import java.util.Map;

import tellh.com.gitclub.model.entity.ShowCase;
import tellh.com.gitclub.presentation.contract.bus.event.GetShowcaseDetailEvent;
import tellh.com.gitclub.presentation.contract.bus.event.LaunchActivityEvent;
import tellh.com.gitclub.presentation.contract.bus.event.OnBackPressEvent;
import tellh.com.gitclub.presentation.contract.bus.event.OnClickOutsideToHideEvent;
import tellh.com.gitclub.presentation.contract.bus.event.QuickReturnEvent;

import static tellh.com.gitclub.presentation.contract.bus.event.LaunchActivityEvent.*;

/**
 * Created by tlh on 2016/9/3 :)
 */
public class RxBusPostman {
    public static void postQuickReturnEvent(boolean show) {
        RxBus.getDefault().post(new QuickReturnEvent(!show));
    }

    public static void postOnBackPressEvent(OnBackPressEvent event) {
        RxBus.getDefault().post(event);
    }

    public static void postOnClickScreenEvent(OnClickOutsideToHideEvent event) {
        RxBus.getDefault().post(event);
    }

    public static void postGetShowcaseDetailEvent(ShowCase showCase) {
        RxBus.getDefault().post(new GetShowcaseDetailEvent(showCase));
    }

    public static void postLaunchActivityEvent(Map<String, String> params, @TargetActivity int targetActivity) {
        RxBus.getDefault().post(new LaunchActivityEvent(params, targetActivity));
    }
}
