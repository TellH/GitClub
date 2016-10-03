package tellh.com.gitclub.presentation.view.activity.repo_page;

import android.os.Bundle;

import java.util.List;

import javax.inject.Inject;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.BaseActivity;
import tellh.com.gitclub.di.component.DaggerRepoPageComponent;
import tellh.com.gitclub.model.entity.Branch;
import tellh.com.gitclub.presentation.contract.RepoSourceContract;
import tellh.com.recyclertreeview_lib.TreeNode;

public class RepoSourceActivity extends BaseActivity implements RepoSourceContract.View {
    @Inject
    RepoSourceContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_source);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {
        if (presenter == null) {
            DaggerRepoPageComponent.builder()
                    .appComponent(AndroidApplication.getInstance().getAppComponent())
                    .build().inject(this);
            presenter.attachView(this);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_repo_source;
    }

    @Override
    public void onGetBranchList(List<Branch> branches) {

    }

    @Override
    public void onGetSourceTree(List<TreeNode> treeNodes) {

    }

    @Override
    public void onGetReadMe(String html_url) {

    }
}
