package tellh.com.gitclub.model.net.DataSource;

import android.text.TextUtils;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import tellh.com.gitclub.common.config.Constant;
import tellh.com.gitclub.common.utils.RxJavaUtils;
import tellh.com.gitclub.model.entity.GankResponse;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.net.service.GankService;

/**
 * Created by tlh on 2016/10/5 :)
 */

public class GankDataSource {
    private GankService api;

    public GankDataSource(GankService api) {
        this.api = api;
    }

    public Observable<List<GankResponse.ResultsEntity>> getData(int page) {
        return api.getData(Constant.PER_PAGE, page)
                .map(new Func1<GankResponse, List<GankResponse.ResultsEntity>>() {
                    @Override
                    public List<GankResponse.ResultsEntity> call(GankResponse gankResponse) {
                        return gankResponse.getResults();
                    }
                }).compose(RxJavaUtils.<List<GankResponse.ResultsEntity>>applySchedulers());
    }

    public Observable<RepositoryInfo> getRepoInfo(int page) {
        getData(page)
                .flatMap(new Func1<List<GankResponse.ResultsEntity>, Observable<RepositoryInfo>>() {
                    @Override
                    public Observable<RepositoryInfo> call(List<GankResponse.ResultsEntity> resultsEntities) {
                        for (GankResponse.ResultsEntity resultsEntity : resultsEntities) {
                            String url = resultsEntity.getUrl();
                            if (TextUtils.isEmpty(url) || !url.startsWith("https://github.com/"))
                                continue;
                        }
                        return null;
                    }
                });
        return null;
    }

}
