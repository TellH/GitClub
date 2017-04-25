package tellh.com.gitclub.model.net.DataSource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
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
import tellh.com.gitclub.common.config.Constant;
import tellh.com.gitclub.di.component.ComponentHolder;
import tellh.com.gitclub.model.entity.Event;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.SearchResult;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.model.entity.UserInfo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by tlh on 2017/4/24 :)
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UserDataSourceTest {
    private UserDataSource userDataSource;
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        ComponentHolder.setContext(RuntimeEnvironment.application);
        userDataSource = ComponentHolder.getAppComponent().userDataSource();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }
        });
//        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {
//            @Override
//            public Scheduler getMainThreadScheduler() {
//                return Schedulers.immediate();
//            }
//        });
    }

    @Test
    public void getUserInfo() throws Exception {
        TestSubscriber<UserInfo> subscriber = TestSubscriber.create();
        userDataSource.getUserInfo("TellH").subscribe(subscriber);
        subscriber.assertValueCount(1);
        List<UserInfo> event = subscriber.getOnNextEvents();
        assertNotNull(event);
        assertTrue(event.size() == 1);
        for (UserInfo userInfo : event) {
            System.out.println(userInfo.toString());
        }
    }

    @Test
    public void login() throws Exception {
        TestSubscriber<Boolean> subscriber = TestSubscriber.create();
        // Success to login
        userDataSource.login("Nolan1995", "a123456").subscribe(subscriber);
        subscriber.assertValue(true);
        subscriber = TestSubscriber.create();
        // Fail to login, may be bug in TestSubscriber
        userDataSource.login("Nolan1995", "").subscribe(subscriber);
        subscriber.assertNoValues();
    }

    @Test
    public void listFollowers() throws Exception {
        TestSubscriber<List<UserEntity>> subscriber = TestSubscriber.create();
        userDataSource.listFollowers("TellH", 1).subscribe(subscriber);
        List<List<UserEntity>> onNextEvents = subscriber.getOnNextEvents();
        assertNotNull(onNextEvents);
        System.out.println(onNextEvents.toString());
    }

    @Test
    public void listFollowing() throws Exception {
        TestSubscriber<List<UserEntity>> subscriber = TestSubscriber.create();
        userDataSource.listFollowing("TellH", 1).subscribe(subscriber);
        List<List<UserEntity>> onNextEvents = subscriber.getOnNextEvents();
        assertNotNull(onNextEvents);
        System.out.println(onNextEvents.toString());
    }

    @Test
    public void listStarredRepo() throws Exception {
        TestSubscriber<List<RepositoryInfo>> subscriber = TestSubscriber.create();
        userDataSource.listStarredRepo("TellH", 1).subscribe(subscriber);
        List<List<RepositoryInfo>> onNextEvents = subscriber.getOnNextEvents();
        assertNotNull(onNextEvents);
        System.out.println(onNextEvents.toString());
    }

    @Test
    public void listStarredRepo1() throws Exception {
        TestSubscriber<List<RepositoryInfo>> subscriber = TestSubscriber.create();
        userDataSource.listStarredRepo("TellH", Constant.SortType.SortType_Repo.STARS, 1).subscribe(subscriber);
        List<List<RepositoryInfo>> onNextEvents = subscriber.getOnNextEvents();
        assertNotNull(onNextEvents);
        System.out.println(onNextEvents.toString());
        List<RepositoryInfo> list = onNextEvents.get(0);
        for (int i = 1; i < list.size(); i++) {
            assertTrue(list.get(i).getStars() < list.get(i - 1).getStars());
        }
    }

    @Test
    public void listWatchingRepo() throws Exception {
        TestSubscriber<List<RepositoryInfo>> subscriber = TestSubscriber.create();
        userDataSource.listWatchingRepo("TellH", 1).subscribe(subscriber);
        List<List<RepositoryInfo>> onNextEvents = subscriber.getOnNextEvents();
        assertNotNull(onNextEvents);
        System.out.println(onNextEvents.get(0).toString());
    }

    @Test
    public void listOwnRepo() throws Exception {
        TestSubscriber<List<RepositoryInfo>> subscriber = TestSubscriber.create();
        userDataSource.listOwnRepo("TellH", 1).subscribe(subscriber);
        List<List<RepositoryInfo>> onNextEvents = subscriber.getOnNextEvents();
        assertNotNull(onNextEvents);
        System.out.println(onNextEvents.get(0).toString());
    }

    @Test
    public void listNews() throws Exception {
        TestSubscriber<List<Event>> subscriber = TestSubscriber.create();
        userDataSource.listNews("TellH", 1).subscribe(subscriber);
        List<List<Event>> onNextEvents = subscriber.getOnNextEvents();
        assertNotNull(onNextEvents);
        assertTrue(onNextEvents.size() == 1);
        System.out.println(onNextEvents.get(0).toString());
    }

    @Test
    public void search() throws Exception {
        TestSubscriber<SearchResult<UserEntity>> subscriber = TestSubscriber.create();
        userDataSource.search("", Constant.Language.ALL.val(), Constant.SortType.SortType_User.BEST_MATCH, 1)
                .subscribe(subscriber);
        List<SearchResult<UserEntity>> onNextEvents = subscriber.getOnNextEvents();
        assertNotNull(onNextEvents);
        System.out.println(onNextEvents.get(0).toString());
    }

    @Test
    public void toFollow() throws Exception {
        TestSubscriber<Boolean> subscriber = TestSubscriber.create();
        userDataSource.toFollow("TellH").subscribe(subscriber);
        subscriber.assertValue(true);
    }

    @Test
    public void toUnFollow() throws Exception {
        TestSubscriber<Boolean> subscriber = TestSubscriber.create();
        userDataSource.toUnFollow("TellH").subscribe(subscriber);
        subscriber.assertValue(true);
    }

    @Test
    public void checkIfFollowing() throws Exception {
        TestSubscriber<Boolean> subscriber = TestSubscriber.create();
        userDataSource.checkIfFollowing("TellH").subscribe(subscriber);
        subscriber.assertValue(true);
    }

}