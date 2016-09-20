package tellh.com.gitclub.presentation.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;
import tellh.com.gitclub.presentation.contract.bus.event.OnClickOutsideToHideEvent;

/**
 * Created by tlh on 2016/9/6 :)
 */
public class DispatchTouchViewPager extends ViewPager {
    public DispatchTouchViewPager(Context context) {
        super(context);
    }

    public DispatchTouchViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        OnClickOutsideToHideEvent clickEvent = new OnClickOutsideToHideEvent();
        RxBusPostman.postOnClickScreenEvent(clickEvent);
        return clickEvent.consume || super.dispatchTouchEvent(ev);
    }
}
