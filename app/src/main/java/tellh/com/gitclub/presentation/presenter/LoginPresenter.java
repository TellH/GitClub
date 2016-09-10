package tellh.com.gitclub.presentation.presenter;

import rx.functions.Action1;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.utils.RxJavaUtils;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;
import tellh.com.gitclub.presentation.contract.LoginContract;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {
    private UserDataSource mDataSource;

    public LoginPresenter(UserDataSource dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public void login(String username, String password) {
        if (!checkNetwork())
            return;
        addSubscription(mDataSource.login(username, password)
                .compose(RxJavaUtils.<Boolean>setLoadingListener(getView()))
                .subscribe(new DefaultSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean success) {
                        if (success)
                            getView().showOnSuccess();
                        else getView().showOnError(Utils.getString(R.string.error_login));
                    }

                    @Override
                    protected void onError(String errorStr) {
                        getView().showOnError(Utils.getString(R.string.error_login) + "Check your name and password.");
                    }
                }));
    }

    @Override
    public void signOut() {
        mDataSource.signOut()
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean success) {
                        if (success)
                            getView().showOnSuccess();
                        else getView().showOnError(Utils.getString(R.string.error_sign_out));
                    }
                });
    }

    @Override
    public String getLoginUserName() {
        return mDataSource.getLoginUserName();
    }

}