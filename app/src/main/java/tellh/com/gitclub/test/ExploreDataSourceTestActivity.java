package tellh.com.gitclub.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.DefaultSubscriber;
import tellh.com.gitclub.common.config.Constant;
import tellh.com.gitclub.common.utils.LogUtils;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.ShowCase;
import tellh.com.gitclub.model.entity.ShowCaseInfo;
import tellh.com.gitclub.model.entity.Trending;
import tellh.com.gitclub.model.net.DataSource.ArsenalDataSource;
import tellh.com.gitclub.model.net.DataSource.ExploreDataSource;
import tellh.com.gitclub.model.net.DataSource.GankDataSource;

/**
 * Created by tlh on 2016/8/27 :)
 */
public class ExploreDataSourceTestActivity extends AppCompatActivity {
    ExploreDataSource dataSource;

    GankDataSource gankDataSource;
    ArsenalDataSource arsenalDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        dataSource = AndroidApplication.getInstance().getAppComponent().exploreDataSource();
//        testListTrending();
//        testListTrending1();
//        testListTrending2();
//        testListShowCase();
//        testGetShowCaseDetail();
//        testGetGankRepositoryList();
        testGetArsenalRepositoryList();
    }

    public void testListTrending() {
        dataSource.listTrending()
                .subscribe(new DefaultSubscriber<List<Trending>>() {
                    @Override
                    public void onNext(List<Trending> trendings) {
                        LogUtils.d(trendings.get(0).getName());
                    }
                });
    }

    public void testListTrending1() {
        String language = "java";
        dataSource.listTrending(language)
                .subscribe(new DefaultSubscriber<List<Trending>>() {
                    @Override
                    public void onNext(List<Trending> trendings) {
                        LogUtils.d(trendings.get(0).getName());
                    }
                });
    }

    public void testListTrending2() {
        Map<String, String> params = new LinkedHashMap<>(2);
        params.put("language", Constant.Language.SWIFT.val());
        params.put("since", Constant.Since.THIS_MONTH.toString());
        dataSource.listTrending(params)
                .subscribe(new DefaultSubscriber<List<Trending>>() {
                    @Override
                    public void onNext(List<Trending> trendings) {
                        LogUtils.d(trendings.get(0).getName());
                    }
                });
    }

    public void testListShowCase() {
        dataSource.listShowCase()
                .subscribe(new DefaultSubscriber<List<ShowCase>>() {
                    @Override
                    public void onNext(List<ShowCase> showCases) {
                        LogUtils.d(showCases.get(0).getName());
                    }
                });
    }

    public void testGetShowCaseDetail() {
        String slug = "ember-projects";
        dataSource.getShowCaseDetail(slug)
                .subscribe(new DefaultSubscriber<ShowCaseInfo>() {
                    @Override
                    public void onNext(ShowCaseInfo showCaseInfo) {
                        LogUtils.d(showCaseInfo.getDescription());
                    }
                });
    }


    public void testGetGankRepositoryList() {
        gankDataSource = AndroidApplication.getInstance().getAppComponent().gankDataSource();
        gankDataSource.getRepositories(1)
                .subscribe(new DefaultSubscriber<List<RepositoryInfo>>() {
                    @Override
                    public void onNext(List<RepositoryInfo> repositoryInfos) {
                        LogUtils.d(repositoryInfos.toString());
                    }
                });
    }

    public void testGetArsenalRepositoryList() {
        arsenalDataSource = AndroidApplication.getInstance().getAppComponent().arsenalDataSource();
        arsenalDataSource.getRepositories(1)
                .subscribe(new DefaultSubscriber<List<RepositoryInfo>>() {
                    @Override
                    public void onNext(List<RepositoryInfo> repositoryInfos) {
                        LogUtils.d(repositoryInfos.toString());
                    }
                });
    }
}
