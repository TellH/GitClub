package tellh.com.gitclub.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;
import tellh.com.gitclub.presentation.view.fragment.login.LoginFragment;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginFragment.LoginCallback {

    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AccountPrefs.isLogin(this)) {
            gotoHomeActivity();
            finish();
        }
        setContentView(R.layout.activity_login);
        initView();
        loginFragment = new LoginFragment();
        loginFragment.setCallback(this);
    }

    private void gotoHomeActivity() {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }

    private void initView() {
        TextView tvLogin = (TextView) findViewById(R.id.tv_login);
        TextView tvSkip = (TextView) findViewById(R.id.tv_skip);
        tvLogin.setOnClickListener(this);
        tvSkip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                if (loginFragment.getDialog() == null)
                    loginFragment.show(getSupportFragmentManager(), ExtraKey.TAG_LOGIN_FRAGMENT);
                else loginFragment.getDialog().show();
                break;
            case R.id.tv_skip:
                gotoHomeActivity();
                finish();
                break;
        }
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
}
