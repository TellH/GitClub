package tellh.com.gitclub.presentation.presenter;

import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;

import tellh.com.gitclub.model.entity.RepositoryInfo;

/**
 * Created by tlh on 2016/9/1 :)
 */
public interface IRepoListPresenter {
    interface OnGetRepoCallback {
        void onGet(RepositoryInfo repositoryInfo);
    }

    void checkState(int position, RecyclerViewAdapter adapter);

    void starRepo(int position, RecyclerViewAdapter adapter, boolean toggle);

    void watchRepo(int position, RecyclerViewAdapter adapter, boolean toggle);

    void forkRepo(int position, RecyclerViewAdapter adapter);

    void getRepoInfo(String owner, String name, OnGetRepoCallback callback);
}
