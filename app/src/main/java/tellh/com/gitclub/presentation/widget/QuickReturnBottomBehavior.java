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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import tellh.com.gitclub.R;
import tellh.com.gitclub.common.utils.Utils;
import tellh.com.gitclub.presentation.contract.bus.RxBusPostman;

public class QuickReturnBottomBehavior extends CoordinatorLayout.Behavior<View> {
    private int mTouchSlop;
    private boolean once;
    private int distanceToHide;
    private boolean animationTime;
    private static final int TRANSLATE = 0;
    private static final int TRANSLATE_OFFSET = 1;
    private static final int SCALE = 2;
    private static final int SCALE_OFFSET = 3;
    private int style;
    private int mOffset;
    private boolean mControlsVisible;

    public QuickReturnBottomBehavior(Context context, AttributeSet attrs) {
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
            int screenHeight = Utils.getScreenHeight();
            int top = child.getTop();
            distanceToHide = screenHeight - top;
            once = true;
        }
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    //处理滑动事件
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
//        Log.d("TAG","dxConsumed = [" + dxConsumed + "], dyConsumed = [" + dyConsumed + "], dxUnconsumed = [" + dxUnconsumed + "], dyUnconsumed = [" + dyUnconsumed + "]");
        if (animationTime)
            return;
        switch (style) {
            case TRANSLATE:
                if (dyConsumed > 0 && dyConsumed > mTouchSlop) {//手指上滑
                    hide(child);
                } else if (dyConsumed < 0 && dyConsumed < 2 * mTouchSlop) {//手指下滑
                    show(child);
                }
                break;
            case SCALE_OFFSET:
            case TRANSLATE_OFFSET:
                clipOffset();
                move(mOffset, child);
                if ((mOffset < distanceToHide && dyConsumed > 0) || (mOffset > 0 && dyConsumed < 0)) {
                    mOffset += dyConsumed;
                }
                break;
            case SCALE:
                if (dyConsumed > 0 && dyConsumed > mTouchSlop) {//手指上滑
                    toggleScale(child, false);
                } else if (dyConsumed < 0 && dyConsumed < 2 * mTouchSlop) {//手指下滑
                    toggleScale(child, true);
                }
                break;
            default:
                style = TRANSLATE;
                break;
        }
    }

    private void toggleScale(View child, final boolean visible) {
        float scaleX = !visible ? 0 : 1;
        float scaleY = !visible ? 0 : 1;
        child.animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .scaleX(scaleX)
                .scaleY(scaleY)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        animationTime = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (!visible) {
                            mOffset = distanceToHide;
                            mControlsVisible = false;
                        } else {
                            mOffset = 0;
                            mControlsVisible = true;
                        }
                        animationTime = false;
                    }
                }).start();
    }


    private void toggleScale(View child) {
        float scaleX = mControlsVisible ? 0 : 1;
        float scaleY = mControlsVisible ? 0 : 1;
        child.animate().setInterpolator(new DecelerateInterpolator())
                .setDuration(300)
                .scaleX(scaleX)
                .scaleY(scaleY)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        animationTime = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (mControlsVisible) {
                            mOffset = distanceToHide;
                            mControlsVisible = false;
                        } else {
                            mOffset = 0;
                            mControlsVisible = true;
                        }
                        animationTime = false;
                    }
                }).start();
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, View child, View target) {
        if (style == TRANSLATE_OFFSET) {
            if (mControlsVisible) {
                if (mOffset > mTouchSlop)
                    setInvisible(child);
                else
                    setVisible(child);
            } else {
                if ((distanceToHide - mOffset) > mTouchSlop)
                    setVisible(child);
            }
        } else if (style == SCALE_OFFSET) {
            if (mControlsVisible) {
                if (mOffset > mTouchSlop)
                    toggleScale(child);
                else
                    toggleScale(child, true);
            } else {
                if ((distanceToHide - mOffset) > mTouchSlop)
                    toggleScale(child);
            }
        }
    }

    private void setVisible(View view) {
        show(view);
        mOffset = 0;
        mControlsVisible = true;
    }

    private void setInvisible(View view) {
        hide(view);
        mOffset = distanceToHide;
        mControlsVisible = false;
    }

    private void clipOffset() {
        if (mOffset > distanceToHide) {
            mOffset = distanceToHide;
        } else if (mOffset < 0) {
            mOffset = 0;
        }
    }


    protected void show(View view) {
        RxBusPostman.postQuickReturnEvent(true);
        if (view != null) {
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(2))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            animationTime = false;
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            animationTime = true;
                        }
                    })
                    .start();
        }
    }

    protected void hide(View view) {
        RxBusPostman.postQuickReturnEvent(false);
        if (view != null) {
            view.animate()
                    .translationY(distanceToHide)
                    .setInterpolator(new AccelerateInterpolator(2))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            animationTime = false;
                        }

                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            animationTime = true;
                        }
                    })
                    .start();
        }
    }

    public void move(int distance, View view) {
        if (style == SCALE_OFFSET) {
            float offset = mOffset == 0 ? 1.0f : 1 - (float) mOffset / (float) distanceToHide;
            ViewCompat.setScaleY(view, offset);
            ViewCompat.setScaleX(view, offset);
        } else {
            ViewCompat.setTranslationY(view, distance);
        }
    }


}