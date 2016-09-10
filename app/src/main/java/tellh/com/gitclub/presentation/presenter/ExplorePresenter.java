package tellh.com.gitclub.presentation.presenter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.config.Constant.LangTrending;
import tellh.com.gitclub.common.utils.RxJavaUtils;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.ShowCase;
import tellh.com.gitclub.model.entity.ShowCaseInfo;
import tellh.com.gitclub.model.entity.Trending;
import tellh.com.gitclub.model.net.DataSource.ExploreDataSource;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.presentation.contract.ExploreContract;
import tellh.com.gitclub.presentation.view.adapter.BaseRecyclerAdapter;

import static tellh.com.gitclub.common.config.Constant.Since;

public class ExplorePresenter extends BasePresenter<ExploreContract.View>
        implements ExploreContract.Presenter {
    private final ExploreDataSource mExploreDataSource;

    private LangTrending curLanguage;
    private Since curSince;

    //use combination instead of inherit.
    private final IRepoListPresenter repoListPresenter;

    private DialogManager dialogManager;

    public ExplorePresenter(ExploreDataSource exploreDataSource, RepositoryDataSource repositoryDataSource) {
        mExploreDataSource = exploreDataSource;
        repoListPresenter = new RepoListPresenter(this, repositoryDataSource);

        curLanguage = LangTrending.ALL;
        curSince = Since.TODAY;
    }

    @Override
    public void attachView(ExploreContract.View mvpView) {
        super.attachView(mvpView);
        dialogManager = new DialogManager();
    }

    public void listTrending(LangTrending language, Since since) {
        Map<String, String> params = new LinkedHashMap<>();
        if (language != LangTrending.ALL)
            params.put(LangTrending.key(), language.toString());
        params.put(Since.key(), since.toString());
        addSubscription(
                mExploreDataSource.listTrending(params)
                        .subscribe(new DefaultSubscriber<List<Trending>>() {
                            @Override
                            public void onNext(List<Trending> trendings) {
                                getView().onGetTrending(trendings);
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(Utils.getString(R.string.error_get_trending) + errorStr,
                                        ExploreContract.ListType.TRENDING);
                            }
                        })
        );
    }


    @Override
    public void listTrending() {
        listTrending(curLanguage, curSince);
    }

    @Override
    public void listShowCase() {
        addSubscription(
                mExploreDataSource.listShowCase()
                        .subscribe(new DefaultSubscriber<List<ShowCase>>() {
                            @Override
                            public void onNext(List<ShowCase> showCases) {
                                getView().onGetShowcases(showCases);
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(Utils.getString(R.string.error_get_showcases) + errorStr,
                                        ExploreContract.ListType.SHOWCASES);
                            }
                        })
        );
    }

    @Override
    public void getShowcaseDetail(final ShowCase showcase) {
        addSubscription(
                mExploreDataSource.getShowCaseDetail(showcase.getSlug())
                        .compose(RxJavaUtils.<ShowCaseInfo>setLoadingListener(getView()))
                        .subscribe(new DefaultSubscriber<ShowCaseInfo>() {
                            @Override
                            public void onNext(ShowCaseInfo showCaseInfo) {
                                showCaseInfo.setImage(showcase.getImage_url());
                                getView().onGetShowcasesDetail(showCaseInfo);
                                getView().showOnSuccess();
                            }

                            @Override
                            protected void onError(String errorStr) {
                                getView().showOnError(Utils.getString(R.string.error_get_showcases) + errorStr);
                            }
                        })
        );
    }

    @Override
    public void checkState(int position, BaseRecyclerAdapter<RepositoryInfo> adapter) {
        repoListPresenter.checkState(position, adapter);
    }

    @Override
    public void starRepo(int position, BaseRecyclerAdapter<RepositoryInfo> adapter, boolean toggle) {
        repoListPresenter.starRepo(position, adapter, toggle);
    }

    @Override
    public void watchRepo(int position, BaseRecyclerAdapter<RepositoryInfo> adapter, boolean toggle) {
        repoListPresenter.watchRepo(position, adapter, toggle);
    }

    @Override
    public void forkRepo(int position, BaseRecyclerAdapter<RepositoryInfo> adapter) {
        repoListPresenter.forkRepo(position, adapter);
    }

    @Override
    public void getRepoInfo(String owner, String name, OnGetRepoCallback callback) {
        repoListPresenter.getRepoInfo(owner, name, callback);
    }

    public class DialogManager {
        private final MaterialDialog dialogSince;
        private MaterialDialog dialogLang;

        public DialogManager() {
            Context viewContext = getView().getViewContext();
            //language dialog
            dialogLang = new MaterialDialog.Builder(viewContext)
                    .title(R.string.title_program_language)
                    .items(LangTrending.getDisplayStringList())
                    .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                            LangTrending[] languageMap = LangTrending.values();
                            curLanguage = languageMap[i];
                            listTrending(curLanguage, curSince);
                            return false;
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
            dialogSince = new MaterialDialog.Builder(viewContext)
                    .title(R.string.title_since)
                    .items(Since.getDisplayStringList())
                    .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                            Since[] sinces = Since.values();
                            curSince = sinces[i];
                            listTrending(curLanguage, curSince);
                            return false;
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
        }
    }

    @Override
    public MaterialDialog getDialogLang() {
        return dialogManager.dialogLang;
    }

    @Override
    public MaterialDialog getDialogSince() {
        return dialogManager.dialogSince;
    }
}