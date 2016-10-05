package tellh.com.gitclub.presentation.view.activity.repo_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.BaseActivity;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.utils.StringUtils;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.di.component.DaggerRepoPageComponent;
import tellh.com.gitclub.model.entity.Branch;
import tellh.com.gitclub.model.entity.File;
import tellh.com.gitclub.presentation.contract.RepoSourceContract;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.DirectoryNodeBinder;
import tellh.com.gitclub.presentation.view.adapter.EasySpinnerAdapter;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.FileNodeBinder;
import tellh.com.gitclub.presentation.widget.WebViewHelper;
import tellh.com.recyclertreeview_lib.TreeNode;
import tellh.com.recyclertreeview_lib.TreeViewAdapter;

public class RepoSourceActivity extends BaseActivity implements RepoSourceContract.View, View.OnClickListener {
    @Inject
    RepoSourceContract.Presenter presenter;

    private String mOwner;
    private String mRepo;
    private WebViewHelper webViewHelper;
    private MaterialProgressBar treeViewProgressBar;
    private ImageButton btnSourceTreeRefresh;
    private DrawerLayout drawer;
    private View drawerSourceTree;
    private EasySpinnerAdapter<Branch> branchSpinnerAdapter;
    private TreeViewAdapter treeViewAdapter;

    public static void launch(Activity srcActivity, String owner, String repo) {
        Intent intent = new Intent(srcActivity, RepoSourceActivity.class);
        intent.putExtra(ExtraKey.USER_NAME, owner);
        intent.putExtra(ExtraKey.REPO_NAME, repo);
        srcActivity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewHelper.detach();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        presenter.getReadMe(mOwner, mRepo);
        treeViewProgressBar.setVisibility(View.VISIBLE);
        btnSourceTreeRefresh.setVisibility(View.INVISIBLE);
        presenter.initSourceTree(mOwner, mRepo);
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        if (intent != null && mOwner == null && mRepo == null) {
            mOwner = intent.getStringExtra(ExtraKey.USER_NAME);
            mRepo = intent.getStringExtra(ExtraKey.REPO_NAME);
        }
        if (presenter == null) {
            DaggerRepoPageComponent.builder()
                    .appComponent(AndroidApplication.getInstance().getAppComponent())
                    .build().inject(this);
            presenter.attachView(this);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final WebView webView = (WebView) findViewById(R.id.web_view);
        ProgressBar webViewProgressBar = (ProgressBar) findViewById(R.id.webView_progressBar);
        Spinner spinnerBranchList = (Spinner) findViewById(R.id.spinner_branchList);
        treeViewProgressBar = (MaterialProgressBar) findViewById(R.id.treeView_progressBar);
        btnSourceTreeRefresh = (ImageButton) findViewById(R.id.btn_refresh_source_tree);
        RecyclerView treeView = (RecyclerView) findViewById(R.id.treeView);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        drawerSourceTree = findViewById(R.id.drawer_source_tree);

        btnSourceTreeRefresh.setOnClickListener(this);

        webView.setVerticalScrollBarEnabled(false);
        webViewHelper = new WebViewHelper(webView, webViewProgressBar);

        branchSpinnerAdapter = new EasySpinnerAdapter<>(spinnerBranchList, new EasySpinnerAdapter.onItemSelectedListener<Branch>() {
            @Override
            public void onItemSelected(Branch entity, int position) {
                treeViewProgressBar.setVisibility(View.VISIBLE);
                btnSourceTreeRefresh.setVisibility(View.INVISIBLE);
                presenter.getSourceTree(mOwner, mRepo, entity);
            }
        });
        spinnerBranchList.setAdapter(branchSpinnerAdapter);

        treeView.setLayoutManager(new LinearLayoutManager(this));
        treeViewAdapter = new TreeViewAdapter(Arrays.asList(new FileNodeBinder(), new DirectoryNodeBinder()));
        treeViewAdapter.setOnTreeNodeClickListener(new TreeViewAdapter.OnTreeNodeClickListener() {
            @Override
            public boolean onClick(TreeNode node, RecyclerView.ViewHolder holder) {
                if (node.isLeaf()) {
                    webView.loadUrl(((File) node.getContent()).html_url);
                    drawer.closeDrawer(drawerSourceTree);
                    return false;
                }
                try {
                    long lastClickTime = (long) holder.itemView.getTag();
                    if (System.currentTimeMillis() - lastClickTime < 500)
                        return true;
                } catch (Exception e) {
                    holder.itemView.setTag(System.currentTimeMillis());
                }
                holder.itemView.setTag(System.currentTimeMillis());
                DirectoryNodeBinder.ViewHolder dirViewHolder = (DirectoryNodeBinder.ViewHolder) holder;
                final ImageView ivArrow = dirViewHolder.getIvArrow();
                int rotateDegree = node.isExpand() ? -90 : 90;
                ivArrow.animate().rotationBy(rotateDegree)
                        .start();
                return false;
            }
        });
        treeView.setAdapter(treeViewAdapter);

        String repoName = StringUtils.append(mOwner, "/", mRepo);
        toolbar.setTitle(repoName);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.openDrawer(drawerSourceTree);
            }
        }, 800);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repo_source;
    }

    @Override
    public void onGetBranchList(List<Branch> branches) {
        branchSpinnerAdapter.refresh(branches);
    }

    @Override
    public void onGetSourceTree(List<TreeNode> treeNodes) {
        treeViewAdapter.refresh(treeNodes);
        treeViewProgressBar.setVisibility(View.INVISIBLE);
        btnSourceTreeRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetReadMe(String html_url) {
        if (TextUtils.isEmpty(webViewHelper.getUrl()))
            webViewHelper.loadUrl(html_url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refresh_source_tree:
                treeViewProgressBar.setVisibility(View.VISIBLE);
                btnSourceTreeRefresh.setVisibility(View.INVISIBLE);
                presenter.initSourceTree(mOwner, mRepo);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_repo_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_drawer) {
            drawer.openDrawer(drawerSourceTree);
            return true;
        } else if (item.getItemId() == R.id.action_reload) {
            webViewHelper.reLoad();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showOnError(String s) {
        super.showOnError(s);
        if (s.startsWith(Utils.getString(R.string.error_get_source_tree))) {
            btnSourceTreeRefresh.setVisibility(View.VISIBLE);
            treeViewProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
