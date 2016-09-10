package tellh.com.gitclub.model.net.DataSource;


import junit.framework.TestCase;

import tellh.com.gitclub.common.AndroidApplication;

/**
 * Created by tlh on 2016/8/27 :)
 */
public class UserDataSourceTest extends TestCase {

    UserDataSource dataSource;
    String userName;
    String password;

    public void setUp() throws Exception {
        dataSource = AndroidApplication.getInstance().getAppComponent().userDataSource();
    }

    public void testLogin() throws Exception {
        userName = "Nolan1995";
        password = "a123456";
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
//                    }
//                });
    }

    public void testListFollowers() throws Exception {
    }

    public void testListFollowing() throws Exception {

    }

    public void testListStarredRepo() throws Exception {

    }

    public void testListWatchingRepo() throws Exception {

    }

    public void testListOwnRepo() throws Exception {

    }

    public void testListNews() throws Exception {

    }

    public void testSearch() throws Exception {

    }
}