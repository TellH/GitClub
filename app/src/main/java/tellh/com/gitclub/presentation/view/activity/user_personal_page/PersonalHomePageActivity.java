package tellh.com.gitclub.presentation.view.activity.user_personal_page;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;

import javax.inject.Inject;

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
import tellh.com.gitclub.presentation.view.activity.detail_list.ListFollowersActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListFollowingUserActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListOwnRepoActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListStarredRepoActivity;
import tellh.com.gitclub.presentation.view.activity.detail_list.ListWatchingActivity;
import tellh.com.gitclub.presentation.widget.ErrorViewHelper;
import tellh.com.gitclub.presentation.widget.PersonalPageTextView;
import tellh.com.gitclub.presentation.widget.RotateIconButton;
import tellh.com.gitclub.presentation.widget.UmengShareCallback;

public class PersonalHomePageActivity extends BaseActivity
        implements View.OnClickListener, PersonalPageContract.View {
    protected ProgressDialog progressDialog;
    @Inject
    PersonalPageContract.Presenter presenter;
    private Toolbar toolbar;
    private TextView tvBio;
    private ImageView ivUser;
    private PersonalPageTextView tvFollowers;
    private PersonalPageTextView tvFollowing;
    private PersonalPageTextView tvRepo;
    private RotateIconButton btnFollow;
    private String mUserName;
    private UserInfo mUserInfo;
    private ErrorViewHelper errorView;
    private NestedScrollView mainContent;
    private ContactUserInfoBottomSheetDialog bottomSheetDialog;

    public static void launch(Activity srcActivity, String user) {
        Intent intent = new Intent(srcActivity, PersonalHomePageActivity.class);
        intent.putExtra(ExtraKey.USER_NAME, user);
        srcActivity.startActivity(intent);
    }

    @Override
    public void showOnError(String s) {
        Note.show(s);
        progressDialog.dismiss();

        if (!errorView.isShowing()) {
            errorView.showErrorView(mainContent, new ErrorViewHelper.OnReLoadCallback() {
                @Override
                public void reload() {
                    presenter.getUserInfo(mUserName);
                }
            });
        }
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
        if (errorView.isShowing())
            errorView.hideErrorView(mainContent);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        if (presenter == null) {
            DaggerHomePageComponent.builder()
                    .appComponent(AndroidApplication.getInstance().getAppComponent())
                    .build().inject(this);
            presenter.attachView(this);
        }
        Intent intent = getIntent();
        if (intent != null) {
            mUserName = intent.getStringExtra(ExtraKey.USER_NAME);
            presenter.getUserInfo(mUserName);
            UserInfo loginUser = AccountPrefs.getLoginUser(this);
            if (loginUser != null && loginUser.getLogin().equals(mUserName)) {
                btnFollow.setClickable(false);
                btnFollow.setBackgroundResource(R.drawable.selector_pink_right_checked);
            } else {
                presenter.checkIfFollowing(mUserName);
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

        errorView = new ErrorViewHelper((ViewStub) findViewById(R.id.vs_error));

        mainContent = (NestedScrollView) findViewById(R.id.main_content);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivUser = (ImageView) findViewById(R.id.iv_user);
        tvFollowers = (PersonalPageTextView) findViewById(R.id.tv_followers);
        tvFollowing = (PersonalPageTextView) findViewById(R.id.tv_following);
        tvRepo = (PersonalPageTextView) findViewById(R.id.tv_repo);
        tvBio = (TextView) findViewById(R.id.tv_bio);

        Button btnContact = (Button) findViewById(R.id.btn_contact);
        btnFollow = (RotateIconButton) findViewById(R.id.btn_follow);
        FrameLayout flStars = (FrameLayout) findViewById(R.id.fl_stars);
        FrameLayout flWatching = (FrameLayout) findViewById(R.id.fl_watching);
        FrameLayout flFollowing = (FrameLayout) findViewById(R.id.fl_following);
        FrameLayout flFollowers = (FrameLayout) findViewById(R.id.fl_followers);
        FrameLayout flRepositories = (FrameLayout) findViewById(R.id.fl_repositories);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0)
                    ViewCompat.setAlpha(ivUser, 1);
                else if (verticalOffset >= -100)
                    ViewCompat.setAlpha(ivUser, (float) 1 + verticalOffset / 100f);
                else ViewCompat.setAlpha(ivUser, 0);
            }
        });
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

        StringUtils.changeFontStyle("fonts/Georgia.ttf", tvBio);
        setSupportActionBar(toolbar);
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
                if (bottomSheetDialog == null)
                    bottomSheetDialog = new ContactUserInfoBottomSheetDialog(this, mUserInfo);
                bottomSheetDialog.show();
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
        tvFollowers.setText(StringUtils.formatNumber2Thousand(userInfo.getFollowers()));
        tvRepo.setText(StringUtils.formatNumber2Thousand(userInfo.getPublic_repos()));
        tvFollowing.setText(StringUtils.formatNumber2Thousand(userInfo.getFollowing()));
        if (!TextUtils.isEmpty(userInfo.getBio())) {
            tvBio.setText(userInfo.getBio());
        }
        if (bottomSheetDialog == null)
            bottomSheetDialog = new ContactUserInfoBottomSheetDialog(this, mUserInfo);
        bottomSheetDialog.refreshData(userInfo);
    }


    @Override
    public void onCheckFollowing(Boolean isFollowing) {
        btnFollow.setState(isFollowing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_personal_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_open_in_browser) {
            if (mUserInfo == null || TextUtils.isEmpty(mUserInfo.getHtml_url()))
                return super.onOptionsItemSelected(item);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(mUserInfo.getHtml_url()));
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            new ShareAction(PersonalHomePageActivity.this)
                    .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN,
                            SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.EVERNOTE,
                            SHARE_MEDIA.POCKET, SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.EMAIL, SHARE_MEDIA.YNOTE, SHARE_MEDIA.MORE)
                    .withTitle("Users from Github")
                    .withText("Users from Github: " + mUserName)
                    .withTargetUrl(mUserInfo.getHtml_url())
                    .setCallback(new UmengShareCallback())
                    .open();
        }
        return super.onOptionsItemSelected(item);
    }

}
