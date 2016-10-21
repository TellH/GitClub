package tellh.com.gitclub.presentation.view.adapter.viewbinder;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tellh.nolistadapter.IListAdapter;
import com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper;
import com.tellh.nolistadapter.viewbinder.base.RecyclerViewBinder;
import com.tellh.nolistadapter.viewbinder.sub.FooterRecyclerViewBinder;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.config.Constant;
import tellh.com.gitclub.common.utils.Utils;

import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.LOADING;
import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.NO_MORE;
import static com.tellh.nolistadapter.adapter.FooterLoadMoreAdapterWrapper.PULL_TO_LOAD_MORE;

/**
 * Created by tlh on 2016/10/4 :)
 */

public class LoadMoreFooterViewBinder extends FooterRecyclerViewBinder<LoadMoreFooterViewBinder.ViewHolder> {
    private String toLoadText = Utils.getString(R.string.pull_to_load_more);
    private String noMoreText = Utils.getString(R.string.no_more);
    private String loadingText = Utils.getString(R.string.loading);
    private int PER_PAGE = Constant.PER_PAGE;

    public LoadMoreFooterViewBinder(int PER_PAGE) {
        this.PER_PAGE = PER_PAGE;
    }

    public LoadMoreFooterViewBinder() {
    }

    @Override
    protected void bindFooter(IListAdapter adapter, ViewHolder holder, int position) {
        FooterLoadMoreAdapterWrapper adapterWrapper = (FooterLoadMoreAdapterWrapper) adapter;
        int size = adapter.getDisplayList().size();
        if (size < PER_PAGE) {
            holder.tvFooter.setText(noMoreText);
            holder.progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        switch (adapterWrapper.getFooterStatus()) {
            case PULL_TO_LOAD_MORE:
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.tvFooter.setText(toLoadText);
                break;
            case LOADING:
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.tvFooter.setText(loadingText);
                break;
            case NO_MORE:
                holder.tvFooter.setText(noMoreText);
                holder.progressBar.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getItemLayoutId(IListAdapter adapter) {
        return R.layout.footer_load_more;
    }

    public static class ViewHolder extends RecyclerViewBinder.ViewHolder {
        TextView tvFooter;
        ProgressBar progressBar;

        public ViewHolder(View rootView) {
            super(rootView);
            this.tvFooter = (TextView) rootView.findViewById(R.id.tv_footer);
            this.progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        }

    }

}
