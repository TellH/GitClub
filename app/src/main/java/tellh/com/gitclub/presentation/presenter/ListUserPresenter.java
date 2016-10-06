package tellh.com.gitclub.presentation.presenter;

import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action0;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.common.base.BaseView;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;
import tellh.com.gitclub.presentation.contract.ShowError;

import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.UpdateType;

/**
 * Created by tlh on 2016/9/16 :)
 */
public class ListUserPresenter extends BasePresenter<ListUserPresenter.ListUserView> implements IUserListPresenter {
    private final RepositoryDataSource mRepositoryDataSource;
    private final IUserListPresenter userListPresenter;
    private final UserDataSource mUserDataSource;
    private boolean isFlying;
    private String owner;
    private String repo;

    public ListUserPresenter(RepositoryDataSource repositoryDataSource, UserDataSource userDataSource) {
        this.mRepositoryDataSource = repositoryDataSource;
        this.mUserDataSource = userDataSource;
        userListPresenter = new UserListPresenter(this, userDataSource);
    }

    public void setRepo(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
    }

    public void listForks(final int page) {
        if (owner == null || repo == null) {
            return;
        }
        if (isFlying) {
            getView().showOnError(getUpdateType(page), Utils.getString(R.string.reqest_flying));
            return;
        }
        isFlying = true;
        addSubscription(
                mRepositoryDataSource.listForks(owner, repo, page)
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                isFlying = false;
                            }
                        })
                        .subscribe(new DefaultSubscriber<List<RepositoryInfo>>() {
                            @Override
                            public void onNext(List<RepositoryInfo> list) {
                                ArrayList<UserEntity> userEntities = new ArrayList<>();
                                for (RepositoryInfo repositoryInfo : list) {
                                    userEntities.add(repositoryInfo.getOwner());
                                }
                                getView().onGetUserList(userEntities, getUpdateType(page));
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(getUpdateType(page), errorStr);
                            }
                        })
        );
    }

    public void listWatchers(final int page) {
        if (owner == null || repo == null) {
            return;
        }
        if (isFlying) {
            getView().showOnError(getUpdateType(page), Utils.getString(R.string.reqest_flying));
            return;
        }
        isFlying = true;
        addSubscription(
                mRepositoryDataSource.listWatchers(owner, repo, page)
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                isFlying = false;
                            }
                        })
                        .subscribe(new DefaultSubscriber<List<UserEntity>>() {
                            @Override
                            public void onNext(List<UserEntity> list) {
                                getView().onGetUserList(list, getUpdateType(page));
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(getUpdateType(page), errorStr);
                            }
                        })
        );
    }

    public void listStargazers(final int page) {
        if (owner == null || repo == null) {
            return;
        }
        if (isFlying) {
            getView().showOnError(getUpdateType(page), Utils.getString(R.string.reqest_flying));
            return;
        }
        isFlying = true;
        addSubscription(
                mRepositoryDataSource.listStargazers(owner, repo, page)
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                isFlying = false;
                            }
                        })
                        .subscribe(new DefaultSubscriber<List<UserEntity>>() {
                            @Override
                            public void onNext(List<UserEntity> list) {
                                getView().onGetUserList(list, getUpdateType(page));
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(getUpdateType(page), errorStr);
                            }
                        })
        );
    }

    public void listContributors(final int page) {
        if (owner == null || repo == null) {
            return;
        }
        if (isFlying) {
            getView().showOnError(getUpdateType(page), Utils.getString(R.string.reqest_flying));
            return;
        }
        isFlying = true;
        addSubscription(
                mRepositoryDataSource.listContributors(owner, repo, page)
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                isFlying = false;
                            }
                        })
                        .subscribe(new DefaultSubscriber<List<UserEntity>>() {
                            @Override
                            public void onNext(List<UserEntity> list) {
                                getView().onGetUserList(list, getUpdateType(page));
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(getUpdateType(page), errorStr);
                            }
                        })
        );
    }

    public void listFollowers(String userName, final int page) {
        if (isFlying) {
            getView().showOnError(getUpdateType(page), Utils.getString(R.string.reqest_flying));
            return;
        }
        isFlying = true;
        addSubscription(
                mUserDataSource.listFollowers(userName, page)
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                isFlying = false;
                            }
                        })
                        .subscribe(new DefaultSubscriber<List<UserEntity>>() {
                            @Override
                            public void onNext(List<UserEntity> list) {
                                getView().onGetUserList(list, getUpdateType(page));
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(getUpdateType(page), errorStr);
                            }
                        })
        );
    }

    public void listFollowing(String userName, final int page) {
        if (isFlying) {
            getView().showOnError(getUpdateType(page), Utils.getString(R.string.reqest_flying));
            return;
        }
        isFlying = true;
        addSubscription(
                mUserDataSource.listFollowing(userName, page)
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                isFlying = false;
                            }
                        })
                        .subscribe(new DefaultSubscriber<List<UserEntity>>() {
                            @Override
                            public void onNext(List<UserEntity> list) {
                                getView().onGetUserList(list, getUpdateType(page));
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(getUpdateType(page), errorStr);
                            }
                        })
        );
    }

    @Override
    public void getUserInfo(int position, RecyclerViewAdapter adapter) {
        userListPresenter.getUserInfo(position, adapter);
    }

    @Override
    public void followUser(int position, RecyclerViewAdapter adapter, boolean toggle) {
        userListPresenter.followUser(position, adapter, toggle);
    }

    public interface ListUserView extends BaseView, ShowError {
        void onGetUserList(List<UserEntity> list, @UpdateType int updateType);
    }
}
