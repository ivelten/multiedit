package ui.frame;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public enum ShapeType {
    Rectangle,
    Ellipse,
    Line;

    public Shape GetShape() {
        return switch (this) {
            case Rectangle -> new Rectangle();
            case Ellipse -> new Ellipse2D.Double();
            case Line -> new Line2D.Double();
        };
    }

    public static void SetBounds(Shape shape, Point start, Point end) {
        var shapeClass = shape.getClass();

        if (Rectangle.class.isAssignableFrom(shapeClass)) {
            var rectangle = (Rectangle)shape;
            var b = new ShapeBounds(start, end);
            rectangle.setBounds(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }
        else if (Ellipse2D.Double.class.isAssignableFrom(shapeClass)) {
            var ellipse = (Ellipse2D.Double)shape;
            var b = new ShapeBounds(start, end);
            ellipse.setFrame(b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }
        else if (Line2D.Double.class.isAssignableFrom(shapeClass)) {
            var line = (Line2D.Double)shape;
            line.setLine(start.x, start.y, end.x, end.y);
        }
    }
}
