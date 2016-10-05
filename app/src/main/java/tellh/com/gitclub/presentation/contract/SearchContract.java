package tellh.com.gitclub.presentation.contract;


import android.support.annotation.IntDef;

import com.afollestad.materialdialogs.MaterialDialog;
import com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.UpdateType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import tellh.com.gitclub.common.base.BaseView;
import tellh.com.gitclub.common.base.MvpPresenter;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.presentation.presenter.IRepoListPresenter;
import tellh.com.gitclub.presentation.presenter.IUserListPresenter;
import tellh.com.gitclub.presentation.view.fragment.search.ListLoadingListener;

import static tellh.com.gitclub.common.config.Constant.SortType;

public interface SearchContract {
    int REPO = 0;
    int USER = 1;

    @IntDef({REPO, USER})
    @Retention(RetentionPolicy.SOURCE)
    @interface ListType {
    }

    interface View extends BaseView {
        void onGetRepos(int total_count, List<RepositoryInfo> items, @UpdateType int updateType);

        void onGetUsers(int total_count, List<UserEntity> items, @UpdateType int updateType);

        void showListRefreshLoading(@ListType int listType);

        void showOnError(String msg, @ListType int type, @UpdateType int updateType);
    }

    interface Presenter extends MvpPresenter<View>, IRepoListPresenter, IUserListPresenter {

        void setCurrentSearchEntity(SearchEntity currentSearchEntity);

        SearchEntity getCurrentSearchEntity();

        SearchEntity getRepoSearchEntity();

        SearchEntity getUserSearchEntity();

        void initialSearch();

        void searchCurrent(boolean fromSearchView, int page);

        void searchRepo(int page);

        void searchUser(int page);

        MaterialDialog getDialogLang();

        MaterialDialog getDialogSortRepo();

        MaterialDialog getDialogSortUser();
    }

    interface OnListFragmentInteractListener {
        void onFetchPage(@ListType int type, int page);

        Presenter getPresenter();
    }

    interface OnGetReposListener extends ListLoadingListener {
        void onGetRepos(int total_count, List<RepositoryInfo> items, @UpdateType int updateType);
    }

    interface OnGetUserListener extends ListLoadingListener {

        void onGetUser(int total_count, List<UserEntity> items, @UpdateType int updateType);
    }

    class SearchEntity {
        public SearchEntity(@ListType int type) {
            this.type = type;
        }

        public boolean isFlying;
        @ListType
        public int type;
        public String keyWord;
        public SortType sortType;
        public String language;
        @UpdateType
        public int updateType;
    }
}