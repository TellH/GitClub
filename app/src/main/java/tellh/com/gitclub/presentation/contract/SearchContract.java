package tellh.com.gitclub.presentation.contract;


import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import tellh.com.gitclub.common.base.BaseView;
import tellh.com.gitclub.common.base.MvpPresenter;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.presentation.presenter.IRepoListPresenter;
import tellh.com.gitclub.presentation.presenter.IUserListPresenter;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.UpdateType;
import tellh.com.gitclub.presentation.view.fragment.search.ListLoadingListener;

import static tellh.com.gitclub.common.config.Constant.SortType;

public interface SearchContract {

    enum ListType {
        REPO, USER
    }

    interface View extends BaseView {
        void onGetRepos(int total_count, List<RepositoryInfo> items, UpdateType updateType);

        void onGetUsers(int total_count, List<UserEntity> items, UpdateType updateType);

        void showListRefreshLoading(ListType listType);

        void showOnError(String msg, ListType type);
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
        void onFetchPage(ListType type, int page);

        Presenter getPresenter();
    }

    interface OnGetReposListener extends ListLoadingListener {
        void onGetRepos(int total_count, List<RepositoryInfo> items, UpdateType updateType);
    }

    interface OnGetUserListener extends ListLoadingListener {

        void onGetUser(int total_count, List<UserEntity> items, UpdateType updateType);
    }

    class SearchEntity {
        public SearchEntity(ListType type) {
            this.type = type;
        }

        public boolean isFlying;
        public ListType type;
        public String keyWord;
        public SortType sortType;
        public String language;
        public UpdateType updateType;
    }
}