package tellh.com.gitclub.presentation.view.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.model.entity.ShowCase;
import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;

/**
 * Created by tlh on 2016/9/7 :)
 */
public class ShowcaseListAdapter extends BaseRecyclerAdapter<ShowCase> {
    public ShowcaseListAdapter(Context ctx, List<ShowCase> list) {
        super(ctx, list);
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        return R.layout.item_showcase;
    }

    @Override
    protected void bindData(RecyclerViewHolder holder, int position, final ShowCase item) {
        ImageView ivHeader = holder.getImageView(R.id.iv_header);
        ImageLoader.loadAndCrop(item.getImage_url(), ivHeader);
        holder.setText(R.id.tv_name, item.getName())
                .setText(R.id.tv_desc, item.getDescription());
        holder.getView(R.id.item_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2016/9/7 go to showcase detail activity.
                RxBusPostman.postGetShowcaseDetailEvent(item);
            }
        });
    }
}
