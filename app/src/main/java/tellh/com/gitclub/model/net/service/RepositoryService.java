package tellh.com.gitclub.model.net.service;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import tellh.com.gitclub.model.entity.Branch;
import tellh.com.gitclub.model.entity.GitTree;
import tellh.com.gitclub.model.entity.ReadMe;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.SearchResult;
import tellh.com.gitclub.model.entity.UserEntity;

/**
 * Created by tlh on 2016/8/26 :)
 */
public interface RepositoryService {
    @GET("repos/{owner}/{repo}")
    Observable<RepositoryInfo> getRepoInfo(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/forks")
    Observable<List<RepositoryInfo>> listForks(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/forks")
    Observable<List<RepositoryInfo>> listForks(@Path("owner") String owner, @Path("repo") String repo, @Query("page") int page);

    @GET("repos/{owner}/{repo}/subscribers")
    Observable<List<UserEntity>> listWatchers(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/subscribers")
    Observable<List<UserEntity>> listWatchers(@Path("owner") String owner, @Path("repo") String repo, @Query("page") int page);

    @GET("repos/{owner}/{repo}/stargazers")
    Observable<List<UserEntity>> listStargazers(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/stargazers")
    Observable<List<UserEntity>> listStargazers(@Path("owner") String owner, @Path("repo") String repo, @Query("page") int page);

    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<UserEntity>> listContributors(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/contributors")
    Observable<List<UserEntity>> listContributors(@Path("owner") String owner, @Path("repo") String repo, @Query("page") int page);

    @POST("repos/{owner}/{repo}/forks")
    Observable<RepositoryInfo> toFork(@Path("owner") String owner, @Path("repo") String repo);

    @Headers("Content-Length: 0")
    @PUT("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> toStar(@Path("owner") String owner, @Path("repo") String repo);

    @Headers("Content-Length: 0")
    @PUT("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> toWatch(@Path("owner") String owner, @Path("repo") String repo);

    @DELETE("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> unStar(@Path("owner") String owner, @Path("repo") String repo);

    @DELETE("user/subscriptions/{owner}/{repo}")
    Observable<Response<ResponseBody>> unWatch(@Path("owner") String owner, @Path("repo") String repo);

    @GET("user/starred/{owner}/{repo}")
    Observable<Response<ResponseBody>> checkStarred(@Path("owner") String owner, @Path("repo") String repo);

    @GET("user/subscriptions/{owner}/{repo}")
        //https://developer.github.com/v3/activity/watching/#check-if-you-are-watching-a-repository-legacy
    Observable<Response<ResponseBody>> checkWatching(@Path("owner") String owner, @Path("repo") String repo);

    @DELETE("repos/{owner}/{repo}")
        //I dont want to realize this operation because repository is very treasure for a user.
    Observable<Response<ResponseBody>> delete(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/readme")
    Observable<ReadMe> getReadMe(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/branches")
    Observable<List<Branch>> listBranches(@Path("owner") String owner, @Path("repo") String repo);

    @GET("repos/{owner}/{repo}/git/trees/{sha}?recursive=1")
    Observable<GitTree> getContent(@Path("owner") String owner, @Path("repo") String repo, @Path("sha") String sha);

    @GET("search/repositories")
        // set encoded = true to prevent from encoding the "+" in keyWord.
    Observable<SearchResult<RepositoryInfo>> search(@Query(value = "q", encoded = true) String keyWord, @Query("page") int page, @Query("per_page") int pageSize);

    @GET("search/repositories")
    Observable<SearchResult<RepositoryInfo>> search(@Query(value = "q", encoded = true) String keyWord, @Query("sort") String sort, @Query("page") int page, @Query("per_page") int pageSize);

}
