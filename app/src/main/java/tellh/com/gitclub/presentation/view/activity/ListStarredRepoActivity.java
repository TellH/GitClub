package tellh.com.gitclub.presentation.view.activity;

import android.app.Activity;
import android.content.Intent;

import static tellh.com.gitclub.common.config.ExtraKey.USER_NAME;

/**
 * Created by tlh on 2016/9/16 :)
 */
public class ListStarredRepoActivity extends ListRepoActivity {
    public static void launch(String user, Activity srcActivity) {
        Intent intent = new Intent(srcActivity, ListStarredRepoActivity.class);
        intent.putExtra(USER_NAME, user);
        srcActivity.startActivity(intent);
    }

    @Override
    protected String getToolbarTitle() {
        return "Stars";
    }

    @Override
    public void onToLoadMore(int curPage) {
        super.onToLoadMore(curPage);
        presenter.listStarredRepo(curPage + 1);
    }

    @Override
    public void onRefresh() {
        presenter.listStarredRepo(1);
    }
}
