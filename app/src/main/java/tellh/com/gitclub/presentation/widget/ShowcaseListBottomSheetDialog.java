package tellh.com.gitclub.presentation.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.wrapper.ImageLoader;
import tellh.com.gitclub.model.entity.ShowCaseInfo;
import tellh.com.gitclub.presentation.presenter.IRepoListPresenter;
import tellh.com.gitclub.presentation.view.adapter.HeaderAndFooterAdapterWrapper;
import tellh.com.gitclub.presentation.view.adapter.RecyclerViewHolder;
import tellh.com.gitclub.presentation.view.adapter.RepoListAdapter;

/**
 * Created by tlh on 2016/9/8 :)
 */
public class ShowcaseListBottomSheetDialog extends BottomSheetDialog {
    private IRepoListPresenter presenter;
    private ShowCaseInfo showcase;
    private HeaderAndFooterAdapterWrapper adapterWrapper;
    private BottomSheetBehavior<View> bottomSheetBehavior;
    private RecyclerView recyclerView;

    public ShowcaseListBottomSheetDialog(@NonNull Context context, IRepoListPresenter presenter) {
        super(context);
        this.presenter = presenter;
        init();
    }

    private void init() {
        //init recycler view
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_list, null, false);
        setContentView(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterWrapper = new HeaderAndFooterAdapterWrapper(new RepoListAdapter(getContext(), null, presenter)) {
            @Override
            protected void onBindHeader(RecyclerViewHolder holder, int position) {
                ImageView ivHeader = holder.getImageView(R.id.iv_header);
                ImageLoader.loadAndCrop(showcase.getImage(), ivHeader);
                holder.setText(R.id.tv_desc, showcase.getDescription())
                        .setText(R.id.tv_name, showcase.getName());
            }
        };
        adapterWrapper.addHeader(R.layout.item_showcase);
        recyclerView.setAdapter(adapterWrapper);

        //set bottom sheet behaviour
        View sheetView = getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        assert sheetView != null;
        bottomSheetBehavior = BottomSheetBehavior.from(sheetView);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    public void refreshAndShow(ShowCaseInfo showCaseInfo) {
        this.showcase = showCaseInfo;
        adapterWrapper.refresh(showCaseInfo.getRepositories());
        show();
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            recyclerView.scrollToPosition(0);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return;
        }
        super.onBackPressed();
    }
}
