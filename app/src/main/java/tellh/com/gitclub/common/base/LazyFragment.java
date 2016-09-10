package tellh.com.gitclub.common.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.Note;

public abstract class LazyFragment extends Fragment implements BaseView {
    protected boolean hasInit = false;
    protected ProgressDialog progressDialog;
    protected View mRootView;
    private Bundle mSavedInstanceState;
    private CompositeSubscription subscriptions;

    @Override
    public Context getViewContext() {
        return getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (subscriptions == null)
            subscriptions = new CompositeSubscription();

        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
            this.mSavedInstanceState = savedInstanceState;
            initView();
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCanceledOnTouchOutside(false);
        }

        if (!shouldLazyLoad())
            loadData(getUserVisibleHint(), true);
        return mRootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        loadData(isVisibleToUser, false);
    }

    private void loadData(boolean isVisibleToUser, boolean forceToLoad) {
        if (forceToLoad || (isVisibleToUser && !hasInit && shouldLazyLoad())) {
            Runnable runnable = new Runnable() {
                public void run() {
                    initData(mSavedInstanceState);
                    hasInit = true;
                }
            };
            new Handler().postDelayed(runnable, 500);
        }
    }

    protected boolean shouldLazyLoad() {
        return true;
    }

    @Override
    public void showOnError(String s) {
        progressDialog.dismiss();
        Note.show(s);
    }

    @Override
    public void showOnLoading() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void showOnSuccess() {
        progressDialog.dismiss();
        Note.show(getString(R.string.success_loading));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
        subscriptions = null;
        Utils.leakWatch(this);
    }

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

}