package tellh.com.gitclub.presentation.widget;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Button;

/**
 * Created by tlh on 2016/9/2 :)
 */
public class ButtonToggleHelper implements ToggleHelper<Button> {

    private int color_default;
    private int color_pressed;

    private String text_default;
    private String text_pressed;

    private int txt_default;
    private int txt_pressed;

    private int txt_color_default;
    private int txt_color_pressed;
    private int drawable_Default;
    private int drawable_Pressed;
    private int icon_left_default;
    private int icon_left_pressed;

    private ButtonToggleHelper() {
    }

    /**
     * @return button state after toggle.
     */
    @Override
    public boolean toggle(@NonNull Button button) {
        boolean state;
        try {
            state = (boolean) button.getTag();
        } catch (Exception e) {
            button.setTag(false);
            state = false;
        }
        setState(button, !state);
        return !state;
    }

    @Override
    public void setState(@NonNull Button button, boolean state) {
        if (drawable_Default != 0 && drawable_Pressed != 0) {
            int drawableRes = !state ? drawable_Default : drawable_Pressed;
            button.setBackgroundResource(drawableRes);
        }
        if (icon_left_default != 0 && icon_left_pressed != 0) {
            int drawableRes = !state ? icon_left_default : icon_left_pressed;
            button.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(button.getContext(), drawableRes),
                    null, null, null);
        }

        if (color_pressed != 0 && color_default != 0) {
            int color = !state ? color_default : color_pressed;
            button.setBackgroundColor(ContextCompat.getColor(button.getContext(), color));
        }

        if (!TextUtils.isEmpty(text_default) && !TextUtils.isEmpty(text_pressed)) {
            String text = state ? text_pressed : text_default;
            button.setText(text);
        } else if (txt_default != 0 && txt_pressed != 0) {
            int textResId = state ? txt_pressed : txt_default;
            button.setText(textResId);
        }
        if (txt_color_default != 0 && txt_color_pressed != 0) {
            int color = !state ? txt_color_default : txt_color_pressed;
            button.setTextColor(ContextCompat.getColor(button.getContext(), color));
        }
        button.setTag(state);
    }

    @Override
    public void setState(Button button) {
        boolean state;
        try {
            state = (boolean) button.getTag();
        } catch (Exception e) {
            button.setTag(false);
            state = false;
        }
        setState(button, state);
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean getState(RotateIconButton rotateIconButton) {
        return (boolean) rotateIconButton.getTag();
    }

    public static class Builder {
        private int color_default;
        private int color_pressed;
        private String text_default;
        private String text_pressed;
        private int txt_default;
        private int txt_pressed;
        private int txt_color_pressed;
        private int txt_color_default;
        private int drawable_Pressed;
        private int drawable_Default;
        private int icon_left_default;
        private int icon_left_pressed;

        private Builder() {
        }

        public Builder setBackgroundDrawable(@DrawableRes int drawableDefault, @DrawableRes int drawablePressed) {
            drawable_Default = drawableDefault;
            drawable_Pressed = drawablePressed;
            return this;
        }

        public Builder setBackgroundColor(@ColorRes int colorDefault, @ColorRes int colorPressed) {
            color_default = colorDefault;
            color_pressed = colorPressed;
            return this;
        }

        public Builder setText(String textDefault, String textPressed) {
            text_default = textDefault;
            text_pressed = textPressed;
            return this;
        }

        public Builder setText(@StringRes int textDefault, @StringRes int textPressed) {
            txt_default = textDefault;
            txt_pressed = textPressed;
            return this;
        }

        public Builder setTextColor(@ColorRes int colorDefault, @ColorRes int colorPressed) {
            txt_color_default = colorDefault;
            txt_color_pressed = colorPressed;
            return this;
        }

        public Builder setDrawableLeft(@DrawableRes int drawable_Default, @DrawableRes int drawable_Pressed) {
            icon_left_default = drawable_Default;
            icon_left_pressed = drawable_Pressed;
            return this;
        }

        public ButtonToggleHelper build() {
            ButtonToggleHelper helper = new ButtonToggleHelper();
            helper.color_default = color_default;
            helper.color_pressed = color_pressed;
            helper.text_default = text_default;
            helper.text_pressed = text_pressed;
            helper.txt_default = txt_default;
            helper.txt_pressed = txt_pressed;
            helper.txt_color_default = txt_color_default;
            helper.txt_color_pressed = txt_color_pressed;
            helper.drawable_Default = drawable_Default;
            helper.drawable_Pressed = drawable_Pressed;
            helper.icon_left_default = icon_left_default;
            helper.icon_left_pressed = icon_left_pressed;
            return helper;
        }

    }
}
