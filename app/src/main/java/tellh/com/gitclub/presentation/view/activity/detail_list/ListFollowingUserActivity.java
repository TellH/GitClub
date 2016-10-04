package tellh.com.gitclub.presentation.view.activity.detail_list;

import android.app.Activity;
import android.content.Intent;

import static tellh.com.gitclub.common.config.ExtraKey.USER_NAME;

/**
 * Created by tlh on 2016/9/16 :)
 */
public class ListFollowingUserActivity extends ListUserActivity {

    public static void launch(String user, Activity srcActivity) {
        Intent intent = new Intent(srcActivity, ListFollowingUserActivity.class);
        intent.putExtra(USER_NAME, user);
        srcActivity.startActivity(intent);
    }

    @Override
    protected String getToolbarTitle() {
        return "Following";
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        presenter.listFollowing(user, 1);
    }

    @Override
    public void onToLoadMore(int curPage) {
        super.onToLoadMore(curPage);
        presenter.listFollowing(user, curPage + 1);
    }
}
