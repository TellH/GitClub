package tellh.com.gitclub.presentation.view.fragment.search;

import android.support.v7.widget.RecyclerView;

import com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper;
import com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.UpdateType;
import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;
import com.tellh.nolistadapter.viewbinder.utils.EasyEmptyRecyclerViewBinder;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.ErrorViewBinder;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.LoadMoreFooterViewBinder;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.RepoListItemViewBinder;
import tellh.com.gitclub.presentation.view.fragment.ListFragment;

import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.LOADING;
import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.PULL_TO_LOAD_MORE;
import static tellh.com.gitclub.presentation.contract.SearchContract.OnGetReposListener;
import static tellh.com.gitclub.presentation.contract.SearchContract.OnListFragmentInteractListener;
import static tellh.com.gitclub.presentation.contract.SearchContract.REPO;

public class SearchRepoFragment extends ListFragment
        implements OnGetReposListener, FooterLoadMoreAdapterWrapper.OnReachFooterListener {
    private OnListFragmentInteractListener mListener;
    private FooterLoadMoreAdapterWrapper loadMoreWrapper;

    public static SearchRepoFragment newInstance() {
        return new SearchRepoFragment();
    }

    @Override
    protected RecyclerView.Adapter getListAdapter() {
        assert mListener != null;
        loadMoreWrapper = (FooterLoadMoreAdapterWrapper) RecyclerViewAdapter.builder()
                .addItemType(new RepoListItemViewBinder(mListener.getPresenter()))
                .setLoadMoreFooter(new LoadMoreFooterViewBinder(), recyclerView, this)
                .setErrorView(new ErrorViewBinder(this))
                .setEmptyView(new EasyEmptyRecyclerViewBinder(R.layout.empty_view))
                .build();
        return loadMoreWrapper;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_items_list;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onGetRepos(int total_count, List<RepositoryInfo> items, @UpdateType int updateType) {
        loadMoreWrapper.OnGetData(items, updateType);
    }

    @Override
    public void onToLoadMore(int curPage) {
        mListener.onFetchPage(REPO, curPage + 1);
    }

    void setListFragmentInteractListener(OnListFragmentInteractListener listener) {
        mListener = listener;
    }

    @Override
    public void onRefresh() {
        mListener.onFetchPage(REPO, 1);
        loadMoreWrapper.hideErrorView(recyclerView);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (loadMoreWrapper.getFooterStatus() == LOADING)
            loadMoreWrapper.setFooterStatus(PULL_TO_LOAD_MORE);
    }
}
