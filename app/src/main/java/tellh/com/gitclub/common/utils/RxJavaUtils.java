package tellh.com.gitclub.common.utils;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import tellh.com.gitclub.common.base.BaseView;

/**
 * Created by tlh on 2016/8/27 :)
 */
public class RxJavaUtils {
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> Observable.Transformer<T, T> setLoadingListener(final BaseView view) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        view.showOnLoading();
                    }
                });
            }
        };
    }

    public static Observable.Transformer<Response<ResponseBody>, Boolean> checkIfSuccessCode() {
        return new Observable.Transformer<Response<ResponseBody>, Boolean>() {
            @Override
            public Observable<Boolean> call(Observable<Response<ResponseBody>> observable) {
                return observable.map(new Func1<Response<ResponseBody>, Boolean>() {
                    @Override
                    public Boolean call(Response<ResponseBody> response) {
                        return response != null && response.code() == 204;
                    }
                });
            }
        };
    }
}
