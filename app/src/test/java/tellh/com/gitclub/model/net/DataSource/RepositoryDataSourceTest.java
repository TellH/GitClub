package tellh.com.gitclub.model.net.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import rx.Scheduler;
import rx.observers.TestSubscriber;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.schedulers.Schedulers;
import tellh.com.gitclub.BuildConfig;
import tellh.com.gitclub.di.component.ComponentHolder;
import tellh.com.gitclub.model.entity.Branch;
import tellh.com.gitclub.model.entity.ReadMe;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.recyclertreeview_lib.TreeNode;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by tlh on 2017/4/25 :)
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class RepositoryDataSourceTest {
    private RepositoryDataSource repositoryDataSource;

    @Before
    public void setUp() throws Exception {
        ComponentHolder.setContext(RuntimeEnvironment.application);
        repositoryDataSource = ComponentHolder.getAppComponent().repositoryDataSource();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }
        });
    }

    @Test
    public void getRepoInfo() throws Exception {
        TestSubscriber<RepositoryInfo> subscriber = TestSubscriber.create();
        repositoryDataSource.getRepoInfo("TellH", "GitClub").subscribe(subscriber);
        List<RepositoryInfo> onNextEvents = subscriber.getOnNextEvents();
        assertNotNull(onNextEvents.get(0));
        System.out.println(onNextEvents.get(0));
    }

    @Test
    public void listForks() throws Exception {
        TestSubscriber<List<RepositoryInfo>> subscriber = TestSubscriber.create();
        repositoryDataSource.listForks("TellH", "GitClub", 1).subscribe(subscriber);
        List<RepositoryInfo> list = subscriber.getOnNextEvents().get(0);
        assertNotNull(list);
        for (RepositoryInfo repo : list) {
            System.out.println(repo.getOwner());
        }
    }

    @Test
    public void listWatchers() throws Exception {
        TestSubscriber<List<UserEntity>> subscriber = TestSubscriber.create();
        repositoryDataSource.listWatchers("TellH", "GitClub", 1).subscribe(subscriber);
        List<UserEntity> userList = subscriber.getOnNextEvents().get(0);
        assertNotNull(userList);
        System.out.println(userList.toString());
    }

    @Test
    public void listStargazers() throws Exception {
        TestSubscriber<List<UserEntity>> subscriber = TestSubscriber.create();
        repositoryDataSource.listStargazers("TellH", "GitClub", 1).subscribe(subscriber);
        List<UserEntity> userList = subscriber.getOnNextEvents().get(0);
        assertNotNull(userList);
        System.out.println(userList.toString());
    }

    @Test
    public void listContributors() throws Exception {
        TestSubscriber<List<UserEntity>> subscriber = TestSubscriber.create();
        repositoryDataSource.listContributors("TellH", "GitClub", 1).subscribe(subscriber);
        List<UserEntity> userList = subscriber.getOnNextEvents().get(0);
        assertNotNull(userList);
        System.out.println(userList.toString());
    }

    @Test
    public void toFork() throws Exception {
        TestSubscriber<RepositoryInfo> subscriber = TestSubscriber.create();
        repositoryDataSource.toFork("TellH", "GitClub").subscribe(subscriber);
        RepositoryInfo repo = subscriber.getOnNextEvents().get(0);
        assertNotNull(repo);
        System.out.println(repo.toString());
    }

    @Test
    public void toStar() throws Exception {
        TestSubscriber<Boolean> subscriber = TestSubscriber.create();
        repositoryDataSource.toStar("TellH", "GitClub").subscribe(subscriber);
        subscriber.assertValue(true);
        subscriber = TestSubscriber.create();
        repositoryDataSource.checkStarred("TellH", "GitClub").subscribe(subscriber);
        subscriber.assertValue(true);
    }

    @Test
    public void toWatch() throws Exception {
        TestSubscriber<Boolean> subscriber = TestSubscriber.create();
        repositoryDataSource.toWatch("TellH", "GitClub").subscribe(subscriber);
        subscriber.assertValue(true);
        subscriber = TestSubscriber.create();
        repositoryDataSource.checkWatching("TellH", "GitClub").subscribe(subscriber);
        subscriber.assertValue(true);
    }

    @Test
    public void unStar() throws Exception {
        TestSubscriber<Boolean> subscriber = TestSubscriber.create();
        repositoryDataSource.unStar("TellH", "GitClub").subscribe(subscriber);
        subscriber.assertValue(true);
        subscriber = TestSubscriber.create();
        repositoryDataSource.checkStarred("TellH", "GitClub").subscribe(subscriber);
        subscriber.assertValue(false);
    }

    @Test
    public void unWatch() throws Exception {
        TestSubscriber<Boolean> subscriber = TestSubscriber.create();
        repositoryDataSource.unWatch("TellH", "GitClub").subscribe(subscriber);
        subscriber.assertValue(true);
        subscriber = TestSubscriber.create();
        repositoryDataSource.checkWatching("TellH", "GitClub").subscribe(subscriber);
        subscriber.assertValue(false);
    }

    @Test
    public void listBranches() throws Exception {
        TestSubscriber<List<Branch>> subscriber = TestSubscriber.create();
        repositoryDataSource.listBranches("TellH", "GitClub").subscribe(subscriber);
        List<Branch> branches = subscriber.getOnNextEvents().get(0);
        assertNotNull(branches);
        System.out.println(branches.toString());
    }

    @Test
    public void getContent() throws Exception {
        TestSubscriber<List<Branch>> branchSubscriber = TestSubscriber.create();
        repositoryDataSource.listBranches("TellH", "GitClub").subscribe(branchSubscriber);
        List<Branch> branches = branchSubscriber.getOnNextEvents().get(0);
        assertNotNull(branches);
        assertTrue(branches.size() > 0);
        TestSubscriber<List<TreeNode>> treeNodeSubscriber = TestSubscriber.create();
        repositoryDataSource.getContent("TellH", "GitClub", branches.get(0)).subscribe(treeNodeSubscriber);
        List<TreeNode> treeNodes = treeNodeSubscriber.getOnNextEvents().get(0);
        assertNotNull(treeNodes);
    }

    @Test
    public void search() throws Exception {

    }

    @Test
    public void search1() throws Exception {

    }

    @Test
    public void getReadMe() throws Exception {
        TestSubscriber<ReadMe> subscriber = TestSubscriber.create();
        repositoryDataSource.getReadMe("TellH", "GitClub").subscribe(subscriber);
        ReadMe readMe = subscriber.getOnNextEvents().get(0);
        assertNotNull(readMe);
    }

}