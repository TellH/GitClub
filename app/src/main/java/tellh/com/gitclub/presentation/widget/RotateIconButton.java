package tellh.com.gitclub.presentation.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.RotateDrawable;
import android.util.AttributeSet;
import android.widget.Button;

import tellh.com.gitclub.R;

/**
 * Created by tlh on 2016/9/18 :)
 */
public class RotateIconButton extends Button {
    private RotateDrawable mDrawable;
    private ButtonToggleHelper btnToggleHelper;

    public RotateIconButton(Context context) {
        super(context);
    }

    public RotateIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDrawable = (RotateDrawable) getCompoundDrawables()[1];
        btnToggleHelper = ButtonToggleHelper.builder()
                .setBackgroundDrawable(R.drawable.selector_pink_right, R.drawable.selector_pink_right_checked)
                .setTextColor(R.color.white, R.color.gray_text)
                .setText(R.string.follow, R.string.unfollow)
                .build();
    }

    public RotateIconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setState(boolean checked) {
        toggleAnim(checked);
        btnToggleHelper.setState(this, checked);
    }

    protected void toggleAnim(final boolean toggle) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 10000);
        animator.setTarget(mDrawable);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                if (!toggle)
                    value = 10000 - value;
                mDrawable.setLevel(value);
            }
        });
        animator.setDuration(100);
        animator.start();
    }

    public boolean getState() {
        return btnToggleHelper.getState(this);
    }

    public boolean toggle() {
        boolean toggle = btnToggleHelper.toggle(this);
        toggleAnim(toggle);
        return toggle;
    }
}
