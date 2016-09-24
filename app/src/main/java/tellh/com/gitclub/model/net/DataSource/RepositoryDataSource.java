package tellh.com.gitclub.model.net.DataSource;

import android.text.TextUtils;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import tellh.com.gitclub.common.config.Constant.SortType.SortType_Repo;
import tellh.com.gitclub.common.utils.RxJavaUtils;
import tellh.com.gitclub.model.entity.ReadMe;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.SearchResult;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.model.net.service.RepositoryService;

import static tellh.com.gitclub.common.config.Constant.PER_PAGE;

/**
 * Created by tlh on 2016/8/27 :)
 */
public class RepositoryDataSource {
    private RepositoryService repositoryApi;

    public RepositoryDataSource(RepositoryService repositoryApi) {
        this.repositoryApi = repositoryApi;
    }

    public Observable<RepositoryInfo> getRepoInfo(String owner, String repo) {
        return repositoryApi.getRepoInfo(owner, repo)
                .compose(RxJavaUtils.<RepositoryInfo>applySchedulers());
    }

    public Observable<List<RepositoryInfo>> listForks(String owner, String repo, int page) {
        return repositoryApi.listForks(owner, repo, page)
                .compose(RxJavaUtils.<List<RepositoryInfo>>applySchedulers());
    }

    public Observable<List<UserEntity>> listWatchers(String owner, String repo, int page) {
        return repositoryApi.listWatchers(owner, repo, page)
                .compose(RxJavaUtils.<List<UserEntity>>applySchedulers());
    }

    public Observable<List<UserEntity>> listStargazers(String owner, String repo, int page) {
        return repositoryApi.listStargazers(owner, repo, page)
                .compose(RxJavaUtils.<List<UserEntity>>applySchedulers());
    }

    public Observable<List<UserEntity>> listContributors(String owner, String repo, int page) {
        return repositoryApi.listContributors(owner, repo, page)
                .compose(RxJavaUtils.<List<UserEntity>>applySchedulers());
    }

    public Observable<RepositoryInfo> toFork(String owner, String repo) {
        return repositoryApi.toFork(owner, repo)
                .compose(RxJavaUtils.<RepositoryInfo>applySchedulers());
    }

    public Observable<Boolean> toStar(String owner, String repo) {
        return repositoryApi.toStar(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> toWatch(String owner, String repo) {
        return repositoryApi.toWatch(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> unStar(String owner, String repo) {
        return repositoryApi.unStar(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> unWatch(String owner, String repo) {
        return repositoryApi.unWatch(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> checkStarred(String owner, String repo) {
        return repositoryApi.checkStarred(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> checkWatching(String owner, String repo) {
        return repositoryApi.checkWatching(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<Boolean> delete(String owner, String repo) {
        return repositoryApi.delete(owner, repo)
                .compose(RxJavaUtils.<Response<ResponseBody>>applySchedulers())
                .compose(RxJavaUtils.checkIfSuccessCode());
    }

    public Observable<SearchResult<RepositoryInfo>> search(String keyWord, String language, int page) {
        return search(keyWord, language, null, page)
                .compose(RxJavaUtils.<SearchResult<RepositoryInfo>>applySchedulers());
    }

    public Observable<SearchResult<RepositoryInfo>> search(String keyWord, String language, SortType_Repo sort, int page) {
        if (keyWord == null)
            keyWord = "";
//        if (location != null && !location.isEmpty()) {
//            q += "+location:" + location;
//        }
        if (!TextUtils.isEmpty(language))
            keyWord += language;
        if (TextUtils.isEmpty(sort.val()))
            return repositoryApi.search(keyWord, page, PER_PAGE)
                    .compose(RxJavaUtils.<SearchResult<RepositoryInfo>>applySchedulers());
        return repositoryApi.search(keyWord, sort.val(), page, PER_PAGE)
                .compose(RxJavaUtils.<SearchResult<RepositoryInfo>>applySchedulers());
    }

    public Observable<ReadMe> getReadMe(String owner, String repo) {
        return repositoryApi.getReadMe(owner, repo)
                .compose(RxJavaUtils.<ReadMe>applySchedulers());
    }
}
