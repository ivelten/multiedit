package ui.frame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageEditorFrame extends EditorFrame {
    private final DrawingArea drawingArea;

    public ImageEditorFrame(int count, int width, int height, MainFrame mainFrame) {
        super("Novo arquivo de imagem " + count, mainFrame);
        drawingArea = new DrawingArea(width, height);
        configureLayout();
    }

    public ImageEditorFrame(File file, MainFrame mainFrame) {
        super(file, mainFrame);
        BufferedImage image;

        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        drawingArea = new DrawingArea(image);
        configureLayout();
    }

    private void configureLayout() {
        setLayout(new BorderLayout());

        var scrollPane = new JScrollPane(drawingArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        var toolbar = configureToolbar();

        add(BorderLayout.NORTH, toolbar);
        add(BorderLayout.CENTER, scrollPane);

        if (drawingArea.getWidth() <= 600 && drawingArea.getHeight() <= 400)
            pack();
    }

    private JPanel configureToolbar() {
        var toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        var lineButton = new JButton("Linha");
        var rectangleButton = new JButton("Retângulo");
        var ellipseButton = new JButton("Elipse");

        lineButton.addActionListener(e -> drawingArea.setCurrentShapeType(ShapeType.Line));
        rectangleButton.addActionListener(e -> drawingArea.setCurrentShapeType(ShapeType.Rectangle));
        ellipseButton.addActionListener(e -> drawingArea.setCurrentShapeType(ShapeType.Ellipse));

        toolbar.add(lineButton);
        toolbar.add(rectangleButton);
        toolbar.add(ellipseButton);

        return toolbar;
    }

    @Override
    public boolean isFileChanged() {
        return drawingArea.isImageChanged();
    }

    @Override
    protected void writeFile(File file) {
        drawingArea.writeFile(file);
    }
}
