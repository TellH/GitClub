package tellh.com.gitclub.presentation.view.fragment.search;

import android.support.v7.widget.RecyclerView;

import com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper;
import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;
import com.tellh.nolistadapter.viewbinder.utils.EasyEmptyRecyclerViewBinder;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.ErrorViewBinder;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.LoadMoreFooterViewBinder;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.UserListItemViewBinder;
import tellh.com.gitclub.presentation.view.fragment.ListFragment;

import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.LOADING;
import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.OnReachFooterListener;
import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.PULL_TO_LOAD_MORE;
import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.UpdateType;
import static tellh.com.gitclub.presentation.contract.SearchContract.OnGetUserListener;
import static tellh.com.gitclub.presentation.contract.SearchContract.OnListFragmentInteractListener;
import static tellh.com.gitclub.presentation.contract.SearchContract.USER;

public class SearchUserFragment extends ListFragment
        implements OnGetUserListener, OnReachFooterListener {

    private OnListFragmentInteractListener mListener;
    private FooterLoadMoreAdapterWrapper loadMoreWrapper;

    public static SearchUserFragment newInstance() {
        return new SearchUserFragment();
    }

    @Override
    protected RecyclerView.Adapter getListAdapter() {
        assert mListener != null;
        loadMoreWrapper = (FooterLoadMoreAdapterWrapper) RecyclerViewAdapter.builder()
                .addItemType(new UserListItemViewBinder(mListener.getPresenter()))
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
    public void onToLoadMore(int curPage) {
        mListener.onFetchPage(USER, curPage + 1);
    }

    @Override
    public void onGetUser(int total_count, List<UserEntity> items, @UpdateType int updateType) {
        loadMoreWrapper.OnGetData(items, updateType);
    }

    void setListFragmentInteractListener(OnListFragmentInteractListener listener) {
        mListener = listener;
    }

    @Override
    public void onRefresh() {
        mListener.onFetchPage(USER, 1);
        loadMoreWrapper.hideErrorView(recyclerView);
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (loadMoreWrapper.getFooterStatus() == LOADING)
            loadMoreWrapper.setFooterStatus(PULL_TO_LOAD_MORE);
    }
}
