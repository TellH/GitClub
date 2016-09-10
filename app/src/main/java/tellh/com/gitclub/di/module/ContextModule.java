package tellh.com.gitclub.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static tellh.com.gitclub.common.AndroidApplication.getInstance;

@Module
public class ContextModule {
    @Provides @Singleton
    public Context provideContext() {
        return getInstance().getApplicationContext();
    }
}