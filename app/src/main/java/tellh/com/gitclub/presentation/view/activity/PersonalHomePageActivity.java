package tellh.com.gitclub.presentation.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import javax.inject.Inject;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.BaseActivity;
import tellh.com.gitclub.common.utils.StringUtils;
import tellh.com.gitclub.di.component.DaggerHomePageComponent;
import tellh.com.gitclub.presentation.contract.PersonalPageContract;

public class PersonalHomePageActivity extends BaseActivity {

    @Inject
    PersonalPageContract.Presenter presenter;

    private String user;

    public static void launch(Activity srcActivity, String user) {
        srcActivity.startActivity(new Intent(srcActivity, PersonalHomePageActivity.class));
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        DaggerHomePageComponent.builder()
                .appComponent(AndroidApplication.getInstance().getAppComponent())
                .build().inject(this);
    }


    @Override
    protected void initView() {
        TextView tvBio = (TextView) findViewById(R.id.tv_bio);
        StringUtils.changeFontStype("fonts/Georgia.ttf", tvBio);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_home_page;
    }
}
