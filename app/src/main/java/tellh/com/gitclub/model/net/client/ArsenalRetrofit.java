package tellh.com.gitclub.model.net.client;

import okhttp3.OkHttpClient;
import tellh.com.gitclub.common.config.Constant;

/**
 * Created by tlh on 2016/8/26 :)
 */
public class ArsenalRetrofit extends BaseRetrofit {
    private CacheOkHttpClient client;

    public ArsenalRetrofit(CacheOkHttpClient client) {
        this.client = client;
    }

    @Override
    protected String getBaseUrl() {
        return Constant.URL_ARSENAL;
    }

    @Override
    protected OkHttpClient getOkHttpClient() {
        return client.build();
    }
}
