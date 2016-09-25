package tellh.com.gitclub.presentation.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

import java.util.Map;

import rx.Subscription;
import rx.functions.Action1;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BaseActivity;
import tellh.com.gitclub.common.wrapper.Note;
import tellh.com.gitclub.presentation.contract.bus.RxBus;
import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;
import tellh.com.gitclub.presentation.contract.bus.event.LaunchActivityEvent;
import tellh.com.gitclub.presentation.contract.bus.event.OnBackPressEvent;
import tellh.com.gitclub.presentation.contract.bus.event.QuickReturnEvent;
import tellh.com.gitclub.presentation.view.activity.repo_page.RepoPageActivity;
import tellh.com.gitclub.presentation.view.activity.user_personal_page.PersonalHomePageActivity;
import tellh.com.gitclub.presentation.view.adapter.CommonViewPagerAdapter;
import tellh.com.gitclub.presentation.view.fragment.explore.ExploreFragment;
import tellh.com.gitclub.presentation.view.fragment.home.HomePageFragment;
import tellh.com.gitclub.presentation.view.fragment.news.NewsFragment;
import tellh.com.gitclub.presentation.view.fragment.search.SearchFragment;
import tellh.com.gitclub.presentation.widget.AHBottomNavigation;

import static tellh.com.gitclub.common.config.ExtraKey.NAME_BLOG;
import static tellh.com.gitclub.common.config.ExtraKey.REPO_NAME;
import static tellh.com.gitclub.common.config.ExtraKey.USER_NAME;

public class HomeActivity extends BaseActivity {

    private AHBottomNavigationViewPager viewPager;
    private AHBottomNavigation navBar;

    Subscription subscriptionQuickReturn;
    private Subscription subscriptionLaunchActivity;
    private long exitTime;

    @Override
    public void initData(Bundle savedInstanceState) {
        initBottomBar();
        initViewPager();
        subscriptionQuickReturn = RxBus.getDefault().toObservable(QuickReturnEvent.class)
                .subscribe(new Action1<QuickReturnEvent>() {
                    @Override
                    public void call(QuickReturnEvent quickReturnEvent) {
                        if (quickReturnEvent.toHide)
                            navBar.hideBottomNavigation(true);
                        else
                            navBar.restoreBottomNavigation(true);
                    }
                });

        subscriptionLaunchActivity = RxBus.getDefault().toObservable(LaunchActivityEvent.class)
                .subscribe(new Action1<LaunchActivityEvent>() {
                    @Override
                    public void call(LaunchActivityEvent launchActivityEvent) {
                        Map<String, String> params = launchActivityEvent.params;
                        switch (launchActivityEvent.targetActivity) {
                            case LaunchActivityEvent.PERSONAL_HOME_PAGE_ACTIVITY:
                                PersonalHomePageActivity.launch(HomeActivity.this, params.get(USER_NAME));
                                break;
                            case LaunchActivityEvent.BROWSER_ACTIVITY:
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(params.get(NAME_BLOG)));
                                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                                startActivity(intent);
                                break;
                            case LaunchActivityEvent.REPO_PAGE_ACTIVITY:
                                RepoPageActivity.launch(HomeActivity.this, params.get(USER_NAME), params.get(REPO_NAME));
                                break;
                        }
                    }
                });
    }

    private void initBottomBar() {
        AHBottomNavigationItem search = new AHBottomNavigationItem(R.string.search, R.drawable.ic_search_white, R.color.blue);
        AHBottomNavigationItem profile = new AHBottomNavigationItem(R.string.profile, R.drawable.ic_personal_page_white, R.color.brown);
        AHBottomNavigationItem explore = new AHBottomNavigationItem(R.string.explore, R.drawable.ic_explore_white, R.color.purple);
        AHBottomNavigationItem news = new AHBottomNavigationItem(R.string.news, R.drawable.ic_notifications_white, R.color.teal);
        navBar.addItem(explore);
        navBar.addItem(news);
        navBar.addItem(search);
        navBar.addItem(profile);
        navBar.setColored(false);
        navBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                viewPager.setCurrentItem(position, false);
                return true;
            }
        });
    }

    private void initViewPager() {
        viewPager.setOffscreenPageLimit(4);
        CommonViewPagerAdapter pagerAdapter = new CommonViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment("Explore", ExploreFragment.newInstance());
        pagerAdapter.addFragment("News", NewsFragment.newInstance());
        pagerAdapter.addFragment("Search", SearchFragment.newInstance());
        pagerAdapter.addFragment("Personal", HomePageFragment.newInstance());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {
        viewPager = (AHBottomNavigationViewPager) findViewById(R.id.view_pager);
        navBar = (AHBottomNavigation) findViewById(R.id.nav_bar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscriptionQuickReturn.unsubscribe();
        subscriptionLaunchActivity.unsubscribe();
    }

    @Override
    public void onBackPressed() {
        OnBackPressEvent event = new OnBackPressEvent();
        RxBusPostman.postOnBackPressEvent(event);
        if (event.hasConsume)
            return;
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Note.getSnackbar("Are you sure to exit GitClub?", navBar)
                    .setAction("Exit", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    }).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}
