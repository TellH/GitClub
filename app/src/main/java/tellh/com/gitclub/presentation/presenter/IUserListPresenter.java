package tellh.com.gitclub.presentation.presenter;

import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.presentation.view.adapter.BaseRecyclerAdapter;

/**
 * Created by tlh on 2016/9/1 :)
 */
public interface IUserListPresenter {
    void getUserInfo(int position, BaseRecyclerAdapter<UserEntity> adapter);

    void followUser(int position, BaseRecyclerAdapter<UserEntity> adapter, boolean toggle);

}
