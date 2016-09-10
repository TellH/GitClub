package tellh.com.gitclub.presentation.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.Constant;
import tellh.com.gitclub.presentation.widget.ButtonToggleHelper;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.presentation.presenter.IUserListPresenter;

/**
 * Created by tlh on 2016/8/31 :)
 */
public class UserListAdapter extends BaseRecyclerAdapter<UserEntity> {
    private ButtonToggleHelper btnToggleHelper;
    private IUserListPresenter presenter;

    public UserListAdapter(Context ctx, List<UserEntity> list, IUserListPresenter presenter) {
        super(ctx, list);
        this.presenter = presenter;
        btnToggleHelper = ButtonToggleHelper.builder()
                .setBackgroundColor(R.color.light_green, R.color.gray)
                .setTextColor(R.color.white, R.color.gray_text)
                .setText(R.string.follow, R.string.unfollow)
                .build();
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_user;
    }

    @Override
    protected void bindData(RecyclerViewHolder holder, final int position, UserEntity item) {
        //fill out data
        holder.setText(R.id.tv_name, item.getLogin());
        ImageLoader.load(item.getAvatar_url(), holder.getImageView(R.id.iv_user));
        if (!TextUtils.isEmpty(item.bio)) {
            holder.setText(R.id.tv_bio, item.bio);
        } else {
            holder.setText(R.id.tv_bio, Utils.getString(R.string.default_bio));
        }

        //set up the Follow or unFollow button.
        final Button btnFollow = holder.getButton(R.id.btn_follow);
        if (!Constant.UsersType.USER.toString().equals(item.getType())) {
            btnFollow.setClickable(false);
            btnFollow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray));
            btnFollow.setTextColor(ContextCompat.getColor(mContext, R.color.gray_text));
            holder.setBackground(R.id.iv_user_type, R.drawable.ic_group);
        } else {
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //to follow or unFollow user
                    presenter.followUser(position, UserListAdapter.this, btnToggleHelper.toggle(btnFollow));
                }
            });
            btnToggleHelper.setState(btnFollow, item.isFollowing);
            holder.setBackground(R.id.iv_user_type, R.drawable.ic_person);
        }
        if (!item.hasCheck) {
            presenter.getUserInfo(position, this);
            btnFollow.setTag(false);
        }


        //jump to detail activity
        holder.setClickListener(R.id.item_container, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2016/9/1 go to the user info detail activity.
            }
        });
    }
}
