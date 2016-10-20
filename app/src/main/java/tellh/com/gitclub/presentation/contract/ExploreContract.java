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
import tellh.com.gitclub.model.entity.ShowCase;
import tellh.com.gitclub.model.entity.ShowCaseInfo;
import tellh.com.gitclub.model.entity.Trending;
import tellh.com.gitclub.presentation.presenter.IRepoListPresenter;
import tellh.com.gitclub.presentation.view.fragment.search.ListLoadingListener;

public interface ExploreContract {
    int SHOWCASES = 0;
    int TRENDING = 1;
    int GANK_IO = 2;
    int ARSENAL = 3;

    @IntDef({SHOWCASES, TRENDING, GANK_IO,ARSENAL})
    @Retention(RetentionPolicy.SOURCE)
    @interface ListType {
    }

    interface View extends BaseView, ShowError {
        void showOnError(String msg, @ListType int type);

        void onGetTrending(List<Trending> trendings);

        void onGetShowcases(List<ShowCase> showCases);

        void onGetGankData(List<RepositoryInfo> repositoryList, @UpdateType int updateType);

        void onGetShowcasesDetail(ShowCaseInfo showCaseInfo);

        void onGetArsenalData(List<RepositoryInfo> repositoryList, int updateType);
    }

    interface Presenter extends MvpPresenter<View>, IRepoListPresenter {

        void listTrending();

        void listShowCase();

        void getShowcaseDetail(ShowCase showCase);

        void listGankData(int page);

        void listArsenalData(int page);

        MaterialDialog getDialogLang();

        MaterialDialog getDialogSince();
    }

    interface OnListFragmentInteractListener {
        void onFetchData(@ListType int type, int page);

        Presenter getPresenter();
    }

    interface OnGetTrendingListener extends ListLoadingListener {
        void onGet(List<Trending> trendings);
    }

    interface OnGetGankDataListener extends ListLoadingListener {
        void onGet(List<RepositoryInfo> repositoryList, @UpdateType int UpdateType);
    }

    interface onGetShowcasesListener extends ListLoadingListener {
        void onGet(List<ShowCase> showCases);
    }
}