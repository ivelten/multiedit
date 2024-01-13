package ui.frame;

import java.awt.*;

public final class ShapeBounds {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public ShapeBounds(Point start, Point end) {
        x = Math.min(start.x, end.x);
        y = Math.min(start.y, end.y);
        width = Math.abs(start.x - end.x);
        height = Math.abs(start.y - end.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
