package tellh.com.gitclub.model.net.client;

import android.content.Context;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.utils.Utils;

/**
 * Created by tlh on 2016/8/26 :)
 */
public class CacheOkHttpClient extends BaseOkHttpClient {
    public static final long SIZE_OF_CACHE = 1024 * 1024 * 50;

    protected Context mCtx;

    @Inject
    public CacheOkHttpClient(Context ctx) {
        mCtx = ctx;
    }

    @Override
    protected OkHttpClient.Builder enrichBuilder(OkHttpClient.Builder builder) {
        File cacheFile = new File(mCtx.getCacheDir(), mCtx.getString(R.string.app_name));
        Cache cache = new Cache(cacheFile, SIZE_OF_CACHE); //50Mb
        //the NetworkInterceptor is to cache setup the response caching.
        //the Interceptor is to force using cache when Network is unavailable.
        //don't combine them into a interceptor.
        builder.cache(cache).addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);
                if (Utils.isNetworkAvailable(mCtx)) {
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=600")
                            .build();
                } else {
                    return response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", CacheControl.FORCE_CACHE.toString())
                            .build();
                }
            }
        }).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!Utils.isNetworkAvailable(mCtx)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                return chain.proceed(request);
            }
        });

        return super.enrichBuilder(builder);
    }

}
