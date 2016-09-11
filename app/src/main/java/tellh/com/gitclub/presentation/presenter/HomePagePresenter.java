package tellh.com.gitclub.presentation.presenter;

import android.content.Context;

import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.model.entity.UserInfo;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;
import tellh.com.gitclub.presentation.contract.HomePageContract;

public class HomePagePresenter extends BasePresenter<HomePageContract.View> implements HomePageContract.Presenter {
    private final UserDataSource mUserDataSource;
    private final RepositoryDataSource mRepositoryDataSource;

    private UserInfo user;

    public HomePagePresenter(RepositoryDataSource repositoryDataSource, UserDataSource userDataSource, Context context) {
        mRepositoryDataSource = repositoryDataSource;
        mUserDataSource = userDataSource;
        AccountPrefs.getLoginUser(context);
    }

}