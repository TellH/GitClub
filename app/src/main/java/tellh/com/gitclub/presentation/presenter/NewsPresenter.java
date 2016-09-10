package tellh.com.gitclub.presentation.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.model.entity.Event;
import tellh.com.gitclub.model.entity.UserInfo;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;
import tellh.com.gitclub.presentation.contract.NewsContract;

import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.UpdateType;

public class NewsPresenter extends BasePresenter<NewsContract.View> implements NewsContract.Presenter {
    private final UserDataSource mUserDataSource;
    private final RepositoryDataSource mRepositoryDataSource;
    private final Context mCtx;

    public NewsPresenter(RepositoryDataSource repositoryDataSource, UserDataSource userDataSource, Context context) {
        mRepositoryDataSource = repositoryDataSource;
        mUserDataSource = userDataSource;
        mCtx = context;
    }

    @Override
    public void listNews(final int page) {
        UserInfo user = AccountPrefs.getLoginUser(mCtx);
        if (user == null) {
            getView().showOnError(mCtx.getString(R.string.error_not_login));
            // TODO: 2016/9/8 go to login activity
            return;
        }
        addSubscription(mUserDataSource.listNews(user.getLogin(), page)
                .subscribe(new DefaultSubscriber<List<Event>>() {
                    @Override
                    public void onNext(List<Event> events) {
                        getView().OnGetNews(events, getUpdateType(page));
                        getView().showOnSuccess();
                    }

                    @Override
                    protected void onError(String errorStr) {
                        getView().showOnError(mCtx.getString(R.string.error_get_news) + errorStr);
                    }
                }));
    }

    @NonNull
    private UpdateType getUpdateType(int page) {
        return page == 1 ? UpdateType.REFRESH : UpdateType.LOAD_MORE;
    }
}