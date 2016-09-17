package tellh.com.gitclub.presentation.contract;

import tellh.com.gitclub.common.base.BaseView;
import tellh.com.gitclub.common.base.MvpPresenter;
import tellh.com.gitclub.model.entity.UserInfo;

public interface PersonalPageContract {

    interface View extends BaseView {
        void onGetUserInfo(UserInfo userInfo);

        void onCheckFollowing(Boolean isFollowing);
    }

    interface Presenter extends MvpPresenter<View> {

        void checkIfFollowing(String user);

        void getUserInfo(String user);

        void toFollow(String user, boolean toggle);
    }
}