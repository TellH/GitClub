package tellh.com.gitclub.presentation.presenter;

import android.text.TextUtils;

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
import tellh.com.gitclub.presentation.view.adapter.BaseRecyclerAdapter;

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
    public void getUserInfo(final int position, final BaseRecyclerAdapter<UserEntity> adapter) {
        if (!mPresenter.checkNetwork())
            return;
        final UserEntity userEntity = adapter.getItems().get(position);
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
                                userEntity.bio = userInfo.getBio();
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
    public void followUser(final int position, final BaseRecyclerAdapter<UserEntity> adapter, boolean toggle) {
        if (!mPresenter.checkNetwork()|| !mPresenter.checkLogin())
            return;
        final UserEntity userEntity = adapter.getItems().get(position);
        final String user = userEntity.getLogin();
        //to follow
        if (toggle)
            mPresenter.addSubscription(
                    mUserDataSource.toFollow(user)
                            .subscribe(new DefaultSubscriber<Boolean>() {
                                           @Override
                                           public void onNext(Boolean result) {
                                               if (result) {
                                                   Note.show(Utils.getString(R.string.success_follow_repo) + user);
                                               } else {
                                                   Note.show(Utils.getString(R.string.error_follow_repo) + user);
                                               }
                                           }

                                           @Override
                                           protected void onError(String errorStr) {
                                               super.onError(errorStr);
                                               Note.show(Utils.getString(R.string.error_follow_repo) + user);
                                           }
                                       }
                            )
            );
        else
            mPresenter.addSubscription(
                    mUserDataSource.toUnFollow(user)
                            .subscribe(new DefaultSubscriber<Boolean>() {
                                           @Override
                                           public void onNext(Boolean result) {
                                               if (result) {
                                                   Note.show(Utils.getString(R.string.success_unfollow_user) + user);
                                               } else {
                                                   Note.show(Utils.getString(R.string.error_unfollow_user) + user);
                                               }
                                           }

                                           @Override
                                           protected void onError(String errorStr) {
                                               super.onError(errorStr);
                                               Note.show(Utils.getString(R.string.error_unfollow_user) + user);
                                           }
                                       }
                            )
            );

    }
}
