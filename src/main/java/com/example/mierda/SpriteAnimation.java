package com.example.mierda;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition {

    private ImageView imageView;
    private int count;
    private int columns;
    private int offsetX;
    private int offsetY;
    private int width;
    private int height;

    private int lastIndex;

    public SpriteAnimation(ImageView imageView, Duration duration, int count,
                           int columns, int offsetX, int offsetY, int width,
                           int height) {
        this.imageView = imageView;
        this.count = count;
        this.columns = columns;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void setColumns(int c) { this.columns = c; }
    protected void setCount(int c) { this.count = c; }
    protected void setOffsetX(int c) { this.offsetX = c; }
    protected void setOffsetY(int c) { this.offsetY = c; }
    protected int getLastIndex() { return lastIndex; }
    protected void setLastIndex(int c) { this.lastIndex = c; }

    protected void interpolate(double k) {
        final int index = Math.min((int)Math.floor(k * count), count - 1);
        if (index != lastIndex) {
            final int x = (index % columns) * width + offsetX;
            final int y = (index / columns) * height + offsetY;
            imageView.setViewport(new Rectangle2D(x, y, width, height));
            lastIndex = index;
        } else {
            return;
        }
    }
}
