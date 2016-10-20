package tellh.com.gitclub.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import tellh.com.gitclub.di.module.ContextModule;
import tellh.com.gitclub.di.module.NetModule;
import tellh.com.gitclub.model.net.DataSource.ArsenalDataSource;
import tellh.com.gitclub.model.net.DataSource.ExploreDataSource;
import tellh.com.gitclub.model.net.DataSource.GankDataSource;
import tellh.com.gitclub.model.net.DataSource.RepositoryDataSource;
import tellh.com.gitclub.model.net.DataSource.UserDataSource;

@Singleton
@Component(modules = {NetModule.class, ContextModule.class})
public interface AppComponent {
    Context CONTEXT();

    ExploreDataSource exploreDataSource();

    RepositoryDataSource repositoryDataSource();

    UserDataSource userDataSource();

    GankDataSource gankDataSource();

    ArsenalDataSource arsenalDataSource();
}