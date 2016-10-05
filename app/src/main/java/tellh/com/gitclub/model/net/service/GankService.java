package tellh.com.gitclub.model.net.service;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import tellh.com.gitclub.model.entity.GankResponse;

/**
 * Created by tlh on 2016/10/5 :
 */

public interface GankService {
    @GET("{page_size}/{page}")
    Observable<GankResponse> getData(@Path("page_size") int pageSize, @Path("page") int page);
}
