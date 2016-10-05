package tellh.com.gitclub.presentation.view.activity.detail_list;

import com.tellh.nolistadapter.viewbinder.base.RecyclerViewBinder;

import java.util.List;

import javax.inject.Inject;

import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.di.component.DaggerListItemComponent;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.presentation.presenter.ListRepoPresenter;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.RepoListItemViewBinder;

import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.UpdateType;

/**
 * Created by tlh on 2016/9/16 :)
 */
public abstract class ListRepoActivity extends BaseListActivity
        implements ListRepoPresenter.ListRepoView {
    @Inject
    ListRepoPresenter presenter;

    @Override
    protected RecyclerViewBinder getListItemViewBinder() {
        return new RepoListItemViewBinder(presenter);
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

    @Override
    public void onRefresh() {
        loadMoreWrapper.hideErrorView(recyclerView);
    }
}
