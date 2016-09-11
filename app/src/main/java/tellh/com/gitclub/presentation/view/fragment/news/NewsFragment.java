package tellh.com.gitclub.presentation.view.fragment.news;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewStub;

import java.util.List;

import javax.inject.Inject;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.LazyFragment;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.di.component.DaggerNewsComponent;
import tellh.com.gitclub.model.entity.Event;
import tellh.com.gitclub.presentation.contract.NewsContract;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.UpdateType;
import tellh.com.gitclub.presentation.view.adapter.NewsListAdapter;
import tellh.com.gitclub.presentation.view.fragment.login.LoginFragment;
import tellh.com.gitclub.presentation.widget.ErrorViewHelper;

public class NewsFragment extends LazyFragment
        implements NewsContract.View, SwipeRefreshLayout.OnRefreshListener,
        FooterLoadMoreAdapterWrapper.OnReachFooterListener, LoginFragment.LoginCallback {
    @Inject
    NewsContract.Presenter presenter;
    private FooterLoadMoreAdapterWrapper loadMoreWrapper;
    private SwipeRefreshLayout refreshLayout;

    private LoginFragment loginFragment;
    private ErrorViewHelper errorView;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        refreshLayout.setRefreshing(true);
        presenter.listNews(1);
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_news;
    }

    @Override
    public void initView() {
        initDagger();

        //find view
        RecyclerView list = (RecyclerView) mRootView.findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refreshLayout);
        errorView = new ErrorViewHelper((ViewStub) mRootView.findViewById(R.id.vs_error));

        //swipe refresh layout
        refreshLayout.setProgressViewOffset(false, -100, 230);
        refreshLayout.setColorSchemeResources(R.color.blue, R.color.brown, R.color.purple, R.color.green);
        refreshLayout.setOnRefreshListener(this);

        //adapter
        loadMoreWrapper = new FooterLoadMoreAdapterWrapper(new NewsListAdapter(null, getContext()));
        loadMoreWrapper.addFooter(R.layout.footer_load_more);
        loadMoreWrapper.setOnReachFooterListener(list, this);

        //recycler view
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(loadMoreWrapper);
    }

    private void initDagger() {
        DaggerNewsComponent.builder()
                .appComponent(AndroidApplication.getInstance().getAppComponent())
                .build().inject(this);
        presenter.attachView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void OnGetNews(List<Event> newsList, UpdateType updateType) {
        loadMoreWrapper.OnGetData(newsList, updateType);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        presenter.listNews(1);
    }

    @Override
    public void onToLoadMore(int curPage) {
        loadMoreWrapper.setFooterStatus(FooterLoadMoreAdapterWrapper.FooterState.LOADING);
        presenter.listNews(curPage + 1);
    }

    @Override
    public void showOnError(String s, UpdateType updateType) {
        showOnError(s);
        if (updateType == UpdateType.REFRESH)
            refreshLayout.setRefreshing(false);
        else
            loadMoreWrapper.setFooterStatus(FooterLoadMoreAdapterWrapper.FooterState.PULL_TO_LOAD_MORE);

        if (updateType == UpdateType.REFRESH && !s.equals(Utils.getString(R.string.reqest_flying))) {
            errorView.showErrorView(refreshLayout, new ErrorViewHelper.OnReLoadCallback() {
                @Override
                public void reload() {
                    refreshLayout.setRefreshing(true);
                    presenter.listNews(1);
                }
            });
        }

    }


    @Override
    public void showLoginDialog() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            loginFragment.setCallback(this);
        }
        if (loginFragment.getDialog() == null)
            loginFragment.show(getFragmentManager(), ExtraKey.TAG_LOGIN_FRAGMENT);
        else loginFragment.getDialog().show();
    }

    @Override
    public void onSuccessToLogin() {
        //dismiss the login dialog
        loginFragment.setDismissable(true);
        loginFragment.dismiss();
        //hide error view
        errorView.hideErrorView(refreshLayout);
        //load data
        refreshLayout.setRefreshing(true);
        presenter.listNews(1);
    }

    @Override
    public void onDismissLogin() {
        loginFragment = null;
    }
}
