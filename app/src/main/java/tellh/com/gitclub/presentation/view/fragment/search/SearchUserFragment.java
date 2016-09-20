package tellh.com.gitclub.presentation.view.fragment.search;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.model.entity.UserEntity;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper;
import tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.UpdateType;
import tellh.com.gitclub.presentation.view.adapter.UserListAdapter;
import tellh.com.gitclub.presentation.view.fragment.ListFragment;

import static tellh.com.gitclub.presentation.contract.SearchContract.OnGetUserListener;
import static tellh.com.gitclub.presentation.contract.SearchContract.OnListFragmentInteractListener;
import static tellh.com.gitclub.presentation.contract.SearchContract.USER;
import static tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.LOADING;
import static tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.OnReachFooterListener;
import static tellh.com.gitclub.presentation.view.adapter.FooterLoadMoreAdapterWrapper.PULL_TO_LOAD_MORE;

public class SearchUserFragment extends ListFragment
        implements OnGetUserListener, OnReachFooterListener {

    private OnListFragmentInteractListener mListener;
    private FooterLoadMoreAdapterWrapper loadMoreWrapper;

    public static SearchUserFragment newInstance() {
        return new SearchUserFragment();
    }

    @Override
    protected RecyclerView.Adapter getListAdapter() {
        loadMoreWrapper = new FooterLoadMoreAdapterWrapper(new UserListAdapter(context, null, mListener.getPresenter()));
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
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (loadMoreWrapper.getFooterStatus() == LOADING)
            loadMoreWrapper.setFooterStatus(PULL_TO_LOAD_MORE);
    }
}
