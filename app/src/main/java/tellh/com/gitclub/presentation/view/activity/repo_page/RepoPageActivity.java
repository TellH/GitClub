package tellh.com.gitclub.presentation.view.activity.repo_page;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import javax.inject.Inject;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.BaseActivity;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.utils.StringUtils;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.di.component.DaggerRepoPageComponent;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.presentation.contract.RepoPageContract;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListContributorActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListForkerActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListStargazerActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListWatcherActivity;
import tellh.com.gitclub.presentation.view.activity.user_personal_page.PersonalHomePageActivity;
import tellh.com.gitclub.presentation.widget.ButtonToggleHelper;
import tellh.com.gitclub.presentation.widget.ErrorViewHelper;
import tellh.com.gitclub.presentation.widget.WebViewHelper;

/**
 * Created by tlh on 2016/9/22 :)
 */
// TODO: 2016/9/24 Source code activity
// TODO: 2016/9/24 load header picture
public class RepoPageActivity extends BaseActivity
        implements RepoPageContract.View, View.OnClickListener, ErrorViewHelper.OnReLoadCallback {
    @Inject
    RepoPageContract.Presenter presenter;
    private String mOwner;
    private String mRepo;
    private Toolbar toolbar;
    private Button btnWatch;
    private Button btnFork;
    private Button btnStar;
    private Button btnSource;
    private ImageView ivOwner;
    private TextView tvRepo;
    private TextView tvDesc;
    private NestedScrollView mainContent;
    private WebViewHelper webViewHelper;
    private ErrorViewHelper errorView;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private RepositoryInfo repo;
    private TextView tvLang;
    private ButtonToggleHelper btnStarToggleHelper;
    private ButtonToggleHelper btnWatchToggleHelper;

    public static void launch(Activity srcActivity, String owner, String repo) {
        Intent intent = new Intent(srcActivity, RepoPageActivity.class);
        intent.putExtra(ExtraKey.USER_NAME, owner);
        intent.putExtra(ExtraKey.REPO_NAME, repo);
        srcActivity.startActivity(intent);
    }

    @Override
    public void showOnError(String s) {
        super.showOnError(s);
        errorView.showErrorView(mainContent, this);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            mOwner = intent.getStringExtra(ExtraKey.USER_NAME);
            mRepo = intent.getStringExtra(ExtraKey.REPO_NAME);
            presenter.getReadMe(mOwner, mRepo);
            presenter.getRepoInfo(mOwner, mRepo);
            presenter.checkStarred(mOwner, mRepo);
            presenter.checkWatch(mOwner, mRepo);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewHelper.detach();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void initView() {
        if (presenter == null) {
            DaggerRepoPageComponent.builder()
                    .appComponent(AndroidApplication.getInstance().getAppComponent())
                    .build().inject(this);
            presenter.attachView(this);
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        drawerView = findViewById(R.id.drawer_view);
//        ImageView ivHeader = (ImageView) findViewById(R.id.iv_header);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnWatch = (Button) findViewById(R.id.btn_watch);
        btnWatch.setOnClickListener(this);
        btnFork = (Button) findViewById(R.id.btn_fork);
        btnFork.setOnClickListener(this);
        btnStar = (Button) findViewById(R.id.btn_star);
        btnStar.setOnClickListener(this);
        btnSource = (Button) findViewById(R.id.btn_source);
        btnSource.setOnClickListener(this);
        btnSource = (Button) findViewById(R.id.btn_source_code);
        btnSource.setOnClickListener(this);
        Button btnBrowser = (Button) findViewById(R.id.btn_open_in_browser);
        btnBrowser.setOnClickListener(this);
        Button btnContributors = (Button) findViewById(R.id.btn_contributors);
        btnContributors.setOnClickListener(this);
        Button btnStargazers = (Button) findViewById(R.id.btn_stargazers);
        btnStargazers.setOnClickListener(this);
        Button btnWatchers = (Button) findViewById(R.id.btn_watchers);
        btnWatchers.setOnClickListener(this);
        Button btnForkers = (Button) findViewById(R.id.btn_forkers);
        btnForkers.setOnClickListener(this);

        ivOwner = (ImageView) findViewById(R.id.iv_owner);
        ivOwner.setOnClickListener(this);
        tvRepo = (TextView) findViewById(R.id.tv_repo);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
        tvLang = (TextView) findViewById(R.id.tv_language);
        tvDesc.setOnClickListener(this);
        WebView webView = (WebView) findViewById(R.id.web_view);
        mainContent = (NestedScrollView) findViewById(R.id.main_content);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webViewHelper = new WebViewHelper(webView, progressBar);
        webViewHelper.setCallback(new WebViewHelper.WebViewCallback() {
            @Override
            public void onError() {
                errorView.showErrorView(mainContent, RepoPageActivity.this);
            }
        });

        errorView = new ErrorViewHelper((ViewStub) findViewById(R.id.vs_error));

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnStarToggleHelper = ButtonToggleHelper.builder()
                .setBackgroundDrawable(R.drawable.selector_button_white, R.drawable.selector_button_blue)
                .setTextColor(R.color.gray_text, R.color.white)
                .setDrawableLeft(R.drawable.ic_star_18dp, R.drawable.ic_star_white_18dp)
                .build();
        btnWatchToggleHelper = ButtonToggleHelper.builder()
                .setBackgroundDrawable(R.drawable.selector_button_white, R.drawable.selector_button_blue)
                .setTextColor(R.color.gray_text, R.color.white)
                .setDrawableLeft(R.drawable.ic_watch_18dp, R.drawable.ic_watch_white_18dp)
                .build();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repo_page;
    }

    @Override
    public void onGetRepositoryInfo(RepositoryInfo repositoryInfo) {
        this.repo = repositoryInfo;
        tvRepo.setText(StringUtils.checkRepoNameLength(repositoryInfo.getFull_name(), repositoryInfo.getName()));
        ImageLoader.load(repositoryInfo.getOwner().getAvatar_url(), ivOwner);
        tvDesc.setText(TextUtils.isEmpty(repositoryInfo.getDescription()) ? "No Description." : repositoryInfo.getDescription());
        tvLang.setText(repositoryInfo.getLanguage());
        btnStar.setText(StringUtils.formatNumber2Thousand(repositoryInfo.getStars()));
        btnFork.setText(StringUtils.formatNumber2Thousand(repositoryInfo.getForks()));
        btnWatch.setText(StringUtils.formatNumber2Thousand(repositoryInfo.getSubscribers_count()));
        toolbar.setTitle(repositoryInfo.getFull_name());
    }

    @Override
    public void onCheckStarred(Boolean result) {
        btnStarToggleHelper.setState(btnStar, result);
    }

    @Override
    public void onCheckWatch(Boolean result) {
        btnWatchToggleHelper.setState(btnWatch, result);
    }

    @Override
    public void onGetReadMe(String html_url) {
        webViewHelper.loadUrl(html_url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_owner:
                PersonalHomePageActivity.launch(this, repo.getOwner().getLogin());
                break;
            case R.id.btn_watch:
                presenter.toWatch(mOwner, mRepo, btnWatchToggleHelper.toggle(btnWatch));
                break;
            case R.id.btn_fork:
                presenter.toFork(mOwner, mRepo);
                break;
            case R.id.btn_star:
                presenter.toStar(mOwner, mRepo, btnStarToggleHelper.toggle(btnStar));
                break;
            case R.id.btn_source:
            case R.id.btn_source_code:
                // TODO: 2016/9/24 To start Source code activity
                break;
            case R.id.btn_open_in_browser:
                drawerLayout.closeDrawer(drawerView);
                String url = webViewHelper.getUrl();
                if (TextUtils.isEmpty(url))
                    break;
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                startActivity(intent);
                break;
            case R.id.btn_contributors:
                drawerLayout.closeDrawer(drawerView);
                ListContributorActivity.launch(repo.getOwner().getLogin(), repo.getName(), this);
                break;
            case R.id.btn_stargazers:
                drawerLayout.closeDrawer(drawerView);
                ListStargazerActivity.launch(repo.getOwner().getLogin(), repo.getName(), this);
                break;
            case R.id.btn_watchers:
                drawerLayout.closeDrawer(drawerView);
                ListWatcherActivity.launch(repo.getOwner().getLogin(), repo.getName(), this);
                break;
            case R.id.btn_forkers:
                drawerLayout.closeDrawer(drawerView);
                ListForkerActivity.launch(repo.getOwner().getLogin(), repo.getName(), this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!webViewHelper.goBackPage())
            super.onBackPressed();
    }

    @Override
    public void reload() {
        initData(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_repo_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_drawer) {
            drawerLayout.openDrawer(drawerView);
            return true;
        } else if (item.getItemId() == R.id.action_reload) {
            reload();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
