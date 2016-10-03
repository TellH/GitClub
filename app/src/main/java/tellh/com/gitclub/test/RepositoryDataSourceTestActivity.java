package tellh.com.gitclub.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.config.Constant;
import tellh.com.gitclub.common.utils.LogUtils;
import tellh.com.gitclub.model.entity.Branch;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.SearchResult;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.recyclertreeview_lib.TreeNode;

import static tellh.com.gitclub.common.config.Constant.SortType.SortType_Repo.STARS;

/**
 * Created by tlh on 2016/8/27 :)
 */
public class RepositoryDataSourceTestActivity extends AppCompatActivity {
    RepositoryDataSource dataSource;
    String owner = "tellh";
    final String repo = "gitclub";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        dataSource = AndroidApplication.getInstance().getAppComponent().repositoryDataSource();

//        testGetRepoInfo();
//        testListForkers();
//        testListWatchers();
//        testListStargazers();
//        testListContributors();
//        testToFork();
//        testToStar();
//        testToWatch();
//        testCheckWatching();
//        testCheckStarred();

//        testUnStar();
//        testUnWatch();

//        testSearch();
//        testSearch1();
        testGetContent();
    }

    public void testGetRepoInfo() {
        dataSource.getRepoInfo(owner, repo)
                .subscribe(new DefaultSubscriber<RepositoryInfo>() {
                    @Override
                    public void onNext(RepositoryInfo repositoryInfo) {
                        LogUtils.d(String.valueOf(repositoryInfo.getStars()));
                    }
                });
    }

    public void testListForkers() {
        dataSource.listForks(owner, repo, 1)
                .subscribe(new DefaultSubscriber<List<RepositoryInfo>>() {
                    @Override
                    public void onNext(List<RepositoryInfo> repositoryInfos) {
                        LogUtils.d(repositoryInfos.get(0).getCreated_at());
                    }
                });
    }

    public void testListWatchers() {
        dataSource.listWatchers(owner, repo, 1)
                .subscribe(new DefaultSubscriber<List<UserEntity>>() {
                    @Override
                    public void onNext(List<UserEntity> userEntities) {
                        LogUtils.d(userEntities.get(0).getLogin());
                    }
                });
    }

    public void testListStargazers() {
        dataSource.listStargazers(owner, repo, 1)
                .subscribe(new DefaultSubscriber<List<UserEntity>>() {
                    @Override
                    public void onNext(List<UserEntity> userEntities) {
                        LogUtils.d(userEntities.get(0).getLogin());
                    }
                });
    }

    public void testListContributors() {
        dataSource.listContributors(owner, repo, 1)
                .subscribe(new DefaultSubscriber<List<UserEntity>>() {
                    @Override
                    public void onNext(List<UserEntity> userEntities) {
                        LogUtils.d(userEntities.get(0).getLogin());
                    }
                });
    }

    public void testToFork() {
        dataSource.toFork(owner, repo)
                .subscribe(new DefaultSubscriber<RepositoryInfo>() {
                    @Override
                    public void onNext(RepositoryInfo repositoryInfo) {
                        LogUtils.d(repositoryInfo.getFull_name());
                    }
                });
    }

    public void testToStar() {
        dataSource.toStar(owner, repo)
                .subscribe(new DefaultSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtils.d(String.valueOf(aBoolean));
                    }
                });
    }

    public void testToWatch() {
        dataSource.toWatch(owner, repo)
                .subscribe(new DefaultSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtils.d(String.valueOf(aBoolean));
                    }
                });
    }

    public void testUnStar() {
        dataSource.unStar(owner, repo)
                .subscribe(new DefaultSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtils.d(String.valueOf(aBoolean));
                    }
                });
    }

    public void testUnWatch() {
        dataSource.unWatch(owner, repo)
                .subscribe(new DefaultSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtils.d(String.valueOf(aBoolean));
                    }
                });
    }

    public void testCheckStarred() {
        dataSource.checkStarred(owner, repo)
                .subscribe(new DefaultSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtils.d("Starred: " + String.valueOf(aBoolean));
                    }
                });
    }

    public void testCheckWatching() {
        dataSource.checkWatching(owner, repo)
                .subscribe(new DefaultSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        LogUtils.d("Watching: " + String.valueOf(aBoolean));
                    }
                });
    }

    //don't test.
    public void testDelete() {

    }

    public void testSearch() {
        String keyWord = "AutoGo";
//        dataSource.search(keyWord, 1)
//                .subscribe(new DefaultSubscriber<SearchResult<RepositoryInfo>>() {
//                    @Override
//                    public void onNext(SearchResult<RepositoryInfo> repositoryInfoSearchResult) {
//                        LogUtils.d(repositoryInfoSearchResult.getItems().get(0).getFull_name());
//                    }
//                });
    }

    public void testSearch1() {
        String keyWord = "";
        dataSource.search(keyWord, Constant.Language.WEB.val(), STARS, 1)
                .subscribe(new DefaultSubscriber<SearchResult<RepositoryInfo>>() {
                    @Override
                    public void onNext(SearchResult<RepositoryInfo> repositoryInfoSearchResult) {
                        LogUtils.d(repositoryInfoSearchResult.getItems().get(0).getFull_name());
                    }
                });
    }

    public void testGetContent() {
        Branch branch = new Branch();
        branch.setName("dev");
        Branch.CommitEntity commitEntity = new Branch.CommitEntity();
        commitEntity.setSha("17afdf50c370095f259802bd8e812164da5a6f24");
        branch.setCommit(commitEntity);
        dataSource.getContent(owner, repo, branch)
                .subscribe(new DefaultSubscriber<List<TreeNode>>() {
                    @Override
                    public void onNext(List<TreeNode> treeNodes) {
                        LogUtils.d(treeNodes.toString());
                    }
                });
    }

}
