package tellh.com.gitclub.presentation.widget;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Button;

/**
 * Created by tlh on 2016/9/2 :)
 */
public class ButtonToggleHelper implements ToggleHelper<Button> {

    int color_default;
    int color_pressed;

    String text_default;
    String text_pressed;

    int txt_default;
    int txt_pressed;

    int txt_color_default;
    int txt_color_pressed;

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
//        if (color_pressed != 0 && color_default != 0) {
//            int color = state ? color_default : color_pressed;
//            button.setBackgroundColor(ContextCompat.getColor(button.getContext(), color));
//        }
//        if (!TextUtils.isEmpty(text_default) && !TextUtils.isEmpty(text_pressed)) {
//            int textResId = state ? txt_default : txt_pressed;
//            button.setText(textResId);
//        } else if (txt_default != 0 && txt_pressed != 0) {
//            String text = !state ? text_pressed : text_default;
//            button.setText(text);
//        }
//        if (txt_color_default != 0 && txt_color_pressed != 0) {
//            int color = state ? txt_color_default : txt_color_pressed;
//            button.setTextColor(ContextCompat.getColor(button.getContext(), color));
//        }
        setState(button, !state);
        return !state;
    }

    @Override
    public void setState(@NonNull Button button, boolean state) {
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

    public static class Builder {
        private int color_default;
        private int color_pressed;
        private String text_default;
        private String text_pressed;
        private int txt_default;
        private int txt_pressed;
        private int txt_color_pressed;
        private int txt_color_default;

        private Builder() {
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
            return helper;
        }

    }
}
