package tellh.com.gitclub.di.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import tellh.com.gitclub.di.DiView;
import tellh.com.gitclub.model.net.DataSource.ArsenalDataSource;
import tellh.com.gitclub.model.net.DataSource.ExploreDataSource;
import tellh.com.gitclub.model.net.DataSource.GankDataSource;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;
import tellh.com.gitclub.presentation.contract.ExploreContract;
import tellh.com.gitclub.presentation.contract.LoginContract;
import tellh.com.gitclub.presentation.contract.NewsContract;
import tellh.com.gitclub.presentation.contract.RepoPageContract;
import tellh.com.gitclub.presentation.contract.RepoSourceContract;
import tellh.com.gitclub.presentation.contract.SearchContract;
import tellh.com.gitclub.presentation.presenter.ExplorePresenter;
import tellh.com.gitclub.presentation.presenter.PersonalPagePresenter;
import tellh.com.gitclub.presentation.presenter.ListRepoPresenter;
import tellh.com.gitclub.presentation.presenter.ListUserPresenter;
import tellh.com.gitclub.presentation.presenter.LoginPresenter;
import tellh.com.gitclub.presentation.presenter.NewsPresenter;
import tellh.com.gitclub.presentation.presenter.RepoPagePresenter;
import tellh.com.gitclub.presentation.presenter.RepoSourcePresenter;
import tellh.com.gitclub.presentation.presenter.SearchPresenter;
import tellh.com.gitclub.presentation.contract.PersonalPageContract.Presenter;

@Module
public class PresenterModule {
    @Provides
    public LoginContract.Presenter provideLoginPresenter(UserDataSource dataSource) {
        return new LoginPresenter(dataSource);
    }

    @Provides
    @DiView
    public SearchContract.Presenter provideSearchPresenter(RepositoryDataSource repositoryDataSource, UserDataSource userDataSource) {
        return new SearchPresenter(repositoryDataSource, userDataSource);
    }

    @Provides
    @DiView
    public ExploreContract.Presenter provideExplorePresenter(ExploreDataSource exploreDataSource, RepositoryDataSource repositoryDataSource,
                                                             GankDataSource gankDataSource, ArsenalDataSource arsenalDataSource) {
        return new ExplorePresenter(exploreDataSource, repositoryDataSource, gankDataSource, arsenalDataSource);
    }

    @Provides
    public NewsContract.Presenter provideNewsPresenter(UserDataSource userDataSource, Context context) {
        return new NewsPresenter(userDataSource, context);
    }

    @Provides
    public Presenter provideHomePagePresenter(UserDataSource userDataSource, Context ctx) {
        return new PersonalPagePresenter(userDataSource, ctx);
    }

    @Provides
    public ListRepoPresenter provideListRepoPresenter(UserDataSource userDataSource, RepositoryDataSource repositoryDataSource) {
        return new ListRepoPresenter(userDataSource, repositoryDataSource);
    }

    @Provides
    public ListUserPresenter provideListUserPresenter(RepositoryDataSource repositoryDataSource, UserDataSource userDataSource) {
        return new ListUserPresenter(repositoryDataSource, userDataSource);
    }

    @Provides
    public RepoPageContract.Presenter provideRepoPagePresenter(RepositoryDataSource repositoryDataSource) {
        return new RepoPagePresenter(repositoryDataSource);
    }

    @Provides
    public RepoSourceContract.Presenter provideRepoSourcePresenter(RepositoryDataSource repositoryDataSource) {
        return new RepoSourcePresenter(repositoryDataSource);
    }
}