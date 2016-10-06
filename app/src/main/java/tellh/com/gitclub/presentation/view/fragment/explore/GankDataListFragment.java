package tellh.com.gitclub.presentation.view.fragment.explore;

import android.support.v7.widget.RecyclerView;

import com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper;
import com.tellh.nolistadapter.adapter.RecyclerViewAdapter;
import com.tellh.nolistadapter.viewbinder.utils.EasyEmptyRecyclerViewBinder;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.model.entity.RepositoryInfo;
import tellh.com.gitclub.presentation.contract.ExploreContract;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.ErrorViewBinder;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.LoadMoreFooterViewBinder;
import tellh.com.gitclub.presentation.view.adapter.viewbinder.RepoListItemViewBinder;
import tellh.com.gitclub.presentation.view.fragment.ListFragment;

import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.OnReachFooterListener;
import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.PULL_TO_LOAD_MORE;
import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.REFRESH;
import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.UpdateType;
import static tellh.com.gitclub.presentation.contract.ExploreContract.OnListFragmentInteractListener;

/**
 * Created by tlh on 2016/9/5 :)
 */
public class GankDataListFragment extends ListFragment implements ExploreContract.OnGetGankDataListener,
        OnReachFooterListener {
    private OnListFragmentInteractListener mListener;
    private FooterLoadMoreAdapterWrapper adapter;

    public static GankDataListFragment newInstance() {
        return new GankDataListFragment();
    }

    @Override
    protected RecyclerView.Adapter getListAdapter() {
        adapter = (FooterLoadMoreAdapterWrapper) RecyclerViewAdapter.builder()
                .addItemType(new RepoListItemViewBinder(mListener.getPresenter()))
                .setLoadMoreFooter(new LoadMoreFooterViewBinder(), recyclerView, this)
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
        mListener.onFetchData(ExploreContract.GANK_IO, 1);
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
    public void onGet(List<RepositoryInfo> repositoryList, @UpdateType int updateType) {
        adapter.OnGetData(repositoryList, updateType);
        hideLoading();
    }

    @Override
    public void onToLoadMore(int curPage) {
        mListener.onFetchData(ExploreContract.GANK_IO, curPage + 1);
    }

    public void showOnError(String msg, @UpdateType int updateType) {
        if (updateType == REFRESH)
            showErrorView();
        else
            adapter.setFooterStatus(PULL_TO_LOAD_MORE);

        if (updateType == REFRESH && !msg.equals(Utils.getString(R.string.reqest_flying))) {
            adapter.showErrorView(recyclerView);
        }
    }
}
