package tellh.com.gitclub.presentation.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tlh on 2016/8/30 :)
 */
public class CommonViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<CharSequence> titleList;

    public CommonViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<CharSequence> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    public CommonViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentList = new ArrayList<>();
        this.titleList = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(CharSequence title, Fragment fragment) {
        titleList.add(title);
        fragmentList.add(fragment);
    }

    public void addFragment(Fragment fragment) {
        addFragment("",fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
