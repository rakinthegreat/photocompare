package ch.want.imagecompare.ui.compareimages;

import android.graphics.PointF;
import android.os.Build;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Optional;

public class PanAndZoomState {

    static final PanAndZoomState DEFAULT = new PanAndZoomState();
    private final float scale;
    private PointF centerPoint;

    private PanAndZoomState() {
        scale = 0;
        centerPoint = null;
    }

    public PanAndZoomState(final float scale, final PointF centerPoint) {
        this.scale = scale;
        this.centerPoint = centerPoint;
    }

    PanAndZoomState(final PanAndZoomState currentPhotoViewSuppMatrix) {
        scale = currentPhotoViewSuppMatrix.getScale();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            centerPoint = currentPhotoViewSuppMatrix.getCenterPoint().orElse(null);
        }
    }

    public Optional<PointF> getCenterPoint() {
        return Optional.ofNullable(centerPoint);
    }

    public float getScale() {
        return scale;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("scale", scale).append("centerPoint", centerPoint).toString();
    }
}
