package tellh.com.gitclub.model.net.DataSource;

import android.content.Context;
import android.text.TextUtils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.Constant;
import tellh.com.gitclub.common.config.Constant.SortType.SortType_Repo;
import tellh.com.gitclub.common.config.Constant.SortType.SortType_User;
import tellh.com.gitclub.common.config.GithubConfig;
import tellh.com.gitclub.common.utils.RxJavaUtils;
import tellh.com.gitclub.model.entity.Authorization;
import tellh.com.gitclub.model.entity.CreateAuthorization;
import tellh.com.gitclub.model.entity.Event;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.SearchResult;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.model.entity.UserInfo;
import tellh.com.gitclub.model.net.client.GithubAuthRetrofit;
import tellh.com.gitclub.model.net.service.UserService;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;

/**
 * Created by tlh on 2016/8/27 :)
 */
public class UserDataSource {
    private final GithubAuthRetrofit authRetrofit;
    private UserService userApi;

    private Context mCtx;

    public UserDataSource(GithubAuthRetrofit authRetrofit, UserService userApi, Context ctx) {
        this.authRetrofit = authRetrofit;
        this.userApi = userApi;
        mCtx = ctx;
    }

    public Observable<UserInfo> getUserInfo(String user) {
        return userApi.getUserInfo(user)
                .compose(RxJavaUtils.<UserInfo>applySchedulers());
    }

    public Observable<Boolean> login(final String userName, String password) {
        authRetrofit.setUserInfo(userName, password);
        final UserService api = authRetrofit.build().create(UserService.class);
        CreateAuthorization createAuthorization = new CreateAuthorization();
        createAuthorization.note = GithubConfig.NOTE;
        createAuthorization.client_id = GithubConfig.CLIENT_ID;
        createAuthorization.client_secret = GithubConfig.CLIENT_SECRET;
        createAuthorization.scopes = GithubConfig.SCOPES;

        return api.createAuthorization(createAuthorization)
                .flatMap(new Func1<Authorization, Observable<UserInfo>>() {
                    @Override
                    public Observable<UserInfo> call(Authorization authorization) {
                        String token = authorization.getToken();
                        // save token
                        AccountPrefs.saveLoginToken(mCtx, token);
                        return api.getUserInfo(userName);
                    }
                }).compose(RxJavaUtils.<UserInfo>applySchedulers())
                .map(new Func1<UserInfo, Boolean>() {
                    @Override
                    public Boolean call(UserInfo userInfo) {
                        if (userInfo != null) {
                            AccountPrefs.saveLoginUser(mCtx, userInfo);
                            return true;
                        }
                        return false;
                    }
                });
    }

    public Observable<List<UserEntity>> listFollowers(String userName, int page) {
        return userApi.listFollowers(userName, page)
                .compose(RxJavaUtils.<List<UserEntity>>applySchedulers());
    }

    public Observable<List<UserEntity>> listFollowing(String user, int page) {
        return userApi.listFollowing(user, page)
                .compose(RxJavaUtils.<List<UserEntity>>applySchedulers());
    }

    public Observable<List<RepositoryInfo>> listStarredRepo(String user, int page) {
        return listStarredRepo(user, SortType_Repo.CREATED, page);
    }

    public Observable<List<RepositoryInfo>> listStarredRepo(String user, SortType_Repo sort, int page) {
        return userApi.listStarredRepo(user, sort.val(), page, Constant.PER_PAGE)
                .compose(RxJavaUtils.<List<RepositoryInfo>>applySchedulers());
    }

    public Observable<List<RepositoryInfo>> listWatchingRepo(String user, int page) {
        return userApi.listWatchingRepo(user, page)
                .compose(RxJavaUtils.<List<RepositoryInfo>>applySchedulers());
    }

    public Observable<List<RepositoryInfo>> listOwnRepo(String user, int page) {
        return userApi.listOwnRepo(user, page)
                .compose(RxJavaUtils.<List<RepositoryInfo>>applySchedulers());
    }

    public Observable<List<Event>> listNews(String user, int page) {
        return userApi.listNews(user, page, Constant.PER_PAGE_NEWS)
                .compose(RxJavaUtils.<List<Event>>applySchedulers());
    }

    public Observable<SearchResult<UserEntity>> search(String keyWord, String language, SortType_User sort, int page) {
        if (keyWord == null)
            keyWord = "";
//        if (location != null && !location.isEmpty()) {
//            q += "+location:" + location;
//        }
        if (!TextUtils.isEmpty(language))
            keyWord += language;
        return userApi.search(keyWord, sort.val(), page, Constant.PER_PAGE)
                .compose(RxJavaUtils.<SearchResult<UserEntity>>applySchedulers());
    }

    public Observable<Boolean> signOut() {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    AccountPrefs.removeLoginUser(mCtx);
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new Throwable(mCtx.getString(R.string.error_sign_out)));
                }
            }
        });
    }

    public String getLoginUserName() {
        return AccountPrefs.getLoginUserName(mCtx);
    }

    public Observable<Boolean> toFollow(String user) {
        return userApi.toFollow(user)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> toUnFollow(String user) {
        return userApi.toUnFollow(user)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> checkIfFollowing(String user) {
        return userApi.checkIfFollowing(user)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }
}
