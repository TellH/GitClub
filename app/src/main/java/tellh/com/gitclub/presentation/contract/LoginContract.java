package tellh.com.gitclub.presentation.contract;

import tellh.com.gitclub.common.base.BaseView;
import tellh.com.gitclub.common.base.MvpPresenter;

public interface LoginContract {

    interface View extends BaseView {
    }

    interface Presenter extends MvpPresenter<View> {
        void login(String username,String password);
        void signOut();
        String getLoginUserName();
    }
}