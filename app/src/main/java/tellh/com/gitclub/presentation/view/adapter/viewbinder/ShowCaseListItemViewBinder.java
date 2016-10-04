package tellh.com.gitclub.presentation.view.adapter.viewbinder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tellh.nolistadapter.IListAdapter;
import com.tellh.nolistadapter.viewbinder.base.RecyclerViewBinder;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.model.entity.ShowCase;
import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;

/**
 * Created by tlh on 2016/10/4 :)
 */

public class ShowCaseListItemViewBinder extends RecyclerViewBinder<ShowCase, ShowCaseListItemViewBinder.ViewHolder> {
    @Override
    public ViewHolder provideViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(IListAdapter iListAdapter, ViewHolder holder, int i, final ShowCase showCase) {
        ImageLoader.loadAndCrop(showCase.getImage_url(), holder.ivHeader);
        holder.tvName.setText(showCase.getName());
        holder.tvDesc.setText(showCase.getDescription());
        holder.itemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxBusPostman.postGetShowcaseDetailEvent(showCase);
            }
        });
    }

    @Override
    public int getItemLayoutId(IListAdapter iListAdapter) {
        return R.layout.item_showcase;
    }

    class ViewHolder extends RecyclerViewBinder.ViewHolder {
        private ImageView ivHeader;
        private TextView tvName;
        private TextView tvDesc;
        private CardView itemContainer;

        ViewHolder(View rootView) {
            super(rootView);
            this.ivHeader = (ImageView) rootView.findViewById(R.id.iv_header);
            this.tvName = (TextView) rootView.findViewById(R.id.tv_name);
            this.tvDesc = (TextView) rootView.findViewById(R.id.tv_desc);
            this.itemContainer = (CardView) rootView.findViewById(R.id.item_container);
        }

    }
}
