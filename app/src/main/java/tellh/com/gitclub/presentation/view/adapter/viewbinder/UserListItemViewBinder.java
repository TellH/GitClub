package tellh.com.gitclub.presentation.view.adapter.viewbinder;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tellh.nolistadapter.IListAdapter;
import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;
import com.tellh.nolistadapter.viewbinder.base.RecyclerViewBinder;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.config.IUsersType;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;
import tellh.com.gitclub.presentation.contract.bus.event.LaunchActivityEvent;
import tellh.com.gitclub.presentation.presenter.IUserListPresenter;
import tellh.com.gitclub.presentation.widget.ButtonToggleHelper;

/**
 * Created by tlh on 2016/10/4 :)
 */

public class UserListItemViewBinder extends RecyclerViewBinder<UserEntity, UserListItemViewBinder.ViewHolder> {

    private ButtonToggleHelper btnToggleHelper;
    private IUserListPresenter presenter;

    public UserListItemViewBinder(IUserListPresenter presenter) {
        this.presenter = presenter;
        btnToggleHelper = ButtonToggleHelper.builder()
                .setBackgroundDrawable(R.drawable.selector_button_green, R.drawable.selector_button_gray)
                .setTextColor(R.color.white, R.color.gray_text)
                .setText(R.string.follow, R.string.unfollow)
                .build();
    }

    @Override
    public ViewHolder provideViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final IListAdapter iListAdapter, ViewHolder holder, final int position, final UserEntity use) {
        //fill out data
        holder.tvName.setText(use.getLogin());
        ImageLoader.load(use.getAvatar_url(), holder.ivUser);
        if (!TextUtils.isEmpty(use.getBio())) {
            holder.tvBio.setText(use.getBio());
        } else {
            holder.tvBio.setText(Utils.getString(R.string.default_bio));
        }

        //set up the Follow or unFollow button.
        final Button btnFollow = holder.btnFollow;
        if (!IUsersType.USER.equals(use.getType())) {
            btnFollow.setClickable(false);
            btnFollow.setBackgroundColor(ContextCompat.getColor(btnFollow.getContext(), R.color.gray));
            btnFollow.setTextColor(ContextCompat.getColor(btnFollow.getContext(), R.color.gray_text));
            holder.ivUserType.setBackgroundResource(R.drawable.ic_group);
        } else {
            btnFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //to follow or unFollow user
                    presenter.followUser(position, (RecyclerViewAdapter) iListAdapter, btnToggleHelper.toggle(btnFollow));
                }
            });
            btnToggleHelper.setState(btnFollow, use.isFollowing);
            holder.ivUserType.setBackgroundResource(R.drawable.ic_person);
        }
        if (!use.hasCheck) {
            presenter.getUserInfo(position, (RecyclerViewAdapter) iListAdapter);
            btnFollow.setTag(false);
        }

        //jump to detail activity
        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> params = new HashMap<>(1);
                params.put(ExtraKey.USER_NAME, use.getLogin());
                RxBusPostman.postLaunchActivityEvent(params, LaunchActivityEvent.PERSONAL_HOME_PAGE_ACTIVITY);
            }
        });

    }

    @Override
    public int getItemLayoutId(IListAdapter iListAdapter) {
        return R.layout.item_user;
    }

    class ViewHolder extends RecyclerViewBinder.ViewHolder {
        private CircleImageView ivUser;
        private TextView tvName;
        private ImageView ivUserType;
        private Button btnFollow;
        private TextView tvBio;
        private FrameLayout itemContainer;

        public ViewHolder(View rootView) {
            super(rootView);
            this.ivUser = (CircleImageView) rootView.findViewById(R.id.iv_user);
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name);
            this.ivUserType = (ImageView) rootView.findViewById(R.id.iv_user_type);
            this.btnFollow = (Button) rootView.findViewById(R.id.btn_follow);
            this.tvBio = (TextView) rootView.findViewById(R.id.tv_bio);
            this.itemContainer = (FrameLayout) rootView.findViewById(R.id.item_container);
        }

    }
}
