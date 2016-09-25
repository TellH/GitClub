package tellh.com.gitclub.presentation.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import tellh.com.gitclub.R;

/**
 * Created by tlh on 2016/9/22 :)
 */

public class NestProgressWebView extends WebView implements NestedScrollingChild {

    private NestedScrollingChildHelper mChildHelper;
    private ProgressBar progressbar;
    private int mLastMotionY;

    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];

    private int mNestedYOffset;

    public NestProgressWebView(Context context) {
        this(context, null);
    }

    public NestProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        //progress bar
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setProgressDrawable(ContextCompat.getDrawable(getContext(), R.drawable.progressbar));
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5, 0, 0));
        addView(progressbar);

        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    progressbar.setProgress(newProgress);
                    progressbar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressbar.setVisibility(INVISIBLE);
                        }
                    }, 200);
                } else {
                    if (progressbar.getVisibility() == INVISIBLE)
                        progressbar.setVisibility(VISIBLE);
                    progressbar.setProgress(newProgress);
                }
            }
        });


        //Client
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressbar.setVisibility(VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressbar.setVisibility(INVISIBLE);
            }
        });

        //setting
        WebSettings settings = getSettings();
        settings.setUseWideViewPort(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo.isAvailable()) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }
    }

    public void goBackPage() {
        if (canGoBack())
            goBack();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        boolean result = false;
//
//        MotionEvent trackedEvent = MotionEvent.obtain(event);
//
//        final int action = MotionEventCompat.getActionMasked(event);
//
//        if (action == MotionEvent.ACTION_DOWN) {
//            mNestedYOffset = 0;
//        }
//
//        int y = (int) event.getY();
//
//        event.offsetLocation(0, mNestedYOffset);
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                mLastMotionY = y;
//                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
//                result = super.onTouchEvent(event);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int deltaY = mLastMotionY - y;
//
//                if (dispatchNestedPreScroll(0, deltaY, mScrollConsumed, mScrollOffset)) {
//                    deltaY -= mScrollConsumed[1];
//                    trackedEvent.offsetLocation(0, mScrollOffset[1]);
//                    mNestedYOffset += mScrollOffset[1];
//                }
//
//                int oldY = getScrollY();
//                mLastMotionY = y - mScrollOffset[1];
//                if (deltaY < 0) {
//                    int newScrollY = Math.max(0, oldY + deltaY);
//                    deltaY -= newScrollY - oldY;
//                    if (dispatchNestedScroll(0, newScrollY - deltaY, 0, deltaY, mScrollOffset)) {
//                        mLastMotionY -= mScrollOffset[1];
//                        trackedEvent.offsetLocation(0, mScrollOffset[1]);
//                        mNestedYOffset += mScrollOffset[1];
//                    }
//                }
//
//                trackedEvent.recycle();
//                result = super.onTouchEvent(trackedEvent);
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                stopNestedScroll();
//                result = super.onTouchEvent(event);
//                break;
//        }
//        return result;
//    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

}
