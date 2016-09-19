package tellh.com.gitclub.presentation.view.fragment.search;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.FooterState;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.UpdateType;
import tellh.com.gitclub.presentation.view.adapter.RepoListAdapter;
import tellh.com.gitclub.presentation.view.fragment.ListFragment;

import static tellh.com.gitclub.presentation.contract.SearchContract.ListType;
import static tellh.com.gitclub.presentation.contract.SearchContract.OnGetReposListener;
import static tellh.com.gitclub.presentation.contract.SearchContract.OnListFragmentInteractListener;

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
        loadMoreWrapper = new FooterLoadMoreAdapterWrapper(new RepoListAdapter(getContext(), null, mListener.getPresenter()));
        loadMoreWrapper.addFooter(R.layout.footer_load_more);
        loadMoreWrapper.setOnReachFooterListener(recyclerView, this);
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
    public void onGetRepos(int total_count, List<RepositoryInfo> items, UpdateType updateType) {
        loadMoreWrapper.OnGetData(items, updateType);
    }

    @Override
    public void onToLoadMore(int curPage) {
        mListener.onFetchPage(ListType.REPO, curPage + 1);
    }

    void setListFragmentInteractListener(OnListFragmentInteractListener listener) {
        mListener = listener;
    }

    @Override
    public void onRefresh() {
        mListener.onFetchPage(ListType.REPO, 1);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (loadMoreWrapper.getFooterStatus() == FooterState.LOADING)
            loadMoreWrapper.setFooterStatus(FooterState.PULL_TO_LOAD_MORE);
    }

}
