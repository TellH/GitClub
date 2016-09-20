package tellh.com.gitclub.presentation.view.fragment.explore;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.model.entity.ShowCase;
import tellh.com.gitclub.presentation.contract.ExploreContract;
import tellh.com.gitclub.presentation.view.adapter.ShowcaseListAdapter;
import tellh.com.gitclub.presentation.view.fragment.ListFragment;

import static tellh.com.gitclub.presentation.contract.ExploreContract.OnListFragmentInteractListener;
import static tellh.com.gitclub.presentation.contract.ExploreContract.SHOWCASES;

/**
 * Created by tlh on 2016/9/5 :)
 */
public class ShowCaseListFragment extends ListFragment implements ExploreContract.onGetShowcasesListener {
    private OnListFragmentInteractListener mListener;
    private ShowcaseListAdapter adapter;

    public static ShowCaseListFragment newInstance() {
        return new ShowCaseListFragment();
    }

    @Override
    protected RecyclerView.Adapter getListAdapter() {
        adapter = new ShowcaseListAdapter(getContext(), null);
        return adapter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_items_list;
    }

    @Override
    public void onRefresh() {
        mListener.onFetchData(SHOWCASES);
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
    public void onGet(List<ShowCase> showCases) {
        adapter.refresh(showCases);
        hideLoading();
    }

    @NonNull
    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
    }
}
