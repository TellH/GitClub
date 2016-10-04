package tellh.com.gitclub.presentation.view.activity.detail_list;

import android.app.Activity;
import android.content.Intent;

import static tellh.com.gitclub.common.config.ExtraKey.REPO_NAME;
import static tellh.com.gitclub.common.config.ExtraKey.USER_NAME;

/**
 * Created by tlh on 2016/9/22 :)
 */

public class ListWatcherActivity extends ListUserActivity {
    public static void launch(String owner, String repo, Activity srcActivity) {
        Intent intent = new Intent(srcActivity, ListWatcherActivity.class);
        intent.putExtra(REPO_NAME, repo);
        intent.putExtra(USER_NAME, owner);
        srcActivity.startActivity(intent);
    }

    @Override
    protected String getToolbarTitle() {
        return "Watchers";
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        presenter.listWatchers(1);
    }

    @Override
    public void onToLoadMore(int curPage) {
        super.onToLoadMore(curPage);
        presenter.listWatchers(curPage + 1);
    }
}
