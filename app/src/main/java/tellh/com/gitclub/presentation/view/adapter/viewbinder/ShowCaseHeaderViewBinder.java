package tellh.com.gitclub.presentation.view.adapter.viewbinder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tellh.nolistadapter.IListAdapter;
import com.tellh.nolistadapter.viewbinder.base.RecyclerViewBinder;
import com.tellh.nolistadapter.viewbinder.sub.HeaderRecyclerViewBinder;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.model.entity.ShowCaseInfo;

/**
 * Created by tlh on 2016/10/4 :)
 */

public class ShowCaseHeaderViewBinder extends HeaderRecyclerViewBinder<ShowCaseHeaderViewBinder.ViewHolder> {
    private ShowCaseInfo showcase;

    public void setShowcase(ShowCaseInfo showcase) {
        this.showcase = showcase;
    }

    @Override
    protected void bindHeader(IListAdapter iListAdapter, ViewHolder holder, int i) {
        ImageLoader.loadAndCrop(showcase.getImage(), holder.ivHeader);
        holder.tvDesc.setText(showcase.getDescription());
        holder.tvName.setText(showcase.getName());
    }

    @Override
    public ViewHolder provideViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getItemLayoutId(IListAdapter iListAdapter) {
        return R.layout.item_showcase;
    }

    class ViewHolder extends RecyclerViewBinder.ViewHolder {
        private ImageView ivHeader;
        private TextView tvName;
        private TextView tvDesc;

        ViewHolder(View rootView) {
            super(rootView);
            this.ivHeader = (ImageView) rootView.findViewById(R.id.iv_header);
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name);
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
        }

    }
}
