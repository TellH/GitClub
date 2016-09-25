package tellh.com.gitclub.common.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.wrapper.Note;

public abstract class BaseActivity extends AppCompatActivity implements BaseView {
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        initView();
        initData(savedInstanceState);
    }

    public abstract void initData(Bundle savedInstanceState);

    public abstract void initView();

    public abstract int getLayoutId();

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
    public Context getViewContext() {
        return this;
    }
}
