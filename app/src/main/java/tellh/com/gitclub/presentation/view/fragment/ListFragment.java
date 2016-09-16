package tellh.com.gitclub.presentation.view.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import tellh.com.gitclub.R;
import tellh.com.gitclub.presentation.view.fragment.search.ListLoadingListener;
import tellh.com.gitclub.presentation.widget.ErrorViewHelper;

public abstract class ListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, ListLoadingListener {
    protected SwipeRefreshLayout refreshLayout;
    protected ErrorViewHelper errorView;

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

    protected RecyclerView recyclerView;
    protected Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        // Set the adapter
        context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        errorView = new ErrorViewHelper((ViewStub) view.findViewById(R.id.vs_error));

        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setAdapter(getListAdapter());

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setProgressViewOffset(false, -100, 80);
        refreshLayout.setColorSchemeResources(R.color.blue, R.color.brown, R.color.purple, R.color.green);
        return view;
    }

    @NonNull
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(context);
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

    protected abstract RecyclerView.Adapter getListAdapter();

    protected abstract int getLayoutId();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
