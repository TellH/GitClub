package tellh.com.gitclub.presentation.presenter;


import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;

import java.util.Arrays;

import rx.functions.Action0;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.config.Constant.Language;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.Note;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.SearchResult;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;
import tellh.com.gitclub.presentation.contract.SearchContract;

import static tellh.com.gitclub.common.config.Constant.SortType.SortType_Repo;
import static tellh.com.gitclub.common.config.Constant.SortType.SortType_User;
import static tellh.com.gitclub.presentation.contract.SearchContract.Presenter;
import static tellh.com.gitclub.presentation.contract.SearchContract.REPO;
import static tellh.com.gitclub.presentation.contract.SearchContract.SearchEntity;
import static tellh.com.gitclub.presentation.contract.SearchContract.USER;

public class SearchPresenter extends BasePresenter<SearchContract.View> implements Presenter {

    private final UserDataSource mUserDataSource;
    private final RepositoryDataSource mRepositoryDataSource;

    //managing 3 dialog
    private DialogManager dialogManager;
    //use combination instead of inherit.
    private final IRepoListPresenter repoListPresenter;
    private final UserListPresenter userListPresenter;

    private SearchEntity currentSearchEntity;
    private SearchEntity repoSearchEntity = new SearchEntity(REPO);
    private SearchEntity userSearchEntity = new SearchEntity(USER);

    public SearchPresenter(RepositoryDataSource repositoryDataSource, UserDataSource userDataSource) {
        mRepositoryDataSource = repositoryDataSource;
        mUserDataSource = userDataSource;
        repoListPresenter = new RepoListPresenter(this, repositoryDataSource);
        userListPresenter = new UserListPresenter(this, userDataSource);
    }

    @Override
    public void attachView(SearchContract.View mvpView) {
        super.attachView(mvpView);
        dialogManager = new DialogManager();
    }

    @Override
    public void setCurrentSearchEntity(SearchEntity currentSearchEntity) {
        this.currentSearchEntity = currentSearchEntity;
    }

    @Override
    public SearchEntity getCurrentSearchEntity() {
        return currentSearchEntity;
    }

    @Override
    public SearchEntity getRepoSearchEntity() {
        return repoSearchEntity;
    }

    @Override
    public SearchEntity getUserSearchEntity() {
        return userSearchEntity;
    }

    @Override
    public void initialSearch() {
        repoSearchEntity.keyWord = null;
        repoSearchEntity.language = Language.ALL.val();
        repoSearchEntity.sortType = SortType_Repo.STARS;
        userSearchEntity.keyWord = null;
        userSearchEntity.language = Language.ALL.val();
        userSearchEntity.sortType = SortType_User.FOLLOWERS;
        searchRepo(1);
        searchUser(1);
    }

    @Override
    public void searchCurrent(boolean fromSearchView, int page) {
        if (fromSearchView) {
            currentSearchEntity.sortType = currentSearchEntity.sortType.getBestMatch();
            currentSearchEntity.language = null;
        }
        if (currentSearchEntity.type == REPO)
            searchRepo(page);
        else searchUser(page);
    }

    @Override
    public void searchRepo(int page) {
        if (page == 1)
            getView().showListRefreshLoading(REPO);
        searchRepo(repoSearchEntity.keyWord, repoSearchEntity.language, (SortType_Repo) repoSearchEntity.sortType, page);
        if (!TextUtils.isEmpty(repoSearchEntity.keyWord))
            Note.showBar("Searching Repository: " + repoSearchEntity.keyWord, ((Fragment) getView()).getView());
    }

    @Override
    public void searchUser(int page) {
        if (page == 1)
            getView().showListRefreshLoading(USER);
        if (Language.ALL.val().equals(userSearchEntity.language))
            userSearchEntity.language = Language.JAVA.val();
        searchUser(userSearchEntity.keyWord, userSearchEntity.language, (SortType_User) userSearchEntity.sortType, page);
        if (!TextUtils.isEmpty(userSearchEntity.keyWord))
            Note.showBar("Searching User: " + userSearchEntity.keyWord, ((Fragment) getView()).getView());
    }

    public void searchRepo(String keyWord, String language, SortType_Repo sort, final int page) {
        if (repoSearchEntity.isFlying) {
            if (getUpdateType(page) == repoSearchEntity.updateType)
                Note.show(Utils.getString(R.string.reqest_flying));
            else
                getView().showOnError(Utils.getString(R.string.reqest_flying), REPO, getUpdateType(page));
            return;
        }
        repoSearchEntity.isFlying = true;
        repoSearchEntity.updateType = getUpdateType(page);
        addSubscription(mRepositoryDataSource.search(keyWord, language, sort, page)
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        repoSearchEntity.isFlying = false;
                    }
                })
                .subscribe(new DefaultSubscriber<SearchResult<RepositoryInfo>>() {
                    @Override
                    public void onNext(SearchResult<RepositoryInfo> result) {
                        getView().onGetRepos(result.getTotal_count(), result.getItems(), getUpdateType(page));
                        getView().showOnSuccess();
                    }

                    @Override
                    protected void onError(String errorStr) {
                        getView().showOnError(Utils.getString(R.string.error_search_repo) + errorStr, REPO, getUpdateType(page));
                    }
                }));
    }

    public void searchUser(String keyWord, String language, SortType_User sort, final int page) {
        if (userSearchEntity.isFlying) {
            if (getUpdateType(page) == userSearchEntity.updateType)
                Note.show(Utils.getString(R.string.reqest_flying));
            else
                getView().showOnError(Utils.getString(R.string.reqest_flying), USER, getUpdateType(page));
            return;
        }
        userSearchEntity.isFlying = true;
        userSearchEntity.updateType = getUpdateType(page);
        addSubscription(mUserDataSource.search(keyWord, language, sort, page)
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        userSearchEntity.isFlying = false;
                    }
                })
                .subscribe(new DefaultSubscriber<SearchResult<UserEntity>>() {
                    @Override
                    public void onNext(SearchResult<UserEntity> result) {
                        getView().onGetUsers(result.getTotal_count(), result.getItems(), getUpdateType(page));
                        getView().showOnSuccess();
                    }

                    @Override
                    protected void onError(String errorStr) {
                        getView().showOnError(Utils.getString(R.string.error_search_user) + errorStr, USER, getUpdateType(page));
                    }
                }));
    }

    @Override
    public void checkState(int position, RecyclerViewAdapter adapter) {
        repoListPresenter.checkState(position, adapter);
    }

    @Override
    public void starRepo(int position, RecyclerViewAdapter adapter, boolean toggle) {
        repoListPresenter.starRepo(position, adapter, toggle);
    }

    @Override
    public void watchRepo(int position, RecyclerViewAdapter adapter, boolean toggle) {
        repoListPresenter.watchRepo(position, adapter, toggle);
    }

    @Override
    public void forkRepo(int position, RecyclerViewAdapter adapter) {
        repoListPresenter.forkRepo(position, adapter);
    }

    @Override
    public void getRepoInfo(String owner, String name, OnGetRepoCallback callback) {
        repoListPresenter.getRepoInfo(owner, name, callback);
    }

    @Override
    public void getUserInfo(int position, RecyclerViewAdapter adapter) {
        userListPresenter.getUserInfo(position, adapter);
    }

    @Override
    public void followUser(int position, RecyclerViewAdapter adapter, boolean toggle) {
        userListPresenter.followUser(position, adapter, toggle);
    }

    private class DialogManager {
        private MaterialDialog dialogLang;
        private MaterialDialog dialogSortRepo;
        private MaterialDialog dialogSortUser;

        public DialogManager() {
            Context viewContext = getView().getViewContext();
            //language dialog
            Integer[] choice = {0};
            dialogLang = new MaterialDialog.Builder(viewContext)
                    .title(R.string.title_program_language)
                    .items(Language.getDisplayStringList())
                    .itemsCallbackMultiChoice(choice, new MaterialDialog.ListCallbackMultiChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                            dialog.setSelectedIndices(which);
                            StringBuilder langBuilder = new StringBuilder();
                            Language[] languageMap = Language.values();
                            for (Integer integer : which) {
                                if (integer == Language.ALL.ordinal()) {
                                    which[0] = which[which.length - 1];
                                    dialogLang.setSelectedIndices(Arrays.copyOf(which, which.length - 1));
                                    continue;
                                }
                                langBuilder.append(languageMap[integer].val());
                            }
                            if (langBuilder.length() != 0) {
                                currentSearchEntity.language = langBuilder.toString();
                                searchCurrent(false, 1);
                            }
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

            //sort type dalog
            dialogSortRepo = new MaterialDialog.Builder(viewContext)
                    .title(R.string.title_sort_repo)
                    .items(SortType_Repo.getDisplayStringList())
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            repoSearchEntity.sortType = SortType_Repo.lookup(text.toString());
                            searchRepo(1);
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

            dialogSortUser = new MaterialDialog.Builder(viewContext)
                    .title(R.string.title_sort_user)
                    .items(SortType_User.getDisplayStringList())
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            userSearchEntity.sortType = SortType_User.lookup(text.toString());
                            searchUser(1);
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

            dialogSortUser.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    dialogSortUser.setSelectedIndex(((SortType_User) userSearchEntity.sortType).ordinal());
                }
            });
            dialogSortRepo.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialogInterface) {
                    dialogSortRepo.setSelectedIndex(((SortType_Repo) repoSearchEntity.sortType).ordinal());
                }
            });
        }

    }

    @Override
    public MaterialDialog getDialogLang() {
        return dialogManager.dialogLang;
    }

    @Override
    public MaterialDialog getDialogSortRepo() {
        return dialogManager.dialogSortRepo;
    }

    @Override
    public MaterialDialog getDialogSortUser() {
        return dialogManager.dialogSortUser;
    }
}