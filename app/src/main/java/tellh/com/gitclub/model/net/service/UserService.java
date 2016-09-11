package tellh.com.gitclub.model.net.service;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import tellh.com.gitclub.model.entity.Authorization;
import tellh.com.gitclub.model.entity.CreateAuthorization;
import tellh.com.gitclub.model.entity.Event;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.SearchResult;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.model.entity.UserInfo;

/**
 * Created by tlh on 2016/8/26 :)
 */
public interface UserService {
    @GET("users/{user}")
    Observable<UserInfo> getUserInfo(@Path("user") String user);

    @GET("users/{user}/followers")
    Observable<List<UserEntity>> listFollowers(@Path("user") String user);

    @GET("users/{user}/followers")
    Observable<List<UserEntity>> listFollowers(@Path("user") String user, @Query("page") int page);

    @GET("users/{user}/followers")
    Observable<List<UserEntity>> listFollowers(@Path("user") String user, @Query("page") int page, @Query("per_page") int pageSize);

    @GET("users/{user}/following")
    Observable<List<UserEntity>> listFollowing(@Path("user") String user);

    @GET("users/{user}/following")
    Observable<List<UserEntity>> listFollowing(@Path("user") String user, @Query("page") int page);

    @GET("users/{user}/following")
    Observable<List<UserEntity>> listFollowing(@Path("user") String user, @Query("page") int page, @Query("per_page") int pageSize);

    @GET("user/following/{username}")
    Observable<Response<ResponseBody>> checkIfFollowing(@Path("username") String user);

    @PUT("user/following/{username}")
    Observable<Response<ResponseBody>> toFollow(@Path("username") String user);

    @DELETE("user/following/{username}")
    Observable<Response<ResponseBody>> toUnFollow(@Path("username") String user);

    @GET("users/{user}/starred")
    Observable<List<RepositoryInfo>> listStarredRepo(@Path("user") String user);

    @GET("users/{user}/starred")
    Observable<List<RepositoryInfo>> listStarredRepo(@Path("user") String user, @Query("sort") String sort,
                                                     @Query("page") int page, @Query("per_page") int pageSize);

    @GET("users/{user}/subscriptions")
        //https://developer.github.com/v3/activity/watching
    Observable<List<RepositoryInfo>> listWatchingRepo(@Path("user") String user);

    @GET("users/{user}/subscriptions")
    Observable<List<RepositoryInfo>> listWatchingRepo(@Path("user") String user, @Query("page") int page);

    @GET("users/{user}/subscriptions")
    Observable<List<RepositoryInfo>> listWatchingRepo(@Path("user") String user, @Query("page") int page, @Query("per_page") int pageSize);

    @GET("users/{user}/repos")
    Observable<List<RepositoryInfo>> listOwnRepo(@Path("user") String user);

    @GET("users/{user}/repos")
    Observable<List<RepositoryInfo>> listOwnRepo(@Path("user") String user, @Query("page") int page);

    @GET("users/{user}/received_events")
    Observable<List<Event>> listNews(@Path("user") String user);

    @GET("users/{user}/received_events")
    Observable<List<Event>> listNews(@Path("user") String user, @Query("page") int page);

    @GET("users/{user}/received_events")
    Observable<List<Event>> listNews(@Path("user") String user, @Query("page") int page, @Query("per_page") int pageSize);

    @GET("search/users")
    Observable<SearchResult<UserEntity>> search(@Query(value = "q", encoded = true) String keyWord, @Query("per_page") int pageSize);

    @GET("search/users")
    Observable<SearchResult<UserEntity>> search(@Query(value = "q", encoded = true) String keyWord,
                                                @Query("sort") String sort, @Query("page") int page, @Query("per_page") int pageSize);

    @POST("/authorizations")
    Observable<Authorization> createAuthorization(@Body CreateAuthorization createAuthorization);
}
