package tellh.com.gitclub.presentation.presenter;

import android.text.TextUtils;

import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;

import rx.Observable;
import rx.functions.Func2;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.BasePresenter;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.Note;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.model.entity.UserInfo;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;

/**
 * Created by tlh on 2016/9/1 :)
 */
public class UserListPresenter implements IUserListPresenter {
    protected final UserDataSource mUserDataSource;
    private final BasePresenter mPresenter;

    public UserListPresenter(BasePresenter presenter, UserDataSource userDataSource) {
        this.mUserDataSource = userDataSource;
        this.mPresenter = presenter;
    }

    @Override
    public void getUserInfo(final int position, final RecyclerViewAdapter adapter) {
        if (!mPresenter.checkNetwork())
            return;
        final UserEntity userEntity = (UserEntity) adapter.getDisplayList().get(position);
        String user = userEntity.getLogin();
        mPresenter.addSubscription(
                Observable.zip(
                        mUserDataSource.checkIfFollowing(user),
                        mUserDataSource.getUserInfo(user),
                        new Func2<Boolean, UserInfo, Void>() {
                            @Override
                            public Void call(Boolean isFollowing, UserInfo userInfo) {
                                boolean change = !TextUtils.isEmpty(userInfo.getBio()) || isFollowing != userEntity.isFollowing;
                                userEntity.isFollowing = isFollowing;
                                userEntity.setBio(userInfo.getBio());
                                userEntity.hasCheck = true;
                                if (change)
                                    adapter.notifyItemChanged(position);
                                return null;
                            }
                        }
                ).subscribe(new DefaultSubscriber<Void>() {
                    @Override
                    public void onNext(Void aVoid) {
                    }
                })
        );

    }

    @Override
    public void followUser(final int position, final RecyclerViewAdapter adapter, boolean toggle) {
        if (!mPresenter.checkNetwork() || !mPresenter.checkLogin())
            return;
        final UserEntity userEntity = (UserEntity) adapter.getDisplayList().get(position);
        final String user = userEntity.getLogin();
        //to follow
        Observable<Boolean> observable;
        final int hint_success;
        final int hint_error;
        if (toggle) {
            observable = mUserDataSource.toFollow(user);
            hint_success = R.string.success_follow_user;
            hint_error = R.string.error_follow_user;
        } else {
            observable = mUserDataSource.toUnFollow(user);
            hint_success = R.string.success_unfollow_user;
            hint_error = R.string.error_unfollow_user;
        }
        mPresenter.addSubscription(observable
                .subscribe(new DefaultSubscriber<Boolean>() {
                               @Override
                               public void onNext(Boolean result) {
                                   if (result) {
                                       Note.show(Utils.getString(hint_success) + user);
                                   } else {
                                       Note.show(Utils.getString(hint_error) + user);
                                   }
                               }

                               @Override
                               protected void onError(String errorStr) {
                                   super.onError(errorStr);
                                   Note.show(Utils.getString(hint_error) + user);
                               }
                           }
                ));
    }
}
