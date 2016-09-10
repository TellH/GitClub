package tellh.com.gitclub.presentation.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import tellh.com.gitclub.R;
import tellh.com.gitclub.presentation.view.fragment.login.LoginFragment;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        loginFragment = new LoginFragment();
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
                    loginFragment.show(getSupportFragmentManager(), "LoginFragment");
                else loginFragment.getDialog().show();
                break;
            case R.id.tv_skip:
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
                break;
        }
    }

    public void onLoginSuccess() {
        loginFragment.dismiss();
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }
}
