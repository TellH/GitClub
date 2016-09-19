package tellh.com.gitclub.presentation.widget;

import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;

import tellh.com.gitclub.R;

/**
 * Created by tlh on 2016/9/10 :)
 */
public class ErrorViewHelper {
    private FrameLayout flErrorView;
    private ViewStub vsError;

    public ErrorViewHelper(ViewStub vsError) {
        this.vsError = vsError;
    }

    public void showErrorView(final View mainView, final OnReLoadCallback callback) {
        mainView.setVisibility(View.INVISIBLE);
        if (flErrorView != null) {
            flErrorView.setVisibility(View.VISIBLE);
            return;
        }
        View errorView = vsError.inflate();
        flErrorView = (FrameLayout) errorView.findViewById(R.id.layout_error_view);
        flErrorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideErrorView(mainView);
                if (callback != null)
                    callback.reload();
            }
        });
    }

    public void hideErrorView(final View mainView) {
        flErrorView.setVisibility(View.INVISIBLE);
        mainView.setVisibility(View.VISIBLE);
    }

    public boolean isShowing() {
        return flErrorView != null && flErrorView.getVisibility() == View.VISIBLE;
    }

    public interface OnReLoadCallback {
        void reload();
    }
}
