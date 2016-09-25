package tellh.com.gitclub.presentation.contract;

import tellh.com.gitclub.common.base.BaseView;
import tellh.com.gitclub.common.base.MvpPresenter;
import tellh.com.gitclub.model.entity.RepositoryInfo;

public interface RepoPageContract {

    interface View extends BaseView {
        void onGetRepositoryInfo(RepositoryInfo repositoryInfo);

        void onCheckStarred(Boolean result);

        void onCheckWatch(Boolean result);

        void onGetReadMe(String html_url);
    }

    interface Presenter extends MvpPresenter<View> {

        void getRepoInfo(String owner, String repo);

        void toFork(String owner, String repo);

        void toStar(String owner, String repo, boolean checked);

        void toWatch(String owner, String repo, boolean checked);

        void checkStarred(String owner, String repo);

        void checkWatch(String owner, String repo);

        void getReadMe(String owner, String repo);
    }
}