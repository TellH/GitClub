package tellh.com.gitclub.common;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.socialize.PlatformConfig;

import tellh.com.gitclub.common.utils.LogUtils;
import tellh.com.gitclub.di.component.AppComponent;
import tellh.com.gitclub.di.component.DaggerAppComponent;

/**
 * Created by tlh on 2016/8/24 :)
 */
public class AndroidApplication extends Application {
    private static AndroidApplication instance;
    private RefWatcher refWatcher;
    AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static AndroidApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LogUtils.init();
//        SharedPrefsUtils.init(this);
        Stetho.initializeWithDefaults(this);
        refWatcher = LeakCanary.install(this);
        appComponent = DaggerAppComponent.builder().build();

        //ShareSDk
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}