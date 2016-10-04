package tellh.com.gitclub.presentation.view.fragment.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.base.LazyFragment;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.common.wrapper.Note;
import tellh.com.gitclub.model.entity.UserInfo;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListFollowersActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListFollowingUserActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListOwnRepoActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListStarredRepoActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListWatchingActivity;
import tellh.com.gitclub.presentation.view.activity.user_personal_page.PersonalHomePageActivity;
import tellh.com.gitclub.presentation.view.fragment.login.LoginFragment;

public class HomePageFragment extends LazyFragment
        implements LoginFragment.LoginCallback, View.OnClickListener {
    private ImageView ivUser;
    private TextView tvUser;

    private UserInfo loginUser;
    private LoginFragment loginFragment;

    public HomePageFragment() {
        // Required empty public constructor
    }

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        loginUser = AccountPrefs.getLoginUser(getContext());
        if (loginUser != null) {
            ImageLoader.load(loginUser.getAvatar_url(), ivUser);
            tvUser.setText(loginUser.getLogin());
        } else {
            ivUser.setImageResource(R.mipmap.ic_launcher);
            tvUser.setText("Please login in.");
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_home_page;
    }

    @Override
    public void initView() {
        ivUser = (ImageView) mRootView.findViewById(R.id.iv_user);
        tvUser = (TextView) mRootView.findViewById(R.id.tv_user);
        FrameLayout flPersonalPage = (FrameLayout) mRootView.findViewById(R.id.fl_personal_page);
        FrameLayout flStars = (FrameLayout) mRootView.findViewById(R.id.fl_stars);
        FrameLayout flWatching = (FrameLayout) mRootView.findViewById(R.id.fl_watching);
        FrameLayout flFollowing = (FrameLayout) mRootView.findViewById(R.id.fl_following);
        FrameLayout flFollowers = (FrameLayout) mRootView.findViewById(R.id.fl_followers);
        FrameLayout flRepositories = (FrameLayout) mRootView.findViewById(R.id.fl_repositories);
        FrameLayout flSettings = (FrameLayout) mRootView.findViewById(R.id.fl_settings);
        TextView tvSignOut = (TextView) mRootView.findViewById(R.id.tv_sign_out);

        flPersonalPage.setOnClickListener(this);
        flStars.setOnClickListener(this);
        flRepositories.setOnClickListener(this);
        flFollowers.setOnClickListener(this);
        flWatching.setOnClickListener(this);
        flFollowing.setOnClickListener(this);
        flSettings.setOnClickListener(this);
        tvSignOut.setOnClickListener(this);
    }

    public void showLoginDialog() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            loginFragment.setCallback(this);
        }
        if (loginFragment.getDialog() == null)
            loginFragment.show(getFragmentManager(), ExtraKey.TAG_LOGIN_FRAGMENT);
        else loginFragment.getDialog().show();
    }

    @Override
    public void onSuccessToLogin() {
        //dismiss the login dialog
        loginFragment.setDismissable(true);
        loginFragment.dismiss();

        initData(null);
    }

    @Override
    public void onDismissLogin() {
        loginFragment = null;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() != R.id.fl_settings && !AccountPrefs.isLogin(getContext())) {
            showLoginDialog();
            return;
        } else if (loginUser == null) {
            initData(null);
        }
        switch (view.getId()) {
            case R.id.fl_personal_page:
                PersonalHomePageActivity.launch(getActivity(), loginUser.getLogin());
                break;
            case R.id.fl_stars:
                ListStarredRepoActivity.launch(loginUser.getLogin(), getActivity());
                break;
            case R.id.fl_watching:
                ListWatchingActivity.launch(loginUser.getLogin(), getActivity());
                break;
            case R.id.fl_followers:
                ListFollowersActivity.launch(loginUser.getLogin(), getActivity());
                break;
            case R.id.fl_following:
                ListFollowingUserActivity.launch(loginUser.getLogin(), getActivity());
                break;
            case R.id.fl_repositories:
                ListOwnRepoActivity.launch(loginUser.getLogin(), getActivity());
                break;
            case R.id.fl_settings:
                // TODO: 2016/9/16 start settings activity
                Note.show("start settings activity");
                break;
            case R.id.tv_sign_out:
                new MaterialDialog.Builder(getContext())
                        .title(R.string.Confirm)
                        .content(R.string.confirm_sign_out)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                AccountPrefs.removeLoginUser(getContext());
                                Note.showBar(Utils.getString(R.string.success_sign_out), getView());
                                initData(null);
                            }
                        })
                        .positiveText("Yes")
                        .negativeText("Cancel")
                        .show();
                break;
        }
    }
}
