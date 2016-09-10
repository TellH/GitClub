package tellh.com.gitclub.presentation.widget;

import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import tellh.com.gitclub.common.utils.Utils;

/**
 * Created by tlh on 2016/9/6 :)
 */
public class FabAnimationHelper {
    public static void show(View view) {
        if (view != null) {
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(2))
                    .start();
        }
    }

    public static void show(View view, AnimatorListenerAdapter listener) {
        if (view != null) {
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(2))
                    .setListener(listener)
                    .start();
        }
    }

    public static void hide(View view) {
        if (view != null) {
            int screenHeight = Utils.getScreenHeight();
            int top = view.getTop();
            int distanceToHide = screenHeight - top;
            view.animate()
                    .translationY(distanceToHide)
                    .setInterpolator(new AccelerateInterpolator(2))
                    .start();
        }
    }

    public static void hide(View view, AnimatorListenerAdapter listener) {
        if (view != null) {
            int screenHeight = Utils.getScreenHeight();
            int top = view.getTop();
            int distanceToHide = screenHeight - top;
            view.animate()
                    .translationY(distanceToHide)
                    .setInterpolator(new AccelerateInterpolator(2))
                    .setListener(listener)
                    .start();
        }
    }

}
