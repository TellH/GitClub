package tellh.com.gitclub.presentation.view.activity.detail_list;

import android.app.Activity;
import android.content.Intent;

import static tellh.com.gitclub.common.config.ExtraKey.REPO_NAME;
import static tellh.com.gitclub.common.config.ExtraKey.USER_NAME;

/**
 * Created by tlh on 2016/9/22 :)
 */

public class ListForkerActivity extends ListUserActivity {
    public static void launch(String owner, String repo, Activity srcActivity) {
        Intent intent = new Intent(srcActivity, ListForkerActivity.class);
        intent.putExtra(REPO_NAME, repo);
        intent.putExtra(USER_NAME, owner);
        srcActivity.startActivity(intent);
    }

    @Override
    protected String getToolbarTitle() {
        return "Who forks repository";
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        presenter.listForks(1);
    }

    @Override
    public void onToLoadMore(int curPage) {
        super.onToLoadMore(curPage);
        presenter.listForks(curPage + 1);
    }

}
