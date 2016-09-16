package tellh.com.gitclub.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import rx.Subscriber;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.config.Constant;
import tellh.com.gitclub.common.utils.LogUtils;
import tellh.com.gitclub.model.entity.Event;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.SearchResult;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;

import static tellh.com.gitclub.common.config.Constant.SortType.SortType_Repo.UPDATED;
import static tellh.com.gitclub.common.config.Constant.SortType.SortType_User.FOLLOWERS;

public class UserDataSourceTestActivity extends AppCompatActivity {
    UserDataSource dataSource;
    String userName;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        dataSource = AndroidApplication.getInstance().getAppComponent().userDataSource();
        userName = "TellH";
//        testLogin();
//        testListFollowers();
//        testListFollowing();
        testListStarredRepo();
//        testListWatchingRepo();
//        testListOwnRepo();
//        testListNews();
//        testSearch();
    }


    public void testListFollowers() {
        dataSource.listFollowers(userName, 1)
                .subscribe(new Subscriber<List<UserEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<UserEntity> userEntities) {
                        LogUtils.d(userEntities.get(0).getLogin());
                    }
                });
    }

    private void testLogin() {
        userName = "Nolan1995";
        password = "***";
//        dataSource.login(userName, password)
//                .subscribe(new Subscriber<UserInfo>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        LogUtils.e(e.getMessage());
//                    }
//
//                    @Override
//                    public void onError(UserInfo userInfo) {
//                        LogUtils.d(userInfo.getLogin());
//                        AccountPrefs.saveLoginUser(AndroidApplication.getInstance(), userInfo);
//                    }
//                });
    }

    public void testListFollowing() {
        dataSource.listFollowing(userName, 1)
                .subscribe(new Subscriber<List<UserEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<UserEntity> userEntities) {
                        LogUtils.d(userEntities.get(0).getLogin());
                    }
                });
    }


    public void testListStarredRepo() {
        dataSource.listStarredRepo(userName, UPDATED, 1)
                .subscribe(new Subscriber<List<RepositoryInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<RepositoryInfo> repositoryInfos) {
                        LogUtils.d(repositoryInfos.get(0).getFull_name());
                    }
                });
    }

    public void testListWatchingRepo() {
        dataSource.listWatchingRepo(userName, 1)
                .subscribe(new Subscriber<List<RepositoryInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<RepositoryInfo> repositoryInfos) {
                        LogUtils.d(repositoryInfos.get(0).getFull_name());
                    }
                });
    }

    public void testListOwnRepo() {
        dataSource.listOwnRepo(userName, 1)
                .subscribe(new Subscriber<List<RepositoryInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<RepositoryInfo> repositoryInfos) {
                        LogUtils.d(repositoryInfos.get(0).getFull_name());
                    }
                });
    }

    public void testListNews() {
        dataSource.listNews(userName, 1)
                .subscribe(new Subscriber<List<Event>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onNext(List<Event> events) {
                        LogUtils.d(events.get(0).getPayload().action);
                    }
                });
    }

    public void testSearch() {
        String keyWord = "";
        String language = Constant.Language.JAVA.val();
        dataSource.search(keyWord, language, FOLLOWERS, 1)
                .subscribe(new Subscriber<SearchResult<UserEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());
                    }

                    @Override
                    public void onNext(SearchResult<UserEntity> userEntitySearchResult) {
                        LogUtils.d(userEntitySearchResult.getItems().get(0).getLogin());
                    }
                });
    }
}
