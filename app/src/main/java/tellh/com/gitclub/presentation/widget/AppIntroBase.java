package tellh.com.gitclub.presentation.widget;

import android.animation.ArgbEvaluator;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.github.paolorotolo.appintro.AppIntroViewPager;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.ISlidePolicy;
import com.github.paolorotolo.appintro.ISlideSelectionListener;
import com.github.paolorotolo.appintro.IndicatorController;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import tellh.com.gitclub.R;

public abstract class AppIntroBase extends AppCompatActivity implements AppIntroViewPager.OnNextPageRequestedListener {

    public final static int DEFAULT_COLOR = 1;

    private static final String TAG = "AppIntroBase";

    private static final int DEFAULT_SCROLL_DURATION_FACTOR = 1;
    private static final int PERMISSIONS_REQUEST_ALL_PERMISSIONS = 1;

    private static final String INSTANCE_DATA_IMMERSIVE_MODE_ENABLED = "com.github.paolorotolo.appintro_immersive_mode_enabled";
    private static final String INSTANCE_DATA_IMMERSIVE_MODE_STICKY = "com.github.paolorotolo.appintro_immersive_mode_sticky";
    private static final String INSTANCE_DATA_COLOR_TRANSITIONS_ENABLED = "com.github.paolorotolo.appintro_color_transitions_enabled";

    protected PagerAdapter mPagerAdapter;
    protected AppIntroViewPager pager;
    protected Vibrator mVibrator;
    protected IndicatorController mController;
    private GestureDetectorCompat gestureDetector;

    protected final List<Fragment> fragments = new Vector<>();
    protected int slidesNumber;

    protected int vibrateIntensity = 20;
    protected int selectedIndicatorColor = DEFAULT_COLOR;
    protected int unselectedIndicatorColor = DEFAULT_COLOR;
    protected View loginButton;
    protected View skipButton;
    protected int savedCurrentItem;

    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    protected boolean isVibrateOn = false;
    protected boolean baseProgressButtonEnabled = true;
    protected boolean progressButtonEnabled = true;
    private boolean isGoBackLockEnabled = false;
    private boolean isImmersiveModeEnabled = false;
    private boolean isImmersiveModeSticky = false;
    private boolean areColorTransitionsEnabled = false;
    protected boolean skipButtonEnabled = true;

    private int currentlySelectedItem = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());

        gestureDetector = new GestureDetectorCompat(this, new WindowGestureListener());

        loginButton = findViewById(R.id.btn_login);
        skipButton = findViewById(R.id.skip);

        mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        pager = (AppIntroViewPager) findViewById(R.id.view_pager);
        pager.setOffscreenPageLimit(4);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                if (isVibrateOn) {
                    mVibrator.vibrate(vibrateIntensity);
                }

                Fragment currentFragment = mPagerAdapter.getItem(pager.getCurrentItem());

                boolean isSlideChangingAllowed = handleBeforeSlideChanged();

                if (isSlideChangingAllowed) {
                    handleSlideChanged(currentFragment, null);
                    onLoginPressed(currentFragment);
                } else {
                    handleIllegalSlideChangeAttempt();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(@NonNull View v) {
                if (isVibrateOn) {
                    mVibrator.vibrate(vibrateIntensity);
                }
                onSkipPressed(mPagerAdapter.getItem(pager.getCurrentItem()));
            }
        });

        pager.setAdapter(this.mPagerAdapter);
        pager.addOnPageChangeListener(new PagerOnPageChangeListener());
        pager.setOnNextPageRequestedListener(this);

        setScrollDurationFactor(DEFAULT_SCROLL_DURATION_FACTOR);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Call deprecated init method only if no fragments have been added trough onCreate() or onStart()
        if (fragments.size() == 0) {
            init(null);
        }

        // required for triggering onPageSelected and onSlideChanged for first page
        pager.setCurrentItem(savedCurrentItem);
        pager.post(new Runnable() {
            @Override
            public void run() {
                handleSlideChanged(null, mPagerAdapter.getItem(pager.getCurrentItem()));
            }
        });

        slidesNumber = fragments.size();

        initController();
    }

    @Override
    public void onBackPressed() {
        // Do nothing if go back lock is enabled or slide has custom policy.
        if (!isGoBackLockEnabled) {
            super.onBackPressed();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus && isImmersiveModeEnabled) {
            setImmersiveMode(true, isImmersiveModeSticky);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (isImmersiveModeEnabled) {
            gestureDetector.onTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("baseProgressButtonEnabled", baseProgressButtonEnabled);
        outState.putBoolean("progressButtonEnabled", progressButtonEnabled);
        outState.putBoolean("nextEnabled", pager.isPagingEnabled());
        outState.putBoolean("nextPagingEnabled", pager.isNextPagingEnabled());
        outState.putBoolean("skipButtonEnabled", skipButtonEnabled);
        outState.putInt("lockPage", pager.getLockPage());
        outState.putInt("currentItem", pager.getCurrentItem());

        outState.putBoolean(INSTANCE_DATA_IMMERSIVE_MODE_ENABLED, isImmersiveModeEnabled);
        outState.putBoolean(INSTANCE_DATA_IMMERSIVE_MODE_STICKY, isImmersiveModeSticky);
        outState.putBoolean(INSTANCE_DATA_COLOR_TRANSITIONS_ENABLED, areColorTransitionsEnabled);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        this.baseProgressButtonEnabled = savedInstanceState.getBoolean("baseProgressButtonEnabled");
        this.progressButtonEnabled = savedInstanceState.getBoolean("progressButtonEnabled");
        this.skipButtonEnabled = savedInstanceState.getBoolean("skipButtonEnabled");
        this.savedCurrentItem = savedInstanceState.getInt("currentItem");
        pager.setPagingEnabled(savedInstanceState.getBoolean("nextEnabled"));
        pager.setNextPagingEnabled(savedInstanceState.getBoolean("nextPagingEnabled"));
        pager.setLockPage(savedInstanceState.getInt("lockPage"));

        isImmersiveModeEnabled = savedInstanceState.getBoolean(INSTANCE_DATA_IMMERSIVE_MODE_ENABLED);
        isImmersiveModeSticky = savedInstanceState.getBoolean(INSTANCE_DATA_IMMERSIVE_MODE_STICKY);
        areColorTransitionsEnabled = savedInstanceState.getBoolean(INSTANCE_DATA_COLOR_TRANSITIONS_ENABLED);
    }

    @Override
    public boolean onCanRequestNextPage() {
        return handleBeforeSlideChanged();
    }

    @Override
    public void onIllegallyRequestedNextPage() {
        handleIllegalSlideChangeAttempt();
    }

    private void initController() {
        if (mController == null)
            mController = new DefaultIndicatorController();

        FrameLayout indicatorContainer = (FrameLayout) findViewById(R.id.indicator_container);
        indicatorContainer.addView(mController.newInstance(this));

        mController.initialize(slidesNumber);
        if (selectedIndicatorColor != DEFAULT_COLOR)
            mController.setSelectedIndicatorColor(selectedIndicatorColor);
        if (unselectedIndicatorColor != DEFAULT_COLOR)
            mController.setUnselectedIndicatorColor(unselectedIndicatorColor);

        mController.selectPosition(currentlySelectedItem);
    }

    private void handleIllegalSlideChangeAttempt() {
        Fragment currentFragment = mPagerAdapter.getItem(pager.getCurrentItem());

        if (currentFragment != null && currentFragment instanceof ISlidePolicy) {
            ISlidePolicy slide = (ISlidePolicy) currentFragment;

            if (!slide.isPolicyRespected()) {
                slide.onUserIllegallyRequestedNextPage();
            }
        }
    }

    /**
     * Called before a slide change happens. By returning false, one can disallow the slide change.
     *
     * @return true, if the slide change should be allowed, else false
     */
    private boolean handleBeforeSlideChanged() {
        Fragment currentFragment = mPagerAdapter.getItem(pager.getCurrentItem());

        // Check if the current fragment implements ISlidePolicy, else a change is always allowed
        if (currentFragment instanceof ISlidePolicy) {
            ISlidePolicy slide = (ISlidePolicy) currentFragment;

            // Check if policy is fulfilled
            if (!slide.isPolicyRespected()) {
                return false;
            }
        }

        return true;
    }

    private void handleSlideChanged(Fragment oldFragment, Fragment newFragment) {
        // Check if oldFragment implements ISlideSelectionListener
        if (oldFragment != null && oldFragment instanceof ISlideSelectionListener) {
            ((ISlideSelectionListener) oldFragment).onSlideDeselected();
        }

        // Check if newFragment implements ISlideSelectionListener
        if (newFragment != null && newFragment instanceof ISlideSelectionListener) {
            ((ISlideSelectionListener) newFragment).onSlideSelected();
        }

        onSlideChanged(oldFragment, newFragment);
    }

    /**
     * Gets the layout id of the layout used by the current activity
     *
     * @return Layout to use
     */
    protected abstract int getLayoutId();

    /**
     * Called after a new slide has been selected
     *
     * @param position Position of the new selected slide
     */
    protected void onPageSelected(int position) {
        // ;
    }

    public boolean isSkipButtonEnabled() {
        return skipButtonEnabled;
    }

    /**
     * Called when the user clicked the skip button
     *
     * @param currentFragment Instance of the currently displayed fragment
     */
    public void onSkipPressed(Fragment currentFragment) {
    }

    protected void setScrollDurationFactor(int factor) {
        pager.setScrollDurationFactor(factor);
    }

    /**
     * Helper method for displaying a view
     *
     * @param button View which visibility should be changed
     * @param show   Whether the view should be visible or not
     */
    protected void setButtonState(View button, boolean show) {
        if (show) {
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Returns the used ViewPager instance
     *
     * @return Instance of the used ViewPager
     */
    public AppIntroViewPager getPager() {
        return pager;
    }

    /**
     * Returns all current slides.
     *
     * @return List of the current slides
     */
    @NonNull
    public List<Fragment> getSlides() {
        return mPagerAdapter.getFragments();
    }

    /**
     * Adds a new slide
     *
     * @param fragment Instance of Fragment which should be added as slide
     */
    public void addSlide(@NonNull Fragment fragment) {
        fragments.add(fragment);
        mPagerAdapter.notifyDataSetChanged();
    }

    public boolean isProgressButtonEnabled() {
        return progressButtonEnabled;
    }

    public void setOffScreenPageLimit(int limit) {
        pager.setOffscreenPageLimit(limit);
    }

    /**
     * @param savedInstanceState Null
     * @deprecated It is strongly recommended to use {@link #onCreate(Bundle)} instead. Be sure calling super.onCreate() in your method.
     * Please note that this method WILL NOT be called when the activity gets recreated i.e. the fragment instances get restored.
     * The method will only be called when there are no fragments registered to the intro at all.
     */
    public void init(@Nullable Bundle savedInstanceState) {

    }


    /**
     * Called when the user clicked the done button
     *
     * @param currentFragment Instance of the currently displayed fragment
     */
    public void onLoginPressed(Fragment currentFragment) {
    }

    /**
     * Called when the selected fragment changed. This will be called automatically if the into starts or is finished via the done button.
     *
     * @param oldFragment Instance of the fragment which was displayed before. This might be null if the the intro has just started.
     * @param newFragment Instance of the fragment which is displayed now. This might be null if the intro has finished
     */
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
    }

    @Override
    public boolean onKeyDown(int code, KeyEvent kvent) {
        if (code == KeyEvent.KEYCODE_ENTER || code == KeyEvent.KEYCODE_BUTTON_A || code == KeyEvent.KEYCODE_DPAD_CENTER) {
            ViewPager vp = (ViewPager) this.findViewById(R.id.view_pager);
            if (vp.getCurrentItem() == vp.getAdapter().getCount() - 1) {
                onLoginPressed(fragments.get(vp.getCurrentItem()));
            } else {
                vp.setCurrentItem(vp.getCurrentItem() + 1);
            }
            return false;
        }
        return super.onKeyDown(code, kvent);
    }

    /**
     * Allows the user to set the nav bar color of their app intro
     *
     * @param Color string form of color in 3 or 6 digit hex form (#ffffff)
     */
    public void setNavBarColor(String Color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(android.graphics.Color.parseColor(Color));
        }
    }

    /**
     * Allows the user to set the nav bar color of their app intro
     *
     * @param color int form of color. pass your color resource to here (R.color.your_color)
     */
    public void setNavBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, color));
        }
    }

    /**
     * Allows for setting statusbar visibility (true by default)
     *
     * @param isVisible put true to show status bar, and false to hide it
     */
    public void showStatusBar(boolean isVisible) {
        if (!isVisible) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * Specifies whether to enable the immersive mode.
     * Note that immersive mode is only supported on Kitkat and newer.
     *
     * @param isEnabled Whether the immersive mode should be enabled or not.
     * @param isSticky  Whether to use the sticky immersive mode or not
     */
    public void setImmersiveMode(boolean isEnabled, boolean isSticky) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!isEnabled && isImmersiveModeEnabled) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

                isImmersiveModeEnabled = false;
            } else if (isEnabled) {

                int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;

                if (isSticky) {
                    flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                    isImmersiveModeSticky = true;
                } else {
                    flags |= View.SYSTEM_UI_FLAG_IMMERSIVE;
                    isImmersiveModeSticky = false;
                }

                getWindow().getDecorView().setSystemUiVisibility(flags);

                isImmersiveModeEnabled = true;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ALL_PERMISSIONS:
                pager.setCurrentItem(pager.getCurrentItem() + 1);
                break;
            default:
        }

    }

    private final class PagerOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (areColorTransitionsEnabled) {
                if (position < mPagerAdapter.getCount() - 1) {
                    if (mPagerAdapter.getItem(position) instanceof ISlideBackgroundColorHolder && mPagerAdapter.getItem(position + 1) instanceof ISlideBackgroundColorHolder) {
                        Fragment currentSlide = mPagerAdapter.getItem(position);
                        Fragment nextSlide = mPagerAdapter.getItem(position + 1);

                        ISlideBackgroundColorHolder currentSlideCasted = (ISlideBackgroundColorHolder) currentSlide;
                        ISlideBackgroundColorHolder nextSlideCasted = (ISlideBackgroundColorHolder) nextSlide;

                        // Check if both fragments are attached to an activity, otherwise getDefaultBackgroundColor may fail.
                        if (currentSlide.isAdded() && nextSlide.isAdded()) {
                            int newColor = (int) argbEvaluator.evaluate(positionOffset, currentSlideCasted.getDefaultBackgroundColor(), nextSlideCasted.getDefaultBackgroundColor());

                            currentSlideCasted.setBackgroundColor(newColor);
                            nextSlideCasted.setBackgroundColor(newColor);
                        }
                    } else {
                        throw new IllegalStateException("Color transitions are only available if all slides implement ISlideBackgroundColorHolder.");
                    }
                }
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (slidesNumber > 1)
                mController.selectPosition(position);
            AppIntroBase.this.onPageSelected(position);

            if (slidesNumber > 0) {
                if (currentlySelectedItem == -1) {
                    handleSlideChanged(null, mPagerAdapter.getItem(position));
                } else {
                    handleSlideChanged(mPagerAdapter.getItem(currentlySelectedItem), mPagerAdapter.getItem(pager.getCurrentItem()));
                }
            }

            currentlySelectedItem = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private final class WindowGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (isImmersiveModeEnabled && !isImmersiveModeSticky) {
                setImmersiveMode(true, isImmersiveModeSticky);
            }
            return false;
        }
    }

    class PagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private Map<Integer, Fragment> retainedFragments;

        public PagerAdapter(FragmentManager fm, @NonNull List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
            this.retainedFragments = new HashMap<>();
        }

        @Override
        public Fragment getItem(int position) {
            // Check if the fragment at this position has been retained by the PagerAdapter
            if (retainedFragments.containsKey(position)) {
                return retainedFragments.get(position);
            }
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        @NonNull
        public List<Fragment> getFragments() {
            return fragments;
        }

        @NonNull
        public Collection<Fragment> getRetainedFragments() {
            return retainedFragments.values();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);

            retainedFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (retainedFragments.containsKey(position)) {
                retainedFragments.remove(position);
            }
            super.destroyItem(container, position, object);
        }
    }

}
