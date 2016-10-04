package tellh.com.gitclub.presentation.view.adapter.viewbinder;

import android.view.View;

import com.tellh.nolistadapter.IListAdapter;
import com.tellh.nolistadapter.viewbinder.base.RecyclerViewBinder;
import com.tellh.nolistadapter.viewbinder.sub.ErrorRecyclerViewBinder;

import tellh.com.gitclub.R;

/**
 * Created by tlh on 2016/10/4 :)
 */

public class ErrorViewBinder extends ErrorRecyclerViewBinder<RecyclerViewBinder.ViewHolder> {
    private OnReLoadCallback callback;

    public ErrorViewBinder(OnReLoadCallback callback) {
        this.callback = callback;
    }

    @Override
    protected void bindErrorView(IListAdapter iListAdapter, ViewHolder holder, int i) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callback != null)
                    callback.reload();
            }
        });
    }

    @Override
    public ViewHolder provideViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getItemLayoutId(IListAdapter iListAdapter) {
        return R.layout.error_view;
    }

    public interface OnReLoadCallback {
        void reload();
    }
}
