package tellh.com.gitclub.model.net.service;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import tellh.com.gitclub.model.entity.ArsenalRepository;

/**
 * Created by tlh on 2016/10/20 :)
 */

public interface ArsenalService {
    @GET("list")
    Observable<List<ArsenalRepository>> list(@Query("page") int page, @Query("pageSize") int pageSize);
}
