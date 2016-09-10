package tellh.com.gitclub.model.net.DataSource;

import android.text.TextUtils;

import java.util.List;
import java.util.Map;

import rx.Observable;
import tellh.com.gitclub.common.utils.RxJavaUtils;
import tellh.com.gitclub.model.entity.ShowCase;
import tellh.com.gitclub.model.entity.ShowCaseInfo;
import tellh.com.gitclub.model.entity.Trending;
import tellh.com.gitclub.model.net.service.ExploreService;

/**
 * Created by tlh on 2016/8/27 :)
 */
public class ExploreDataSource {
    private ExploreService api;

    public ExploreDataSource(ExploreService api) {
        this.api = api;
    }

    public Observable<List<Trending>> listTrending() {
        return api.listTrending().compose(RxJavaUtils.<List<Trending>>applySchedulers());
    }

    public Observable<List<Trending>> listTrending(String language) {
        if (TextUtils.isEmpty(language))
            return listTrending();
        return api.listTrending(language)
                .compose(RxJavaUtils.<List<Trending>>applySchedulers());
    }

    //key: language & since
    public Observable<List<Trending>> listTrending(Map<String, String> params) {
        return api.listTrending(params)
                .compose(RxJavaUtils.<List<Trending>>applySchedulers());
    }

    public Observable<List<ShowCase>> listShowCase() {
        return api.listShowCase()
                .compose(RxJavaUtils.<List<ShowCase>>applySchedulers());
    }

    public Observable<ShowCaseInfo> getShowCaseDetail(String slug) {
        return api.getShowCaseDetail(slug)
                .compose(RxJavaUtils.<ShowCaseInfo>applySchedulers());
    }
}
