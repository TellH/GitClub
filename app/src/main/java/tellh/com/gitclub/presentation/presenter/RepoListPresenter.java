package tellh.com.gitclub.presentation.presenter;

import com.tellh.nolistadapter.DataBean;
import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.Note;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;

/**
 * Created by tlh on 2016/8/31 :)
 */
class RepoListPresenter implements IRepoListPresenter {
    private final RepositoryDataSource mRepositoryDataSource;
    private final BasePresenter mPresenter;

    RepoListPresenter(BasePresenter presenter, RepositoryDataSource repositoryDataSource) {
        this.mRepositoryDataSource = repositoryDataSource;
        this.mPresenter = presenter;
    }

    @Override
    public void checkState(final int position, final RecyclerViewAdapter adapter) {
        if (!mPresenter.checkNetwork() || !mPresenter.checkLogin())
            return;
        List<DataBean> items = adapter.getDisplayList();
        final RepositoryInfo repositoryInfo = (RepositoryInfo) items.get(position);
        String owner = repositoryInfo.getOwner().getLogin();
        String repo = repositoryInfo.getName();
        mPresenter.addSubscription(
                Observable.zip(
                        mRepositoryDataSource.checkStarred(owner, repo),
                        mRepositoryDataSource.checkWatching(owner, repo),
                        new Func2<Boolean, Boolean, Void>() {
                            @Override
                            public Void call(Boolean starring, Boolean watching) {
                                boolean change = repositoryInfo.hasStarred != starring || repositoryInfo.hasWatched != watching;
                                repositoryInfo.hasStarred = starring;
                                repositoryInfo.hasWatched = watching;
                                repositoryInfo.hasCheckState = true;
                                if (change)
                                    adapter.notifyItemChanged(position);
                                return null;
                            }
                        }
                ).subscribe(new DefaultSubscriber<Void>() {
                    @Override
                    public void onNext(Void aVoid) {
                    }
                })
        );

    }

    @Override
    public void starRepo(final int position, final RecyclerViewAdapter adapter, boolean toggle) {
        if (!mPresenter.checkNetwork())
            return;
        final RepositoryInfo repo = (RepositoryInfo) adapter.getDisplayList().get(position);
        //to star
        if (toggle) {
            mPresenter.addSubscription(
                    mRepositoryDataSource.toStar(repo.getOwner().getLogin(), repo.getName())
                            .subscribe(new DefaultSubscriber<Boolean>() {
                                           @Override
                                           public void onNext(Boolean result) {
                                               if (result) {
                                                   Note.show(Utils.getString(R.string.success_star_repo) + repo.getFull_name());
                                                   repo.hasStarred = true;
                                               } else {
                                                   handleStarError(repo);
                                               }
                                           }

                                           @Override
                                           protected void onError(String errorStr) {
                                               super.onError(errorStr);
                                               handleStarError(repo);
                                           }
                                       }
                            )
            );
        } else {//unStar
            mPresenter.addSubscription(
                    mRepositoryDataSource.unStar(repo.getOwner().getLogin(), repo.getName())
                            .subscribe(
                                    new DefaultSubscriber<Boolean>() {
                                        @Override
                                        public void onNext(Boolean result) {
                                            if (result) {
                                                Note.show(Utils.getString(R.string.success_unstar_repo) + repo.getFull_name());
                                                repo.hasStarred = false;
                                            } else {
                                                handleStarError(repo);
                                            }
                                        }

                                        @Override
                                        protected void onError(String errorStr) {
                                            super.onError(errorStr);
                                            handleStarError(repo);
                                        }
                                    }
                            )
            );
        }

    }

    private void handleStarError(RepositoryInfo repo) {
        Note.show(Utils.getString(R.string.error_star_repo) + repo.getFull_name());
        repo.setStars(repo.getStars() - 1);
    }

    @Override
    public void watchRepo(final int position, final RecyclerViewAdapter adapter, boolean toggle) {
        if (!mPresenter.checkNetwork())
            return;
        final RepositoryInfo repo = (RepositoryInfo) adapter.getDisplayList().get(position);
        //to watch
        if (toggle) {
            mPresenter.addSubscription(
                    mRepositoryDataSource.toWatch(repo.getOwner().getLogin(), repo.getName())
                            .subscribe(
                                    new DefaultSubscriber<Boolean>() {
                                        @Override
                                        public void onNext(Boolean result) {
                                            if (result) {
                                                Note.show(Utils.getString(R.string.success_watch_repo) + repo.getFull_name());
                                                repo.hasWatched = true;
                                            } else {
                                                Note.show(Utils.getString(R.string.error_watch_repo) + repo.getFull_name());
                                            }
                                        }

                                        @Override
                                        protected void onError(String errorStr) {
                                            super.onError(errorStr);
                                            Note.show(Utils.getString(R.string.error_watch_repo) + repo.getFull_name());
                                        }
                                    }
                            )
            );
        } else {//to unWatch
            mPresenter.addSubscription(
                    mRepositoryDataSource.unWatch(repo.getOwner().getLogin(), repo.getName())
                            .subscribe(
                                    new DefaultSubscriber<Boolean>() {
                                        @Override
                                        public void onNext(Boolean result) {
                                            if (result) {
                                                Note.show(Utils.getString(R.string.success_unwatch_repo) + repo.getFull_name());
                                                repo.hasStarred = false;
                                            } else {
                                                Note.show(Utils.getString(R.string.error_unwatch_repo) + repo.getFull_name());
                                            }
                                        }

                                        @Override
                                        protected void onError(String errorStr) {
                                            super.onError(errorStr);
                                            Note.show(Utils.getString(R.string.error_unwatch_repo) + repo.getFull_name());
                                        }
                                    }
                            )
            );
        }
    }

    @Override
    public void forkRepo(final int position, final RecyclerViewAdapter adapter) {
        if (!mPresenter.checkNetwork())
            return;
        final RepositoryInfo repo = (RepositoryInfo) adapter.getDisplayList().get(position);
        Note.show(Utils.getString(R.string.Forking) + repo.getFull_name());
        mPresenter.addSubscription(
                mRepositoryDataSource.toFork(repo.getOwner().getLogin(), repo.getName())
                        .map(new Func1<RepositoryInfo, Boolean>() {
                            @Override
                            public Boolean call(RepositoryInfo repositoryInfo) {
                                return repositoryInfo != null;
                            }
                        })
                        .subscribe(
                                new DefaultSubscriber<Boolean>() {
                                    @Override
                                    public void onNext(Boolean result) {
                                        if (result) {
                                            Note.show(Utils.getString(R.string.success_fork_repo) + repo.getFull_name());
                                        } else {
                                            Note.show(Utils.getString(R.string.error_fork_repo) + repo.getFull_name());
                                        }
                                    }

                                    @Override
                                    protected void onError(String errorStr) {
                                        super.onError(errorStr);
                                        Note.show(Utils.getString(R.string.error_fork_repo) + repo.getFull_name());
                                    }

                                }
                        )
        );

    }

    @Override
    public void getRepoInfo(String owner, String name, final OnGetRepoCallback callback) {
        mPresenter.addSubscription(
                mRepositoryDataSource.getRepoInfo(owner, name)
                        .subscribe(new DefaultSubscriber<RepositoryInfo>() {
                            @Override
                            public void onNext(RepositoryInfo repositoryInfo) {
                                if (callback != null)
                                    callback.onGet(repositoryInfo);
                            }
                        })
        );
    }
}
