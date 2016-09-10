package tellh.com.gitclub.presentation.presenter;

import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.presentation.view.adapter.BaseRecyclerAdapter;

/**
 * Created by tlh on 2016/9/1 :)
 */
public interface IRepoListPresenter {
    interface OnGetRepoCallback {
        void onGet(RepositoryInfo repositoryInfo);
    }

    void checkState(int position, BaseRecyclerAdapter<RepositoryInfo> adapter);

    void starRepo(int position, BaseRecyclerAdapter<RepositoryInfo> adapter, boolean toggle);

    void watchRepo(int position, BaseRecyclerAdapter<RepositoryInfo> adapter, boolean toggle);

    void forkRepo(int position, BaseRecyclerAdapter<RepositoryInfo> adapter);

    void getRepoInfo(String owner, String name, OnGetRepoCallback callback);
}
