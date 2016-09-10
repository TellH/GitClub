package tellh.com.gitclub.presentation.widget;

import android.support.v4.view.ViewPager;

/**
 * Created by tlh on 2016/9/6 :)
 */
public abstract class OnPageChangeListenerAdapter implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public abstract void onPageSelected(int position);

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
