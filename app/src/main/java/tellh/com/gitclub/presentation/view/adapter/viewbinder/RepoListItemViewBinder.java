package tellh.com.gitclub.presentation.view.adapter.viewbinder;

import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tellh.nolistadapter.IListAdapter;
import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;
import com.tellh.nolistadapter.viewbinder.base.RecyclerViewBinder;

import java.util.HashMap;
import java.util.Map;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.AndroidApplication;
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
 * Created by tlh on 2016/10/4 :)
 */

public class RepoListItemViewBinder extends RecyclerViewBinder<RepositoryInfo, RepoListItemViewBinder.ViewHolder> {
    private IRepoListPresenter presenter;
    private IconToggleHelper starToggleHelper;
    private IconToggleHelper watchToggleHelper;

    public RepoListItemViewBinder(IRepoListPresenter presenter) {
        this.presenter = presenter;
        starToggleHelper = new IconToggleHelper(R.drawable.ic_star, R.drawable.ic_star_pressed);
        watchToggleHelper = new IconToggleHelper(R.drawable.ic_watch, R.drawable.ic_watch_pressed);
    }

    @Override
    public ViewHolder provideViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final IListAdapter iListAdapter, final ViewHolder holder, final int position, final RepositoryInfo repo) {
        //init
        if (!repo.hasCheckState) {
            presenter.checkState(position, (RecyclerViewAdapter) iListAdapter);
            holder.ivStar.setTag(false);
            holder.ivWatch.setTag(false);
        }
        if (TextUtils.isEmpty(repo.getLanguage())) {
            presenter.getRepoInfo(repo.getOwner().getLogin(), repo.getName(), new IRepoListPresenter.OnGetRepoCallback() {
                @Override
                public void onGet(RepositoryInfo result) {
                    repo.setLanguage(result.getLanguage());
                }
            });
        }
        starToggleHelper.setState(holder.ivStar, repo.hasStarred);
        watchToggleHelper.setState(holder.ivWatch, repo.hasWatched);

        //set data and listener
        ImageLoader.load(repo.getOwner().getAvatar_url(), holder.ivOwner);
        holder.ivOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> params = new HashMap<>(1);
                params.put(ExtraKey.USER_NAME, repo.getOwner().getLogin());
                RxBusPostman.postLaunchActivityEvent(params, LaunchActivityEvent.PERSONAL_HOME_PAGE_ACTIVITY);
            }
        });

        holder.tvRepo.setText(StringUtils.checkRepoNameLength(repo.getFull_name(), repo.getName()));
        holder.tvDesc.setText(repo.getDescription());
        holder.tvLanguage.setText(repo.getLanguage() != null ? repo.getLanguage() : "");
        holder.tvStarCount.setText(String.valueOf(repo.getStars()));
        holder.tvForkCount.setText(String.valueOf(repo.getForks_count()));
        holder.tvWatchCount.setText(repo.getSubscribers_count() == 0 ? "" : String.valueOf(repo.getSubscribers_count()));

        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<>(1);
                params.put(ExtraKey.USER_NAME, repo.getOwner().getLogin());
                params.put(ExtraKey.REPO_NAME, repo.getName());
                RxBusPostman.postLaunchActivityEvent(params, LaunchActivityEvent.REPO_PAGE_ACTIVITY);
            }
        });
        holder.ivFork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLogin()) return;
                //to ic_fork
                presenter.forkRepo(position, (RecyclerViewAdapter) iListAdapter);
            }
        });
        holder.ivStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLogin()) return;
                boolean state = starToggleHelper
                        .toggleStarCount(holder.tvStarCount, repo, holder.ivStar)
                        .toggle(holder.ivStar);
                //to star or unStar
                presenter.starRepo(position, (RecyclerViewAdapter) iListAdapter, state);
            }
        });
        holder.ivWatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLogin()) return;
                //to watch or unWatch
                presenter.watchRepo(position, (RecyclerViewAdapter) iListAdapter,
                        watchToggleHelper.toggle(holder.ivWatch));
            }
        });

    }

    @Override
    public int getItemLayoutId(IListAdapter iListAdapter) {
        return R.layout.item_repo;
    }

    private boolean checkLogin() {
        if (!AccountPrefs.isLogin(AndroidApplication.getInstance())) {
            Note.show(Utils.getString(R.string.note_to_login));
            return false;
        }
        return true;
    }

    class ViewHolder extends RecyclerViewBinder.ViewHolder {
        private ImageView ivOwner;
        private TextView tvRepo;
        private TextView tvDesc;
        private TextView tvLanguage;
        private ImageView ivStar;
        private TextView tvStarCount;
        private ImageView ivFork;
        private TextView tvForkCount;
        private ImageView ivWatch;
        private TextView tvWatchCount;
        private CardView itemContainer;

        public ViewHolder(View rootView) {
            super(rootView);
            this.ivOwner = (ImageView) rootView.findViewById(R.id.iv_owner);
            this.tvRepo = (TextView) rootView.findViewById(R.id.tv_repo);
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.tvLanguage = (TextView) rootView.findViewById(R.id.tv_language);
            this.ivStar = (ImageView) rootView.findViewById(R.id.iv_star);
            this.tvStarCount = (TextView) rootView.findViewById(R.id.tv_star_count);
            this.ivFork = (ImageView) rootView.findViewById(R.id.iv_fork);
            this.tvForkCount = (TextView) rootView.findViewById(R.id.tv_fork_count);
            this.ivWatch = (ImageView) rootView.findViewById(R.id.iv_watch);
            this.tvWatchCount = (TextView) rootView.findViewById(R.id.tv_watch_count);
            this.itemContainer = (CardView) rootView.findViewById(R.id.item_container);
        }

    }
}
