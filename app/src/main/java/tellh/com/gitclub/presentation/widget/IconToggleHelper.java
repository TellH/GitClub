package tellh.com.gitclub.presentation.widget;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import tellh.com.gitclub.model.entity.RepositoryInfo;

/**
 * Created by tlh on 2016/9/2 :)
 */
public class IconToggleHelper implements ToggleHelper<ImageView> {
    int drawable_default;
    int drawable_pressed;

    public IconToggleHelper(@DrawableRes int drawable_default, @DrawableRes int drawable_pressed) {
        this.drawable_default = drawable_default;
        this.drawable_pressed = drawable_pressed;
    }

    public IconToggleHelper toggleStarCount(TextView tv, RepositoryInfo repositoryInfo, @NonNull ImageView me) {
        boolean state;
        try {
            state = (boolean) me.getTag();
        } catch (Exception e) {
            me.setTag(false);
            state = false;
        }
        int increase = state ? -1 : 1;
        repositoryInfo.stars += increase;
        tv.setText(String.valueOf(repositoryInfo.stars));
        return this;
    }

    @Override
    public boolean toggle(@NonNull ImageView me) {
        boolean state;
        try {
            state = (boolean) me.getTag();
        } catch (Exception e) {
            me.setTag(false);
            state = false;
        }
        if (drawable_default != 0 && drawable_pressed != 0) {
            int resId = state ? drawable_default : drawable_pressed;
            me.setTag(!state);
            me.setBackgroundResource(resId);
        }
        return !state;
    }

    @Override
    public void setState(@NonNull ImageView me, boolean state) {
        if (drawable_default != 0 && drawable_pressed != 0) {
            int resId = !state ? drawable_default : drawable_pressed;
            me.setTag(state);
            me.setBackgroundResource(resId);
        }
    }

    @Override
    public void setState(ImageView me) {
        boolean state;
        try {
            state = (boolean) me.getTag();
        } catch (Exception e) {
            me.setTag(false);
            state = false;
        }
        setState(me, state);
    }
}
