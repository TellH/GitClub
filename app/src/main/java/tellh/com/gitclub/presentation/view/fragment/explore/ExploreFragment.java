package tellh.com.gitclub.presentation.view.fragment.explore;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.LazyFragment;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.di.component.DaggerExploreComponent;
import tellh.com.gitclub.model.entity.ShowCase;
import tellh.com.gitclub.model.entity.ShowCaseInfo;
import tellh.com.gitclub.model.entity.Trending;
import tellh.com.gitclub.presentation.contract.ExploreContract;
import tellh.com.gitclub.presentation.contract.bus.RxBus;
import tellh.com.gitclub.presentation.contract.bus.event.GetShowcaseDetailEvent;
import tellh.com.gitclub.presentation.contract.bus.event.OnBackPressEvent;
import tellh.com.gitclub.presentation.contract.bus.event.OnClickOutsideToHideEvent;
import tellh.com.gitclub.presentation.view.adapter.CommonViewPagerAdapter;
import tellh.com.gitclub.presentation.widget.FabAnimationHelper;
import tellh.com.gitclub.presentation.widget.OnPageChangeListenerAdapter;
import tellh.com.gitclub.presentation.widget.ShowcaseListBottomSheetDialog;

import static tellh.com.gitclub.presentation.contract.ExploreContract.ListType;
import static tellh.com.gitclub.presentation.contract.ExploreContract.OnListFragmentInteractListener;
import static tellh.com.gitclub.presentation.contract.ExploreContract.Presenter;
import static tellh.com.gitclub.presentation.contract.ExploreContract.SHOWCASES;
import static tellh.com.gitclub.presentation.contract.ExploreContract.TRENDING;

public class ExploreFragment extends LazyFragment
        implements ExploreContract.View, OnListFragmentInteractListener {
    @Inject
    Presenter presenter;
    private ViewPager mViewPager;
    private ShowCaseListFragment showCaseListFragment;
    private TrendingListFragment trendingListFragment;
    private FloatingActionButton fabLang;
    private FloatingActionButton fabSince;
    private FloatingActionsMenu fabMenu;
    private ShowcaseListBottomSheetDialog showcaseListDialog;
    private ImageView ivHeader;
    private CollapsingToolbarLayout collapsingLayout;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        presenter.listTrending();
        presenter.listShowCase();
        trendingListFragment.showLoading();
        showCaseListFragment.showLoading();
        addSubscription(RxBus.getDefault().toObservable(GetShowcaseDetailEvent.class)
                .subscribe(new Action1<GetShowcaseDetailEvent>() {
                    @Override
                    public void call(GetShowcaseDetailEvent getShowcaseDetailEvent) {
                        presenter.getShowcaseDetail(getShowcaseDetailEvent.showCase);
                    }
                }));
    }

    private void initDagger() {
        DaggerExploreComponent.builder()
                .appComponent(AndroidApplication.getInstance().getAppComponent())
                .build().inject(this);
        presenter.attachView(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_explore;
    }

    @Override
    public void initView() {
        initDagger();

        //find view
        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) mRootView.findViewById(R.id.tab);
        collapsingLayout = (CollapsingToolbarLayout) mRootView.findViewById(R.id.collapsing_toolbar);
        fabLang = (FloatingActionButton) mRootView.findViewById(R.id.fab_language);
        fabSince = (FloatingActionButton) mRootView.findViewById(R.id.fab_since);
        fabMenu = (FloatingActionsMenu) mRootView.findViewById(R.id.fab);
        ivHeader = (ImageView) mRootView.findViewById(R.id.iv_header);

//        init toolbar
        setHasOptionsMenu(true);
        toolbar.setTitle(R.string.explore);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        collapsingLayout.setTitle(Utils.getString(R.string.explore));

        //init viewpager and tab
        setupViewPager();
        tabLayout.setupWithViewPager(mViewPager);

        //fab
        initFab();
    }

    private void initFab() {
        fabLang.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                presenter.getDialogLang().show();
                fabMenu.collapse();
            }
        });
        fabSince.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                presenter.getDialogSince().show();
                fabMenu.collapse();
            }
        });

        addSubscription(RxBus.getDefault().toObservable(OnBackPressEvent.class).subscribe(new Action1<OnBackPressEvent>() {
            @Override
            public void call(OnBackPressEvent event) {
                if (fabMenu == null || !fabMenu.isExpanded())
                    return;
                fabMenu.collapse();
                event.hasConsume = true;
            }
        }));

        addSubscription(RxBus.getDefault().toObservable(OnClickOutsideToHideEvent.class).subscribe(new Action1<OnClickOutsideToHideEvent>() {
            @Override
            public void call(OnClickOutsideToHideEvent event) {
                if (fabMenu == null || !fabMenu.isExpanded())
                    return;
                fabMenu.collapse();
                event.consume = true;
            }
        }));
    }

    private void setupViewPager() {
        CommonViewPagerAdapter viewPagerAdapter = new CommonViewPagerAdapter(getFragmentManager());
        trendingListFragment = TrendingListFragment.newInstance();
        trendingListFragment.setListFragmentInteractListener(this);
        viewPagerAdapter.addFragment("Trending", trendingListFragment);
        showCaseListFragment = ShowCaseListFragment.newInstance();
        showCaseListFragment.setListFragmentInteractListener(this);
        viewPagerAdapter.addFragment("ShowCases", showCaseListFragment);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new OnPageChangeListenerAdapter() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    collapsingLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_purple));
                    Utils.setImageWithFade(ivHeader, R.drawable.train);
                    FabAnimationHelper.show(fabMenu, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            fabMenu.setVisibility(View.VISIBLE);
                        }
                    });
                } else {
                    collapsingLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange_red));
                    Utils.setImageWithFade(ivHeader, R.drawable.sun);
                    FabAnimationHelper.hide(fabMenu, new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            fabMenu.setVisibility(View.INVISIBLE);
                        }
                    });
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView();
        showCaseListFragment = null;
        trendingListFragment = null;
    }

    @Override
    public void showOnError(String msg, @ListType int type) {
        showOnError(msg);
        if (type == ExploreContract.TRENDING)
            trendingListFragment.hideLoading();
        else showCaseListFragment.hideLoading();
    }

    @Override
    public void onGetTrending(List<Trending> trendings) {
        trendingListFragment.onGet(trendings);
    }

    @Override
    public void onGetShowcases(List<ShowCase> showCases) {
        showCaseListFragment.onGet(showCases);
    }

    @Override
    public void onGetShowcasesDetail(ShowCaseInfo showCaseInfo) {
        if (showcaseListDialog == null)
            showcaseListDialog = new ShowcaseListBottomSheetDialog(getContext(), presenter);
        showcaseListDialog.refreshAndShow(showCaseInfo);
    }

    @Override
    public void onFetchData(@ListType int type) {
        switch (type) {
            case SHOWCASES:
                presenter.listShowCase();
                break;
            case TRENDING:
                presenter.listTrending();
                break;
        }
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }

    @Override //the first page will not be lazy loaded.
    protected boolean shouldLazyLoad() {
        return false;
    }
}
