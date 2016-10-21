package tellh.com.gitclub.model.net.DataSource;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Func1;
import tellh.com.gitclub.common.config.Constant;
import tellh.com.gitclub.common.utils.RxJavaUtils;
import tellh.com.gitclub.common.utils.StringUtils;
import tellh.com.gitclub.model.entity.GankResponse;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.net.service.GankService;
import tellh.com.gitclub.model.net.service.RepositoryService;

/**
 * Created by tlh on 2016/10/5 :)
 */

public class GankDataSource {
    private GankService gankService;
    private RepositoryService repositoryService;

    public GankDataSource(GankService gankService, RepositoryService repositoryService) {
        this.gankService = gankService;
        this.repositoryService = repositoryService;
    }

    private Observable<List<GankResponse.ResultsEntity>> getData(int page) {
        return gankService.getData(Constant.PER_PAGE_GANK, page)
                .compose(RxJavaUtils.<GankResponse>applySchedulers())
                .map(new Func1<GankResponse, List<GankResponse.ResultsEntity>>() {
                    @Override
                    public List<GankResponse.ResultsEntity> call(GankResponse gankResponse) {
                        return gankResponse.getResults();
                    }
                });
    }

    private Observable<RepositoryInfo> getRepository(int page) {
        return getData(page)
                .flatMap(new Func1<List<GankResponse.ResultsEntity>, Observable<RepositoryInfo>>() {
                    @Override
                    public Observable<RepositoryInfo> call(final List<GankResponse.ResultsEntity> resultsEntities) {
                        List<Observable<RepositoryInfo>> observableList = new ArrayList<>(20);
                        for (GankResponse.ResultsEntity resultsEntity : resultsEntities) {
                            String url = resultsEntity.getUrl();
                            if (TextUtils.isEmpty(url) || !url.startsWith("https://github.com/"))
                                continue;
                            String[] result = StringUtils.parseGithubUrl(url);
                            if (result == null || result.length != 2)
                                continue;
                            observableList.add(repositoryService.getRepoInfo(result[0], result[1]));
                        }
                        return Observable.mergeDelayError(observableList).compose(RxJavaUtils.<RepositoryInfo>applySchedulers());
                    }
                });
    }

    public Observable<List<RepositoryInfo>> getRepositories(final int page) {
        final List<RepositoryInfo> repositories = new ArrayList<>(20);
        return Observable.create(new Observable.OnSubscribe<List<RepositoryInfo>>() {
            @Override
            public void call(final Subscriber<? super List<RepositoryInfo>> subscriber) {
                getRepository(page)
                        .doOnTerminate(new Action0() {
                            @Override
                            public void call() {
                                subscriber.onNext(repositories);
                            }
                        })
                        .subscribe(new Subscriber<RepositoryInfo>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);
                            }

                            @Override
                            public void onNext(RepositoryInfo repositoryInfo) {
                                repositories.add(repositoryInfo);
                            }
                        });
            }
        });
    }


}
