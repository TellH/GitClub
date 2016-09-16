package tellh.com.gitclub.presentation.presenter;

import android.content.Context;

import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.model.entity.UserInfo;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;
import tellh.com.gitclub.presentation.contract.PersonalPageContract;

public class PersonalPagePresenter extends BasePresenter<PersonalPageContract.View> implements PersonalPageContract.Presenter {
    private final UserDataSource mUserDataSource;
    private final RepositoryDataSource mRepositoryDataSource;

    private UserInfo user;

    public PersonalPagePresenter(RepositoryDataSource repositoryDataSource, UserDataSource userDataSource, Context context) {
        mRepositoryDataSource = repositoryDataSource;
        mUserDataSource = userDataSource;
        AccountPrefs.getLoginUser(context);
    }

}