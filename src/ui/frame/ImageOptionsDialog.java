package ui.frame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;

public final class ImageOptionsDialog extends JDialog {
    private boolean accepted = false;
    private int selectedWidth = 400;
    private int selectedHeight = 300;

    private JFormattedTextField heightEditor;
    private JFormattedTextField widthEditor;

    private ImageOptionsDialog(MainFrame mainFrame) {
        super(mainFrame, "Nova imagem", true);
        configureContentPane();
        configureLayout();
    }

    public static ImageOptions showDialog(MainFrame mainFrame) {
        var dialog = new ImageOptionsDialog(mainFrame);
        dialog.setVisible(true);
        ImageOptions imageOptions = null;

        if (dialog.accepted)
            imageOptions = new ImageOptions(dialog.selectedWidth, dialog.selectedHeight);

        dialog.dispose();

        return imageOptions;
    }

    private void configureContentPane() {
        getContentPane().setLayout(new BorderLayout());
        configureSizeControlsLayout();
        configureButtonsLayout();
    }

    private void configureLayout() {
        setResizable(false);
        pack();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void configureSizeControlsLayout() {
        var panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        var widthPanel = new JPanel();
        widthPanel.setLayout(new GridLayout(1, 2));

        var widthFormatter = new NumberFormatter();
        widthFormatter.setMinimum(1);
        widthFormatter.setMaximum(3840);
        widthFormatter.setFormat(NumberFormat.getIntegerInstance());

        var widthLabel = new JLabel("Largura:");
        widthEditor = new JFormattedTextField(widthFormatter);
        widthEditor.setValue(selectedWidth);

        widthPanel.add(widthLabel);
        widthPanel.add(widthEditor);

        var heightPanel = new JPanel();
        heightPanel.setLayout(new GridLayout(1, 2));

        var heightFormatter = new NumberFormatter();
        heightFormatter.setMinimum(1);
        heightFormatter.setMaximum(2160);
        heightFormatter.setFormat(NumberFormat.getIntegerInstance());

        var heightLabel = new JLabel("Altura:");
        heightEditor = new JFormattedTextField(heightFormatter);
        heightEditor.setValue(selectedHeight);

        heightPanel.add(heightLabel);
        heightPanel.add(heightEditor);

        panel.add(widthPanel);
        panel.add(heightPanel);

        getContentPane().add(BorderLayout.CENTER, panel);
    }

    private void configureButtonsLayout() {
        var panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));

        var okButton = new JButton("OK");
        var cancelButton = new JButton("Cancelar");

        okButton.addActionListener(e -> {
            accepted = true;
            selectedWidth = (int)widthEditor.getValue();
            selectedHeight = (int)heightEditor.getValue();
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        cancelButton.addActionListener(e -> {
            accepted = false;
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });

        panel.add(okButton);
        panel.add(cancelButton);

        getContentPane().add(BorderLayout.SOUTH, panel);
    }
}
