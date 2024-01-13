package ui.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public final class DrawingArea extends JPanel {
    private final BufferedImage image;
    private Shape currentShape;
    private ShapeType currentShapeType = ShapeType.Line;
    private boolean isImageChanged = false;

    public DrawingArea(BufferedImage image) {
        var mouseInputAdapter = new MouseInputAdapter() {
            private Point startPoint;

            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = e.getPoint();
                currentShape = currentShapeType.GetShape();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                ShapeType.SetBounds(currentShape, startPoint, e.getPoint());
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                addCurrentShape();
                isImageChanged = true;
            }
        };

        this.image = image;
        currentShape = null;
        setBackground(Color.GRAY);
        addMouseListener(mouseInputAdapter);
        addMouseMotionListener(mouseInputAdapter);
    }

    public DrawingArea(int width, int height) {
        this(createEmptyImage(width, height));
    }

    public void setCurrentShapeType(ShapeType currentShapeType) {
        this.currentShapeType = currentShapeType;
    }

    public boolean isImageChanged() {
        return isImageChanged;
    }

    public void writeFile(File file) {
        try {
            ImageIO.write(image, "png", file);
            isImageChanged = false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return isPreferredSizeSet()
                ? super.getPreferredSize()
                : new Dimension(image.getWidth(), image.getHeight());
    }

    private static BufferedImage createEmptyImage(int width, int height) {
        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        var g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());

        return image;
    }

    private void addCurrentShape() {
        if (currentShape != null) {
            var bounds = currentShape.getBounds2D();
            var width = bounds.getWidth();
            var height = bounds.getHeight();

            if (width != 0 && height != 0) {
                var imageGraphics = (Graphics2D)image.getGraphics();
                imageGraphics.setColor(Color.BLACK);
                imageGraphics.draw(currentShape);
                repaint();
            }

            currentShape = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null)
            g.drawImage(image, 0, 0, null);

        if (currentShape != null)
            ((Graphics2D)g).draw(currentShape);
    }
}
