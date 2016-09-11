package tellh.com.gitclub.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntroFragment;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;
import tellh.com.gitclub.presentation.view.fragment.login.LoginFragment;
import tellh.com.gitclub.presentation.widget.AppIntroBase;

/**
 * Created by tlh on 2016/9/10 :)
 */
public class IntroActivity extends AppIntroBase implements LoginFragment.LoginCallback {
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //check login
        if (AccountPrefs.isLogin(this)) {
            gotoHomeActivity();
            finish();
        }
        super.onCreate(savedInstanceState);
        addIntroPage();

        //init Login Dialog
        loginFragment = new LoginFragment();
        loginFragment.setCallback(this);
    }

    private void addIntroPage() {
        addSlide(AppIntroFragment.newInstance(
                "See what people are working on",
                "Follow friends or developers you admire to learn what they are working on.",
                R.drawable.intro_see,
                ContextCompat.getColor(this, R.color.dark_purple)
        ));
        addSlide(AppIntroFragment.newInstance(
                "Learn how developers build software",
                "Learn how developers build and maintain open source software. You can watch a project that interests you to see its progress as it happens.",
                R.drawable.intro_watch,
                ContextCompat.getColor(this, R.color.blue)
        ));
        addSlide(AppIntroFragment.newInstance(
                "Share your work with the world",
                "Share your project so others can use it or to get feedback from the GitHub community.",
                R.drawable.intro_share,
                ContextCompat.getColor(this, R.color.teal)
        ));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.intro;
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        loginFragment.setDismissable(true);
        gotoHomeActivity();
        finish();
    }

    @Override
    public void onLoginPressed(Fragment currentFragment) {
        super.onLoginPressed(currentFragment);
        if (loginFragment.getDialog() == null)
            loginFragment.show(getSupportFragmentManager(), ExtraKey.TAG_LOGIN_FRAGMENT);
        else loginFragment.getDialog().show();
    }

    @Override
    public void onSuccessToLogin() {
        loginFragment.setDismissable(true);
        loginFragment.dismiss();
        gotoHomeActivity();
        finish();
    }

    @Override
    public void onDismissLogin() {
        loginFragment = null;
    }

    private void gotoHomeActivity() {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
