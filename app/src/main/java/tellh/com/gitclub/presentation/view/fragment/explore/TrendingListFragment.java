package tellh.com.gitclub.presentation.view.fragment.explore;

import android.support.v7.widget.RecyclerView;

import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;
import com.tellh.nolistadapter.viewbinder.utils.EasyEmptyRecyclerViewBinder;

import java.util.ArrayList;
import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.utils.StringUtils;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.model.entity.Trending;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.ErrorViewBinder;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.RepoListItemViewBinder;
import tellh.com.gitclub.presentation.view.fragment.ListFragment;

import static tellh.com.gitclub.presentation.contract.ExploreContract.OnGetTrendingListener;
import static tellh.com.gitclub.presentation.contract.ExploreContract.OnListFragmentInteractListener;
import static tellh.com.gitclub.presentation.contract.ExploreContract.TRENDING;

/**
 * Created by tlh on 2016/9/5 :)
 */
public class TrendingListFragment extends ListFragment implements OnGetTrendingListener {
    private OnListFragmentInteractListener mListener;
    private RecyclerViewAdapter adapter;

    public static TrendingListFragment newInstance() {
        return new TrendingListFragment();
    }

    @Override
    protected RecyclerView.Adapter getListAdapter() {
        adapter = RecyclerViewAdapter.builder()
                .addItemType(new RepoListItemViewBinder(mListener.getPresenter()))
                .setErrorView(new ErrorViewBinder(this))
                .setEmptyView(new EasyEmptyRecyclerViewBinder(R.layout.empty_view))
                .build();
        return adapter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_items_list;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mListener.onFetchData(TRENDING, 1);
    }

    void setListFragmentInteractListener(OnListFragmentInteractListener listener) {
        mListener = listener;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGet(List<Trending> trendings) {
        ArrayList<RepositoryInfo> repos = new ArrayList<>();
        for (Trending trending : trendings) {
            RepositoryInfo repositoryInfo = new RepositoryInfo();
            repositoryInfo.setName(trending.getName());
            repositoryInfo.setFull_name(StringUtils.append(trending.getOwner(), "/", trending.getName()));
            //user
            UserEntity userEntity = new UserEntity();
            userEntity.setLogin(trending.getOwner());
            userEntity.setAvatar_url(trending.getAvatarUrl());
            repositoryInfo.setOwner(userEntity);

            repositoryInfo.setDescription(trending.getDescription());
            repositoryInfo.setStars(trending.getStars());
            repositoryInfo.setForks_count(trending.getForks());
            repos.add(repositoryInfo);
        }
        adapter.refresh(repos);
        hideLoading();
    }
}
