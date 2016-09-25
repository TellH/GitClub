package tellh.com.gitclub.presentation.view.adapter;

import android.support.annotation.IntDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.Constant;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;

public class FooterLoadMoreAdapterWrapper extends HeaderAndFooterAdapterWrapper {
    private int curPage;
    //UpdateType
    public static final int REFRESH = 401;
    public static final int LOAD_MORE = 140;

    //FooterState
    public static final int PULL_TO_LOAD_MORE = 501;
    public static final int LOADING = 323;
    public static final int NO_MORE = 313;

    @IntDef({REFRESH, LOAD_MORE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UpdateType {
    }

    @IntDef({PULL_TO_LOAD_MORE, LOADING, NO_MORE})
    @Retention(RetentionPolicy.SOURCE)
    @interface FooterState {
    }

    @FooterState
    private int mFooterStatus = PULL_TO_LOAD_MORE;
    private String toLoadText = Utils.getString(R.string.pull_to_load_more);
    private String noMoreText = Utils.getString(R.string.no_more);
    private String loadingText = Utils.getString(R.string.loading);

    @FooterState
    public int getFooterStatus() {
        return mFooterStatus;
    }

    public FooterLoadMoreAdapterWrapper(BaseRecyclerAdapter adapter) {
        super(adapter);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void onBindFooter(RecyclerViewHolder holder, int position) {
        ProgressBar progressBar = (ProgressBar) holder.getView(R.id.progressBar);
        if (mItems.size() == 0) {
            progressBar.setVisibility(View.INVISIBLE);
            holder.setText(R.id.tv_footer, Utils.getString(R.string.empty));
            return;
        }
        if (mItems.size() < getPerPageSize()) {
            holder.setText(R.id.tv_footer, noMoreText);
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        switch (mFooterStatus) {
            case PULL_TO_LOAD_MORE:
                progressBar.setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_footer, toLoadText);
                break;
            case LOADING:
                progressBar.setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_footer, loadingText);
                break;
            case NO_MORE:
                holder.setText(R.id.tv_footer, noMoreText);
                progressBar.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void setOnReachFooterListener(RecyclerView recyclerView, final OnReachFooterListener listener) {
        if (recyclerView == null || listener == null)
            return;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isReachBottom(recyclerView, newState) || mItems.size() == 0)
                    return;
                RxBusPostman.postQuickReturnEvent(true);
                if (mFooterStatus != LOADING
                        && mFooterStatus != NO_MORE) {
                    setFooterStatus(LOADING);
                    listener.onToLoadMore(curPage);
                }
            }
        });
    }


    public void setFooterStatus(@FooterState int status) {
        mFooterStatus = status;
        notifyDataSetChanged();
    }

    private boolean isReachBottom(RecyclerView recyclerView, int newState) {
        return recyclerView != null && newState == RecyclerView.SCROLL_STATE_IDLE &&
                ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1;
    }

    public interface OnReachFooterListener {
        void onToLoadMore(int curPage);
    }


    public void setToLoadText(String toLoadText) {
        this.toLoadText = toLoadText;
    }

    public void setNoMoreText(String noMoreText) {
        this.noMoreText = noMoreText;
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    public void OnGetData(List data, @UpdateType int updateType) {
        if (updateType == REFRESH) {
            refresh(data);
            curPage = 1;
            setFooterStatus(PULL_TO_LOAD_MORE);
        } else {
            if (data.isEmpty()) {
                setFooterStatus(NO_MORE);
                return;
            }
            addAll(data);
            curPage++;
            setFooterStatus(PULL_TO_LOAD_MORE);
        }
    }

    private int getPerPageSize() {
        return Constant.PER_PAGE;
    }

    public int getCurPage() {
        return curPage;
    }
}
