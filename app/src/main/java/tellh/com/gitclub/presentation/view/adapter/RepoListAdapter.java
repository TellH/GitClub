package tellh.com.gitclub.presentation.view.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.ExtraKey;
import tellh.com.gitclub.common.utils.StringUtils;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.common.wrapper.Note;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.sharedprefs.AccountPrefs;
import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;
import tellh.com.gitclub.presentation.contract.bus.event.LaunchActivityEvent;
import tellh.com.gitclub.presentation.presenter.IRepoListPresenter;
import tellh.com.gitclub.presentation.widget.IconToggleHelper;

/**
 * Created by tlh on 2016/8/31 :)
 */
public class RepoListAdapter extends BaseRecyclerAdapter<RepositoryInfo> {
    private IRepoListPresenter presenter;
    private IconToggleHelper starToggleHelper;
    private IconToggleHelper watchToggleHelper;

    public RepoListAdapter(Context ctx, List<RepositoryInfo> list, IRepoListPresenter presenter) {
        super(ctx, list);
        this.presenter = presenter;
        starToggleHelper = new IconToggleHelper(R.drawable.ic_star, R.drawable.ic_star_pressed);
        watchToggleHelper = new IconToggleHelper(R.drawable.ic_watch, R.drawable.ic_watch_pressed);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_repo;
    }

    @Override
    protected void bindData(final RecyclerViewHolder holder, final int position, final RepositoryInfo item) {
        //init
        final ImageView ivStar = holder.getImageView(R.id.iv_star);
        final ImageView ivFork = holder.getImageView(R.id.iv_fork);
        final ImageView ivWatch = holder.getImageView(R.id.iv_watch);
        if (!item.hasCheckState) {
            presenter.checkState(position, this);
            ivStar.setTag(false);
            ivWatch.setTag(false);
        }
        if (TextUtils.isEmpty(item.getLanguage())) {
            presenter.getRepoInfo(item.getOwner().getLogin(), item.getName(), new IRepoListPresenter.OnGetRepoCallback() {
                @Override
                public void onGet(RepositoryInfo result) {
                    item.setLanguage(result.getLanguage());
                }
            });
        }
        starToggleHelper.setState(ivStar, item.hasStarred);
        watchToggleHelper.setState(ivWatch, item.hasWatched);

        //set data and listener
        final ImageView ivOwner = holder.getImageView(R.id.iv_owner);
        ImageLoader.load(item.getOwner().getAvatar_url(), ivOwner);
        ivOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> params = new HashMap<>(1);
                params.put(ExtraKey.USER_NAME, item.getOwner().getLogin());
                RxBusPostman.postLaunchActivityEvent(params, LaunchActivityEvent.PERSONAL_HOME_PAGE_ACTIVITY);
            }
        });

        holder.setText(R.id.tv_repo, checkRepoNameLength(item))
                .setText(R.id.tv_desc, item.getDescription())
                .setText(R.id.tv_language, item.getLanguage() != null ? item.getLanguage() : "")
                .setText(R.id.tv_star_count, String.valueOf(item.getStars()))
                .setText(R.id.tv_fork_count, String.valueOf(item.getForks_count()))
                .setText(R.id.tv_watch_count, item.getSubscribers_count() == 0 ? "" : String.valueOf(item.getSubscribers_count()));

        holder.getView(R.id.item_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<>(1);
                params.put(ExtraKey.USER_NAME, item.getOwner().getLogin());
                params.put(ExtraKey.REPO_NAME, item.getName());
                RxBusPostman.postLaunchActivityEvent(params, LaunchActivityEvent.REPO_PAGE_ACTIVITY);
            }
        });
        ivFork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLogin()) return;
                //to ic_fork
                presenter.forkRepo(position, RepoListAdapter.this);
            }
        });
        ivStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLogin()) return;
                boolean state = starToggleHelper
                        .toggleStarCount(holder.getTextView(R.id.tv_star_count), item, ivStar)
                        .toggle(ivStar);
                //to star or unStar
                presenter.starRepo(position, RepoListAdapter.this, state);
            }
        });
        ivWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLogin()) return;
                //to watch or unWatch
                presenter.watchRepo(position, RepoListAdapter.this,
                        watchToggleHelper.toggle(ivWatch));
            }
        });

    }

    protected boolean checkLogin() {
        if (!AccountPrefs.isLogin(mContext)) {
            Note.show(Utils.getString(R.string.note_to_login));
            return false;
        }
        return true;
    }

    private String checkRepoNameLength(RepositoryInfo item) {
        String name = item.getFull_name();
        if (name.length() < 25)
            return name;
        return StringUtils.append("…/", item.getName());
    }
}
