package tellh.com.gitclub.presentation.view.activity.user_personal_page;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.Note;
import tellh.com.gitclub.model.entity.UserInfo;
import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;
import tellh.com.gitclub.presentation.contract.bus.event.LaunchActivityEvent;

/**
 * Created by tlh on 2016/9/19 :)
 */
public class ContactUserInfoBottomSheetDialog extends BottomSheetDialog implements View.OnClickListener {
    private UserInfo mUser;
    private TextView tvBlog;
    private TextView tvEmail;
    private TextView tvName;
    private TextView tvLocation;
    private TextView tvCompany;

    public ContactUserInfoBottomSheetDialog(@NonNull Context context, UserInfo userInfo) {
        super(context);
        initView();
        mUser = userInfo;
    }

    protected void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_contact, null, false);
        setContentView(view);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvBlog = (TextView) findViewById(R.id.tv_blog);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvBlog.setOnClickListener(this);
        tvEmail.setOnClickListener(this);

        //set bottom sheet behaviour
        View sheetView = getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        assert sheetView != null;
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(sheetView);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_blog:
                if (mUser == null || TextUtils.isEmpty(mUser.getBlog()))
                    break;
                Map<String, String> params = new HashMap<>(1);
                params.put(ExtraKey.NAME_BLOG, mUser.getBlog());
                RxBusPostman.postLaunchActivityEvent(params, LaunchActivityEvent.BROWSER_ACTIVITY);
                break;
            case R.id.tv_email:
                if (mUser == null || TextUtils.isEmpty(mUser.getEmail()))
                    break;
                Utils.copyDataToClipBoard(mUser.getEmail());
                Note.show("Email address has been copied to clipboard.");
                break;
        }
    }

    public void refreshData(UserInfo userInfo) {
        mUser = userInfo;
        setContactData(userInfo.getName(), tvName);
        setContactData(userInfo.getCompany(), tvCompany);
        setContactData(userInfo.getBlog(), tvBlog);
        setContactData(userInfo.getLocation(), tvLocation);
        setContactData(userInfo.getEmail(), tvEmail);
    }


    protected void setContactData(String data, TextView textView) {
        if (!TextUtils.isEmpty(data))
            textView.setText(data);
        else textView.setText(R.string.no_description);
    }
}
