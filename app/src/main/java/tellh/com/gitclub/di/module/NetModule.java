package tellh.com.gitclub.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tellh.com.gitclub.model.net.DataSource.ArsenalDataSource;
import tellh.com.gitclub.model.net.DataSource.ExploreDataSource;
import tellh.com.gitclub.model.net.DataSource.GankDataSource;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;
import tellh.com.gitclub.model.net.client.ArsenalRetrofit;
import tellh.com.gitclub.model.net.client.CacheOkHttpClient;
import tellh.com.gitclub.model.net.client.GithubAuthRetrofit;
import tellh.com.gitclub.model.net.client.GithubCommonRetrofit;
import tellh.com.gitclub.model.net.client.GithubExploreRetrofit;
import tellh.com.gitclub.model.net.client.GankRetrofit;
import tellh.com.gitclub.model.net.client.GithubOkHttpClient;
import tellh.com.gitclub.model.net.service.ArsenalService;
import tellh.com.gitclub.model.net.service.ExploreService;
import tellh.com.gitclub.model.net.service.GankService;
import tellh.com.gitclub.model.net.service.RepositoryService;
import tellh.com.gitclub.model.net.service.UserService;

@Module
public class NetModule {
    @Provides
    @Singleton
    public GithubCommonRetrofit provideGithubCommonRetrofit(GithubOkHttpClient client) {
        return new GithubCommonRetrofit(client);
    }

    @Provides
    public GithubAuthRetrofit provideGithubAuthRetrofit() {
        return new GithubAuthRetrofit();
    }

    @Provides
    public GithubExploreRetrofit provideGithubExploreRetrofit(CacheOkHttpClient client) {
        return new GithubExploreRetrofit(client);
    }

    @Provides
    public GankRetrofit provideGankExploreRetrofit(CacheOkHttpClient client) {
        return new GankRetrofit(client);
    }

    @Provides
    public ArsenalRetrofit provideArsenalRetrofit(CacheOkHttpClient client) {
        return new ArsenalRetrofit(client);
    }

    @Provides
    public ExploreService provideExploreService(GithubExploreRetrofit githubExploreRetrofit) {
        return githubExploreRetrofit.build().create(ExploreService.class);
    }

    @Provides
    public GankService provideGankService(GankRetrofit gankRetrofit) {
        return gankRetrofit.build().create(GankService.class);
    }

    @Provides
    public ArsenalService provideArsenalService(ArsenalRetrofit arsenalRetrofit) {
        return arsenalRetrofit.build().create(ArsenalService.class);
    }

    @Provides
    public RepositoryService provideRepositoryService(GithubCommonRetrofit githubCommonRetrofit) {
        return githubCommonRetrofit.build().create(RepositoryService.class);
    }

    @Provides
    public UserService provideUserService(GithubCommonRetrofit githubCommonRetrofit) {
        return githubCommonRetrofit.build().create(UserService.class);
    }

    @Provides
    @Singleton
    public UserDataSource provideUserDataSource(GithubAuthRetrofit authRetrofit, UserService userApi, Context ctx) {
        return new UserDataSource(authRetrofit, userApi, ctx);
    }

    @Provides
    @Singleton
    public RepositoryDataSource provideRepositoryDataSource(RepositoryService repositoryApi) {
        return new RepositoryDataSource(repositoryApi);
    }

    @Provides
    @Singleton
    public ExploreDataSource provideExploreDataSource(ExploreService exploreApi) {
        return new ExploreDataSource(exploreApi);
    }

    @Provides
    @Singleton
    public GankDataSource provideGankDataSource(GankService gankService, RepositoryService repositoryService) {
        return new GankDataSource(gankService, repositoryService);
    }

    @Provides
    @Singleton
    public ArsenalDataSource provideArsenalDataSource(ArsenalService arsenalService, RepositoryService repositoryService) {
        return new ArsenalDataSource(arsenalService, repositoryService);
    }
}