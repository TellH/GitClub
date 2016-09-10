package tellh.com.gitclub.presentation.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import tellh.com.gitclub.R;
import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;

public class QuickReturnTopBehavior extends CoordinatorLayout.Behavior<View> {
    private static final int DEFAULT = 0;
    private static final int OFFSET = 1;
    private final int mTouchSlop;
    private boolean once;
    private int distanceToHide;
    private boolean animationTime;
    private int mOffset;
    private boolean mControlsVisible;
    private int style;

    public QuickReturnTopBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickReturnBehavior);
        style = typedArray.getInt(R.styleable.QuickReturnBehavior_quick_return_style, 0);
        typedArray.recycle();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        if (!once) {
            distanceToHide = child.getBottom();
            once = true;
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    //handle sliding event
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (animationTime)
            return;
        switch (style) {
            case DEFAULT:
                if (dyConsumed > 0 && dyConsumed > mTouchSlop) {//手指上滑
                    hide(child);
                } else if (dyConsumed < 0 && dyConsumed < 2 * mTouchSlop) {//手指下滑
                    show(child);
                }
                break;
            case OFFSET:
                clipOffset();
                move(child, mOffset);
                if ((mOffset < distanceToHide && dyConsumed > 0) || (mOffset > 0 && dyConsumed < 0)) {
                    mOffset += dyConsumed;
                }
                break;

            default:
                style = DEFAULT;
                break;
        }
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        if (style == OFFSET) {
            if (mControlsVisible) {
                if (mOffset > mTouchSlop)
                    setInvisible(child);
                else
                    setVisible(child);
            } else {
                if ((distanceToHide - mOffset) > mTouchSlop)
                    setVisible(child);
            }
        }
    }


    private void clipOffset() {
        if (mOffset > distanceToHide) {
            mOffset = distanceToHide;
        } else if (mOffset < 0) {
            mOffset = 0;
        }
    }

    private void setVisible(View view) {
        if (mOffset > 0) {
            show(view);
            mOffset = 0;
        }
        mControlsVisible = true;
    }

    private void setInvisible(View view) {
        if (mOffset < distanceToHide) {
            hide(view);
            mOffset = distanceToHide;
        }
        mControlsVisible = false;
    }


    private void show(View view) {
        RxBusPostman.postQuickReturnEvent(true);
        if (view != null) {
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(2))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationTime = false;
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            animationTime = true;
                        }
                    })
                    .start();
        }
    }

    private void hide(View view) {
        RxBusPostman.postQuickReturnEvent(false);
        if (view != null) {
            view.animate()
                    .translationY(-distanceToHide)
                    .setInterpolator(new DecelerateInterpolator(2))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            animationTime = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationTime = false;
                        }
                    })
                    .start();
        }

    }

    private void move(View View, int distance) {
        View.setTranslationY(-distance);
    }
}