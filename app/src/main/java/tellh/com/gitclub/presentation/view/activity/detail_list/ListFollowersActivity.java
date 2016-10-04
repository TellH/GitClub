package tellh.com.gitclub.presentation.view.activity.detail_list;

import android.app.Activity;
import android.content.Intent;

import static tellh.com.gitclub.common.config.ExtraKey.USER_NAME;

/**
 * Created by tlh on 2016/9/16 :)
 */
public class ListFollowersActivity extends ListUserActivity {

    public static void launch(String user, Activity srcActivity) {
        Intent intent = new Intent(srcActivity, ListFollowersActivity.class);
        intent.putExtra(USER_NAME, user);
        srcActivity.startActivity(intent);
    }

    @Override
    protected String getToolbarTitle() {
        return "Followers";
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        presenter.listFollowers(user, 1);
    }

    @Override
    public void onToLoadMore(int curPage) {
        super.onToLoadMore(curPage);
        presenter.listFollowers(user, curPage + 1);
    }
}
