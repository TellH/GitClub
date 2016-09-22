package tellh.com.gitclub.presentation.presenter;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.utils.RxJavaUtils;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.Note;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.presentation.contract.RepoPageContract;

/**
 * Created by tlh on 2016/9/22 :)
 */

public class RepoPagePresenter extends BasePresenter<RepoPageContract.View> implements RepoPageContract.Presenter {
    private final RepositoryDataSource mRepositoryDataSource;

    public RepoPagePresenter(RepositoryDataSource mRepositoryDataSource) {
        this.mRepositoryDataSource = mRepositoryDataSource;
    }

    @Override
    public void getRepoInfo(String owner, String repo) {
        addSubscription(
                mRepositoryDataSource.getRepoInfo(owner, repo)
                        .compose(RxJavaUtils.<RepositoryInfo>setLoadingListener(getView()))
                        .subscribe(new DefaultSubscriber<RepositoryInfo>() {
                            @Override
                            public void onNext(RepositoryInfo repositoryInfo) {
                                getView().showOnSuccess();
                                getView().onGetRepositoryInfo(repositoryInfo);
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(errorStr);
                            }
                        })
        );
    }

    @Override
    public void toFork(String owner, final String repo) {
        addSubscription(
                mRepositoryDataSource.toFork(owner, repo)
                        .subscribe(new DefaultSubscriber<RepositoryInfo>() {
                            @Override
                            public void onNext(RepositoryInfo repositoryInfo) {
                                if (repositoryInfo != null) {
                                    getView().showOnSuccess();
                                    Note.show(Utils.getString(R.string.success_fork_repo) + repo);
                                } else {
                                    getView().showOnError(Utils.getString(R.string.error_fork_repo) + repo);
                                }
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(Utils.getString(R.string.error_fork_repo) + repo);
                            }
                        })
        );
    }

    @Override
    public void toStar(String owner, final String repo) {
        addSubscription(
                mRepositoryDataSource.toStar(owner, repo)
                        .subscribe(new DefaultSubscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean result) {
                                if (result) {
                                    getView().showOnSuccess();
                                    Note.show(Utils.getString(R.string.success_star_repo) + repo);
                                } else {
                                    getView().showOnError(Utils.getString(R.string.error_star_repo) + repo);
                                }
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(Utils.getString(R.string.error_star_repo) + repo);
                            }
                        })
        );
    }

    @Override
    public void toWatch(String owner, final String repo) {
        addSubscription(
                mRepositoryDataSource.toWatch(owner, repo)
                        .subscribe(new DefaultSubscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean result) {
                                if (result) {
                                    getView().showOnSuccess();
                                    Note.show(Utils.getString(R.string.success_watch_repo) + repo);
                                } else {
                                    getView().showOnError(Utils.getString(R.string.error_watch_repo) + repo);
                                }
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(Utils.getString(R.string.error_watch_repo) + repo);
                            }
                        })
        );
    }

    @Override
    public void unStar(String owner, final String repo) {
        addSubscription(
                mRepositoryDataSource.unStar(owner, repo)
                        .subscribe(new DefaultSubscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean result) {
                                if (result) {
                                    getView().showOnSuccess();
                                    Note.show(Utils.getString(R.string.success_unstar_repo) + repo);
                                } else {
                                    getView().showOnError(Utils.getString(R.string.error_unstar_repo) + repo);
                                }
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(Utils.getString(R.string.error_unstar_repo) + repo);
                            }
                        })
        );
    }

    @Override
    public void unWatch(String owner, final String repo) {
        addSubscription(
                mRepositoryDataSource.unWatch(owner, repo)
                        .subscribe(new DefaultSubscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean result) {
                                if (result) {
                                    getView().showOnSuccess();
                                    Note.show(Utils.getString(R.string.success_unwatch_repo) + repo);
                                } else {
                                    getView().showOnError(Utils.getString(R.string.error_unwatch_repo) + repo);
                                }
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(Utils.getString(R.string.error_unwatch_repo) + repo);
                            }
                        })
        );
    }

    @Override
    public void checkStarred(String owner, final String repo) {
        addSubscription(
                mRepositoryDataSource.checkStarred(owner, repo)
                        .subscribe(new DefaultSubscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean result) {
                                getView().onCheckStarred(result);
                            }
                        })
        );
    }

    @Override
    public void checkWatch(String owner, final String repo) {
        addSubscription(
                mRepositoryDataSource.checkWatching(owner, repo)
                        .subscribe(new DefaultSubscriber<Boolean>() {
                            @Override
                            public void onNext(Boolean result) {
                                getView().onCheckWatch(result);
                            }
                        })
        );
    }


}
