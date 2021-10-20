package ch.want.imagecompare.ui.compareimages;

import android.graphics.PointF;
import android.os.Build;

import java.util.Optional;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * This class handles the responsibility of remembering zoom/pan of an image, and
 * restoring that matrix on the next image on paging.
 */
abstract class ZoomPanRestoreHandler implements ViewPager.OnPageChangeListener, ImageViewEventListener {

    private int latestPagerIndex = 0;
    private boolean pagerStateIdle;
    private boolean imageResourceReady = false;
    private PanAndZoomState lastPanAndZoomState = null;

    abstract void onApplyPanAndZoomState(PanAndZoomState targetPanAndZoomState);

    /**
     * Called when the ViewPager has selected a new page. Beware that at the point
     * of this method being called, not all view elements have been laid out. Esp.
     * the PhotoView is still in the process of loading / layout
     */
    abstract void onNewPageSelected(int position);

    @Override
    public void onImageReady() {
        imageResourceReady = true;
        checkStateAndApplyPanAndZoomState();
    }

    @Override
    public void onError() {
        imageResourceReady = false;
    }

    @Override
    public void onPanChanged(final PointF newCenter) {
        updateLastPanAndZoomState(null, newCenter);
    }

    @Override
    public void onZoomChanged(final float newScale) {
        updateLastPanAndZoomState(newScale, null);
    }

    void onPanOrZoomChanged(final PanAndZoomState newPanAndZoomState) {
        lastPanAndZoomState = new PanAndZoomState(newPanAndZoomState);
    }

    private void updateLastPanAndZoomState(final @Nullable Float newScale, final @Nullable PointF newCenter) {
        //
        //
        //
        //
        //
        float scale = 1f;
        if (lastPanAndZoomState != null) {
            Float panAndZoomStateScale = lastPanAndZoomState.getScale();
            scale = panAndZoomStateScale;
        }
        if (newScale != null) scale = newScale;
        //
        //
        //
        //
        //
        PointF center = null;
        if (lastPanAndZoomState != null) {
            PointF pointF = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pointF = lastPanAndZoomState.getCenterPoint().orElse(null);
            }
            if (pointF != null) center = pointF;
        }
        if (newCenter != null) center = newCenter;
        lastPanAndZoomState = new PanAndZoomState(scale, center);
    }

    @Override
    public void onPageSelected(final int position) {
        if (latestPagerIndex != position) {
            latestPagerIndex = position;
            onNewPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(final int state) {
        pagerStateIdle = state == ViewPager.SCROLL_STATE_IDLE;
        checkStateAndApplyPanAndZoomState();
    }

    @Override
    public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
        // no-op
    }

    /**
     * Reset the internal state of "image resource is ready". Calling this method
     * ensures that the {@link #lastPanAndZoomState} will not be applied anymore until
     * a new image resource is loaded.
     */
    void resetImageResourceState() {
        imageResourceReady = false;
    }

    void checkStateAndApplyPanAndZoomState() {
        if (imageResourceReady && pagerStateIdle && lastPanAndZoomState != null) {
            resetImageResourceState();
            if (lastPanAndZoomState.getScale() > 0) {
                // on a fully zoomed-out image, there's nothing to adjust
                // so prevent any flicker caused by applying such a view state
                onApplyPanAndZoomState(lastPanAndZoomState);
            }
        }
    }
}
