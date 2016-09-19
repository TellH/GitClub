package tellh.com.gitclub.presentation.view.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
import tellh.com.gitclub.common.base.BaseActivity;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.utils.StringUtils;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.common.wrapper.Note;
import tellh.com.gitclub.di.component.DaggerHomePageComponent;
import tellh.com.gitclub.model.entity.UserInfo;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;
import tellh.com.gitclub.presentation.contract.PersonalPageContract;
import tellh.com.gitclub.presentation.widget.PersonalPageTextView;
import tellh.com.gitclub.presentation.widget.RotateIconButton;

public class PersonalHomePageActivity extends BaseActivity implements View.OnClickListener, PersonalPageContract.View {
    protected ProgressDialog progressDialog;
    @Inject
    PersonalPageContract.Presenter presenter;
    private Toolbar toolbar;
    private CircleImageView ivUser;
    private PersonalPageTextView tvFollowers;
    private PersonalPageTextView tvFollowing;
    private PersonalPageTextView tvRepo;
    private TextView tvBio;
    private Button btnContact;
    private RotateIconButton btnFollow;
    private String mUserName;
    private UserInfo mUserInfo;
    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private TextView tvBlog;
    private TextView tvEmail;
    private TextView tvName;
    private TextView tvLocation;
    private TextView tvCompany;

    public static void launch(Activity srcActivity, String user) {
        Intent intent = new Intent(srcActivity, PersonalHomePageActivity.class);
        intent.putExtra(ExtraKey.USER_NAME, user);
        srcActivity.startActivity(intent);
    }

    @Override
    public void showOnError(String s) {
        Note.show(s);
        progressDialog.dismiss();
    }

    @Override
    public void showOnLoading() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    @Override
    public void showOnSuccess() {
        progressDialog.dismiss();
        Note.show(getString(R.string.success_loading));
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        DaggerHomePageComponent.builder()
                .appComponent(AndroidApplication.getInstance().getAppComponent())
                .build().inject(this);
        presenter.attachView(this);
        Intent intent = getIntent();
        if (intent != null) {
            mUserName = intent.getStringExtra(ExtraKey.USER_NAME);
            presenter.getUserInfo(mUserName);
            presenter.checkIfFollowing(mUserName);
            UserInfo loginUser = AccountPrefs.getLoginUser(this);
            if (loginUser != null && loginUser.getLogin().equals(mUserName)) {
                btnFollow.setClickable(false);
                btnFollow.setBackgroundResource(R.drawable.selector_pink_right_checked);
            }

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_home_page;
    }

    @Override
    public void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivUser = (CircleImageView) findViewById(R.id.iv_user);
        tvFollowers = (PersonalPageTextView) findViewById(R.id.tv_followers);
        tvFollowing = (PersonalPageTextView) findViewById(R.id.tv_following);
        tvRepo = (PersonalPageTextView) findViewById(R.id.tv_repo);
        tvBio = (TextView) findViewById(R.id.tv_bio);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvBlog = (TextView) findViewById(R.id.tv_blog);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        btnContact = (Button) findViewById(R.id.btn_contact);
        btnFollow = (RotateIconButton) findViewById(R.id.btn_follow);
        FrameLayout flStars = (FrameLayout) findViewById(R.id.fl_stars);
        FrameLayout flWatching = (FrameLayout) findViewById(R.id.fl_watching);
        FrameLayout flFollowing = (FrameLayout) findViewById(R.id.fl_following);
        FrameLayout flFollowers = (FrameLayout) findViewById(R.id.fl_followers);
        FrameLayout flRepositories = (FrameLayout) findViewById(R.id.fl_repositories);

        LinearLayout bottomSheetContainer
                = (LinearLayout) findViewById(R.id.bottom_sheet_container);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer);


        tvFollowers.setOnClickListener(this);
        tvFollowing.setOnClickListener(this);
        tvRepo.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnFollow.setOnClickListener(this);
        flRepositories.setOnClickListener(this);
        flFollowers.setOnClickListener(this);
        flStars.setOnClickListener(this);
        flWatching.setOnClickListener(this);
        flFollowing.setOnClickListener(this);
        tvBlog.setOnClickListener(this);
        tvEmail.setOnClickListener(this);

        StringUtils.changeFontStype("fonts/Georgia.ttf", tvBio);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_followers:
            case R.id.fl_followers:
                ListFollowersActivity.launch(mUserName, this);
                break;
            case R.id.tv_following:
            case R.id.fl_following:
                ListFollowingUserActivity.launch(mUserName, this);
                break;
            case R.id.tv_repo:
            case R.id.fl_repositories:
                ListOwnRepoActivity.launch(mUserName, this);
                break;
            case R.id.fl_stars:
                ListStarredRepoActivity.launch(mUserName, this);
                break;
            case R.id.fl_watching:
                ListWatchingActivity.launch(mUserName, this);
                break;
            case R.id.btn_contact:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.btn_follow:
                presenter.toFollow(mUserName, btnFollow.toggle());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        presenter = null;
    }

    @Override
    public void onGetUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
        ImageLoader.load(userInfo.getAvatar_url(), ivUser);
        toolbar.setTitle(userInfo.getLogin());
        tvFollowers.setText(String.valueOf(userInfo.getFollowers()));
        tvRepo.setText(String.valueOf(userInfo.getPublic_repos()));
        tvFollowing.setText(String.valueOf(userInfo.getFollowing()));
        if (!TextUtils.isEmpty(userInfo.getBio())) {
            tvBio.setText(userInfo.getBio());
        }

        setContactData(userInfo.getName(), tvName);
        setContactData(userInfo.getCompany(), tvCompany);
        setContactData(userInfo.getBlog(), tvBlog);
        setContactData(userInfo.getLocation(), tvLocation);
        setContactData(userInfo.getEmail(), tvEmail);
    }

    protected void setContactData(String data, TextView textView) {
        if (!TextUtils.isEmpty(data))
            textView.setText(data);
        else textView.setText("No description.");
    }

    @Override
    public void onCheckFollowing(Boolean isFollowing) {
        btnFollow.setState(isFollowing);
    }
}
