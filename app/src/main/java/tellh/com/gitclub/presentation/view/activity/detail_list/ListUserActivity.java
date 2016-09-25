package tellh.com.gitclub.presentation.view.activity.detail_list;

import java.util.List;

import javax.inject.Inject;

import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.di.component.DaggerListItemComponent;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.presentation.presenter.ListUserPresenter;
import tellh.com.gitclub.presentation.view.adapter.BaseRecyclerAdapter;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.UpdateType;
import tellh.com.gitclub.presentation.view.adapter.UserListAdapter;

/**
 * Created by tlh on 2016/9/16 :)
 */
public abstract class ListUserActivity extends BaseListActivity implements ListUserPresenter.ListUserView {
    @Inject
    ListUserPresenter presenter;

    @Override
    protected BaseRecyclerAdapter getListAdapter() {
        return new UserListAdapter(this, null, presenter);
    }

    @Override
    protected void initDagger() {
        DaggerListItemComponent.builder()
                .appComponent(AndroidApplication.getInstance().getAppComponent())
                .build().inject(this);
        presenter.attachView(this);
        presenter.setRepo(user, repo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void onGetUserList(List<UserEntity> list, @UpdateType int updateType) {
        loadMoreWrapper.OnGetData(list, updateType);
        refreshLayout.setRefreshing(false);
    }
}
