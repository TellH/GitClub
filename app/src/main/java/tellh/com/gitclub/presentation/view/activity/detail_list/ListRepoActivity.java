package tellh.com.gitclub.presentation.view.activity.detail_list;

import java.util.List;

import javax.inject.Inject;

import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.di.component.DaggerListItemComponent;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.presentation.presenter.ListRepoPresenter;
import tellh.com.gitclub.presentation.view.adapter.BaseRecyclerAdapter;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.UpdateType;
import tellh.com.gitclub.presentation.view.adapter.RepoListAdapter;

/**
 * Created by tlh on 2016/9/16 :)
 */
public abstract class ListRepoActivity extends BaseListActivity
        implements ListRepoPresenter.ListRepoView {
    @Inject
    ListRepoPresenter presenter;

    @Override
    protected BaseRecyclerAdapter getListAdapter() {
        return new RepoListAdapter(this, null, presenter);
    }

    @Override
    protected void initDagger() {
        DaggerListItemComponent.builder()
                .appComponent(AndroidApplication.getInstance().getAppComponent())
                .build().inject(this);
        presenter.attachView(this);
        presenter.setUser(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void onGetRepoList(List<RepositoryInfo> list, @UpdateType int updateType) {
        loadMoreWrapper.OnGetData(list, updateType);
        refreshLayout.setRefreshing(false);
    }

}
