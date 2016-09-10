package tellh.com.gitclub.common.base;

import android.content.Context;
import android.os.Bundle;

public interface BaseView {
    void showOnError(String s);

    void showOnLoading();

    void showOnSuccess();

    void initData(Bundle savedInstanceState);

    int getLayoutId();

    void initView();

    Context getViewContext();
}