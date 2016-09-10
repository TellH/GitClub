package tellh.com.gitclub.model.net.client;

import android.content.Context;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tellh.com.gitclub.R;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;

/**
 * Created by tlh on 2016/8/26 :)
 */
public class GithubOkHttpClient extends CacheOkHttpClient {
    public String getAcceptHeader() {
        return "application/vnd.github.v3.json";
    }


    @Inject
    public GithubOkHttpClient(Context ctx) {
        super(ctx);
    }

    @Override
    protected OkHttpClient.Builder enrichBuilder(OkHttpClient.Builder builder) {
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Accept", getAcceptHeader())
                        .header("User-Agent", mCtx.getString(R.string.app_name));
                if (AccountPrefs.isLogin(mCtx)) {
                    requestBuilder
                            .header("Authorization", "token " + AccountPrefs.getLoginToken(mCtx));
                }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
        return super.enrichBuilder(builder);
    }
}
