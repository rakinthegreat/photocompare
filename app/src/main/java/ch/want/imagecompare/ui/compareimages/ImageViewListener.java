package ch.want.imagecompare.ui.compareimages;

import android.graphics.PointF;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as an adapter for the {@link SubsamplingScaleImageView} event listeners, as the view
 * only allows for 1 listener, but we need two
 */
public class ImageViewListener implements SubsamplingScaleImageView.OnImageEventListener, SubsamplingScaleImageView.OnStateChangedListener {

    private final List<ImageViewEventListener> listeners = new ArrayList<>();

    ImageViewListener addListener(final ImageViewEventListener listener) {
        listeners.add(listener);
        return this;
    }

    @Override
    public void onReady() {
    }

    @Override
    public void onImageLoaded() {
        for (ImageViewEventListener listener : listeners) {
            listener.onImageReady();
        }
    }

    @Override
    public void onPreviewLoadError(final Exception e) {
        for (ImageViewEventListener listener : listeners) {
            listener.onError();
        }
    }

    @Override
    public void onImageLoadError(final Exception e) {
        for (ImageViewEventListener listener : listeners) {
            listener.onError();
        }
    }

    @Override
    public void onTileLoadError(final Exception e) {
        for (ImageViewEventListener listener : listeners) {
            listener.onError();
        }
    }

    @Override
    public void onPreviewReleased() {
    }

    @Override
    public void onScaleChanged(final float newScale, final int origin) {
        for (ImageViewEventListener l : listeners) {
            l.onZoomChanged(newScale);
        }
    }

    @Override
    public void onCenterChanged(final PointF newCenter, final int origin) {
        for (ImageViewEventListener l : listeners) {
            l.onPanChanged(newCenter);
        }
    }
}
