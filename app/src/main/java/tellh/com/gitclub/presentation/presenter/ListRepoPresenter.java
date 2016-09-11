package tellh.com.gitclub.presentation.presenter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.common.base.BaseView;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.config.Constant.SortType.SortType_Repo;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.UserInfo;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.UpdateType;

/**
 * Created by tlh on 2016/9/11 :)
 */
public class ListRepoPresenter extends BasePresenter<ListRepoPresenter.ListRepoView> {
    private final UserDataSource mUserDataSource;
    private final IRepoListPresenter repoListPresenter;

    private UserInfo user;

    private ListStarredRepoRequest listStarredRepoRequest;
    private DialogManager dialogManager;

    private class ListStarredRepoRequest {
        int page;
        SortType_Repo sortType;

        public ListStarredRepoRequest(int page, SortType_Repo sortType) {
            this.page = page;
            this.sortType = sortType;
        }
    }

    public ListRepoPresenter(UserDataSource userDataSource, RepositoryDataSource repositoryDataSource, Context ctx) {
        this.mUserDataSource = userDataSource;
        repoListPresenter = new RepoListPresenter(this, repositoryDataSource);
        user = AccountPrefs.getLoginUser(ctx);
    }

    public void listStarredRepo(final int page) {
        if (user == null) {
            getView().showOnError(Utils.getString(R.string.error_not_login));
            return;
        }
        if (listStarredRepoRequest == null)
            listStarredRepoRequest = new ListStarredRepoRequest(1, SortType_Repo.CREATED);
        addSubscription(
                mUserDataSource.listStarredRepo(user.getLogin(), listStarredRepoRequest.sortType,
                        listStarredRepoRequest.page)
                        .subscribe(new DefaultSubscriber<List<RepositoryInfo>>() {
                            @Override
                            public void onNext(List<RepositoryInfo> repositoryInfos) {
                                getView().onGetRepoList(repositoryInfos, getUpdateType(page));
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(errorStr, getUpdateType(page));
                            }
                        })
        );
    }

    public void listWatchingRepo(final int page) {
        if (user == null) {
            getView().showOnError(Utils.getString(R.string.error_not_login));
            return;
        }
        addSubscription(
                mUserDataSource.listWatchingRepo(user.getLogin(), page)
                        .subscribe(new DefaultSubscriber<List<RepositoryInfo>>() {
                            @Override
                            public void onNext(List<RepositoryInfo> repositoryInfos) {
                                getView().onGetRepoList(repositoryInfos, getUpdateType(page));
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(errorStr, getUpdateType(page));
                            }
                        })
        );
    }

    public void listOwnRepo(final int page) {
        if (user == null) {
            getView().showOnError(Utils.getString(R.string.error_not_login));
            return;
        }
        addSubscription(
                mUserDataSource.listOwnRepo(user.getLogin(), page)
                        .subscribe(new DefaultSubscriber<List<RepositoryInfo>>() {
                            @Override
                            public void onNext(List<RepositoryInfo> repositoryInfos) {
                                getView().onGetRepoList(repositoryInfos, getUpdateType(page));
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(errorStr, getUpdateType(page));
                            }
                        })
        );
    }

    @Override
    public void attachView(ListRepoView view) {
        super.attachView(view);
        dialogManager = new DialogManager();
    }

    public MaterialDialog getDialogSortRepo() {
        return dialogManager.dialogSortRepo;
    }

    private class DialogManager {
        private MaterialDialog dialogSortRepo;
        Context viewContext = getView().getViewContext();

        public DialogManager() {
            dialogSortRepo = new MaterialDialog.Builder(viewContext)
                    .title(R.string.title_sort_repo)
                    .items(SortType_Repo.getDisplayStringListForStarredRepo())
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            listStarredRepoRequest.sortType = (SortType_Repo) SortType_Repo.lookup(text.toString());
                            listStarredRepo(1);
                            return true;
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                            materialDialog.dismiss();
                        }
                    })
                    .positiveText("OK")
                    .negativeText("Cancel")
                    .negativeColorRes(R.color.red)
                    .build();
            dialogSortRepo.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    dialogSortRepo.setSelectedIndex(listStarredRepoRequest.sortType.ordinal());
                }
            });
        }
    }

    public interface ListRepoView extends BaseView {
        void onGetRepoList(List<RepositoryInfo> list, UpdateType updateType);

        void showOnError(String msg, UpdateType updateType);
    }
}
