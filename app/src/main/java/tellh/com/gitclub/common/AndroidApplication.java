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
        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }
}