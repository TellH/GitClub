package tellh.com.gitclub.presentation.view.activity.detail_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BaseActivity;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.presentation.contract.ShowError;
import tellh.com.gitclub.presentation.view.adapter.BaseRecyclerAdapter;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.UpdateType;
import tellh.com.gitclub.presentation.view.fragment.search.ListLoadingListener;
import tellh.com.gitclub.presentation.widget.ErrorViewHelper;

import static tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.LOADING;
import static tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.PULL_TO_LOAD_MORE;
import static tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.REFRESH;

/**
 * Created by tlh on 2016/9/16 :)
 */
public abstract class BaseListActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener, ListLoadingListener, ShowError,
        FooterLoadMoreAdapterWrapper.OnReachFooterListener {
    protected SwipeRefreshLayout refreshLayout;
    protected ErrorViewHelper errorView;
    protected RecyclerView recyclerView;
    protected FooterLoadMoreAdapterWrapper loadMoreWrapper;
    protected String user;
    protected String repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            user = intent.getStringExtra(ExtraKey.USER_NAME);
            repo = intent.getStringExtra(ExtraKey.REPO_NAME);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        initDagger();

        recyclerView = (RecyclerView) findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        errorView = new ErrorViewHelper((ViewStub) findViewById(R.id.vs_error));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getToolbarTitle());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadMoreWrapper = new FooterLoadMoreAdapterWrapper(getListAdapter());
        loadMoreWrapper.addFooter(R.layout.footer_load_more);
        loadMoreWrapper.setOnReachFooterListener(recyclerView, this);
        recyclerView.setAdapter(loadMoreWrapper);

        //swipe refresh layout
        refreshLayout.setProgressViewOffset(false, -100, 230);
        refreshLayout.setColorSchemeResources(R.color.blue, R.color.brown, R.color.purple, R.color.green);
        refreshLayout.setOnRefreshListener(this);
    }

    protected abstract BaseRecyclerAdapter getListAdapter();

    protected abstract String getToolbarTitle();

    protected abstract void initDagger();

    @Override
    public int getLayoutId() {
        return R.layout.activity_list;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        refreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void showLoading() {
        if (refreshLayout.isRefreshing())
            return;
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (!refreshLayout.isRefreshing())
            return;
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showErrorView() {
        errorView.showErrorView(refreshLayout, new ErrorViewHelper.OnReLoadCallback() {
            @Override
            public void reload() {
                hideLoading();
                onRefresh();
            }
        });
    }

    @Override
    public void onToLoadMore(int curPage) {
        loadMoreWrapper.setFooterStatus(LOADING);
    }

    @Override
    public void showOnError(String msg, @UpdateType int updateType) {
        showOnError(msg);
        handleError(msg, updateType);
    }

    protected void handleError(String msg, @UpdateType int updateType) {
        if (updateType == REFRESH)
            refreshLayout.setRefreshing(false);
        else
            loadMoreWrapper.setFooterStatus(PULL_TO_LOAD_MORE);

        if (updateType == REFRESH && !msg.equals(Utils.getString(R.string.reqest_flying))) {
            errorView.showErrorView(refreshLayout, new ErrorViewHelper.OnReLoadCallback() {
                @Override
                public void reload() {
                    refreshLayout.setRefreshing(true);
                    onRefresh();
                }
            });
        }
    }

}
