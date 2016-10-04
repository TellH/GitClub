package tellh.com.gitclub.presentation.presenter;

import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;

/**
 * Created by tlh on 2016/9/1 :)
 */
public interface IUserListPresenter {
    void getUserInfo(int position, RecyclerViewAdapter adapter);

    void followUser(int position, RecyclerViewAdapter adapter, boolean toggle);

}
