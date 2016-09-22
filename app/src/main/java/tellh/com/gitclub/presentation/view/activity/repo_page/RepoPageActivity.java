package tellh.com.gitclub.presentation.view.activity.repo_page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.BaseActivity;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.di.component.DaggerRepoPageComponent;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.presentation.contract.RepoPageContract;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by tlh on 2016/9/22 :)
 */

public class RepoPageActivity extends BaseActivity
        implements RepoPageContract.View, View.OnClickListener {
    @Inject
    RepoPageContract.Presenter presenter;
    private String mOwner;
    private String mRepo;
    private ImageView ivHeader;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbar;
    private AppBarLayout appBarLayout;
    private Button btnWatch;
    private Button btnFork;
    private Button btnStar;
    private Button btnSource;
    private ImageView ivOwner;
    private TextView tvRepo;
    private TextView tvDesc;
    private WebView webView;
    private NestedScrollView mainContent;
    private ProgressBar progressBar;

    public static void launch(Activity srcActivity, String owner, String repo) {
        Intent intent = new Intent(srcActivity, RepoPageActivity.class);
        intent.putExtra(ExtraKey.USER_NAME, owner);
        intent.putExtra(ExtraKey.REPO_NAME, repo);
        srcActivity.startActivity(intent);
    }

    @Override
    public void showOnError(String s) {

    }

    @Override
    public void showOnLoading() {

    }

    @Override
    public void showOnSuccess() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (presenter == null) {
            DaggerRepoPageComponent.builder()
                    .appComponent(AndroidApplication.getInstance().getAppComponent())
                    .build().inject(this);
            presenter.attachView(this);
        }
//        Intent intent = getIntent();
//        if (intent != null) {
//            mOwner = intent.getStringExtra(ExtraKey.USER_NAME);
//            mRepo = intent.getStringExtra(ExtraKey.REPO_NAME);
//            presenter.getRepoInfo(mOwner, mRepo);
//            presenter.checkStarred(mOwner, mRepo);
//            presenter.checkWatch(mOwner, mRepo);
//        }

        webView.loadUrl("https://github.com/TellH/NoListAdapter/blob/master/README.md");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressBar.setProgress(newProgress);
                    progressBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(INVISIBLE);
                        }
                    }, 200);
                } else {
                    if (progressBar.getVisibility() == INVISIBLE)
                        progressBar.setVisibility(VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void initView() {

        ivHeader = (ImageView) findViewById(R.id.iv_header);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnWatch = (Button) findViewById(R.id.btn_watch);
        btnWatch.setOnClickListener(this);
        btnFork = (Button) findViewById(R.id.btn_fork);
        btnFork.setOnClickListener(this);
        btnStar = (Button) findViewById(R.id.btn_star);
        btnStar.setOnClickListener(this);
        btnSource = (Button) findViewById(R.id.btn_source);
        btnSource.setOnClickListener(this);
        ivOwner = (ImageView) findViewById(R.id.iv_owner);
        ivOwner.setOnClickListener(this);
        tvRepo = (TextView) findViewById(R.id.tv_repo);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvDesc.setOnClickListener(this);
        webView = (WebView) findViewById(R.id.web_view);
        mainContent = (NestedScrollView) findViewById(R.id.main_content);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repo_page;
    }

    @Override
    public void onGetRepositoryInfo(RepositoryInfo repositoryInfo) {

    }

    @Override
    public void onCheckStarred(Boolean result) {

    }

    @Override
    public void onCheckWatch(Boolean result) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_watch:

                break;
            case R.id.btn_fork:

                break;
            case R.id.btn_star:

                break;
            case R.id.btn_source:

                break;
        }
    }
}
