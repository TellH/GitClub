package tellh.com.gitclub.presentation.contract;


import android.support.annotation.IntDef;

import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import tellh.com.gitclub.common.base.BaseView;
import tellh.com.gitclub.common.base.MvpPresenter;
import tellh.com.gitclub.model.entity.ShowCase;
import tellh.com.gitclub.model.entity.ShowCaseInfo;
import tellh.com.gitclub.model.entity.Trending;
import tellh.com.gitclub.presentation.presenter.IRepoListPresenter;
import tellh.com.gitclub.presentation.view.fragment.search.ListLoadingListener;

public interface ExploreContract {
    int SHOWCASES = 0;
    int TRENDING = 1;

    @IntDef({SHOWCASES, TRENDING})
    @Retention(RetentionPolicy.SOURCE)
    @interface ListType {
    }

    interface View extends BaseView {
        void showOnError(String msg, @ListType int type);

        void onGetTrending(List<Trending> trendings);

        void onGetShowcases(List<ShowCase> showCases);

        void onGetShowcasesDetail(ShowCaseInfo showCaseInfo);
    }

    interface Presenter extends MvpPresenter<View>, IRepoListPresenter {

        void listTrending();

        void listShowCase();

        void getShowcaseDetail(ShowCase showCase);

        MaterialDialog getDialogLang();

        MaterialDialog getDialogSince();
    }

    interface OnListFragmentInteractListener {
        void onFetchData(@ListType int type);

        Presenter getPresenter();
    }

    interface OnGetTrendingListener extends ListLoadingListener {
        void onGet(List<Trending> trendings);
    }

    interface onGetShowcasesListener extends ListLoadingListener {
        void onGet(List<ShowCase> showCases);
    }
}