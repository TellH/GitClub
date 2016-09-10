package tellh.com.gitclub.common.base;

public interface MvpPresenter<V extends BaseView> {
    void attachView(V view);

    void detachView();
}