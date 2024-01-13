package ui.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class MainFrame extends JFrame {
    private final Container contentPane;
    private final JDesktopPane desktopPane;
    private int textEditorCount = 0;
    private int imageEditorCount = 0;

    public MainFrame() {
        super("Multiedit");

        contentPane = configureContentPane();
        desktopPane = configureDesktopPane();

        configureLayout();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (var frame : desktopPane.getAllFrames()) {
                    var editorFrame = (EditorFrame)frame;
                    if (editorFrame.trySave()) {
                        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    } else {
                        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        break;
                    }
                }
            }
        });
    }

    private Container configureContentPane() {
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        return contentPane;
    }

    private JDesktopPane configureDesktopPane() {
        var desktopPane = new JDesktopPane();
        contentPane.add(BorderLayout.CENTER, desktopPane);
        return desktopPane;
    }

    private Image getAppIconImage() {
        var iconPath = getClass().getResource("/resources/app.png");

        if (iconPath != null)
            return new ImageIcon(iconPath).getImage();

        return null;
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    private void configureLayout() {
        var iconImage = getAppIconImage();

        if (iconImage != null)
            setIconImage(iconImage);

        setSize(800, 600);
        configureMenuBar();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public EditorFrame getActiveEditorFrame() {
        for (var frame : desktopPane.getAllFrames())
            if (frame.isSelected())
                return (EditorFrame)frame;

        return null;
    }

    private void configureMenuBar() {
        var textFile = "Arquivo de texto";
        var imageFile = "Arquivo de imagem";
        var menuBar = new JMenuBar();
        var fileMenu = new JMenu("Arquivo");
        var newMenu = new JMenu("Novo");
        var newTextFileMenuItem = new JMenuItem(textFile);
        var newImageFileMenuItem = new JMenuItem(imageFile);
        var openMenu = new JMenu("Abrir");
        var openTextFileMenuItem = new JMenuItem(textFile);
        var openImageFileMenuItem = new JMenuItem(imageFile);
        var saveMenuItem = new JMenuItem("Salvar");
        var exitMenuItem = new JMenuItem("Sair");

        newTextFileMenuItem.addActionListener(e -> new TextEditorFrame(++textEditorCount, this));

        newImageFileMenuItem.addActionListener(e -> {
            var imageOptions = ImageOptionsDialog.showDialog(this);

            if (imageOptions != null)
                new ImageEditorFrame(++imageEditorCount, imageOptions.getWidth(), imageOptions.getHeight(), this);
        });

        saveMenuItem.addActionListener(e -> {
            var activeFrame = getActiveEditorFrame();

            if (activeFrame != null)
                activeFrame.save();
        });

        openTextFileMenuItem.addActionListener(e -> {
            var fileChooser = new JFileChooser();

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                var file = fileChooser.getSelectedFile();
                new TextEditorFrame(file, this);
            }
        });

        openImageFileMenuItem.addActionListener(e -> {
            var fileChooser = new JFileChooser();

            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                var file = fileChooser.getSelectedFile();
                new ImageEditorFrame(file, this);
            }
        });

        exitMenuItem.addActionListener(e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)));

        newMenu.add(newTextFileMenuItem);
        newMenu.add(newImageFileMenuItem);
        openMenu.add(openTextFileMenuItem);
        openMenu.add(openImageFileMenuItem);
        fileMenu.add(newMenu);
        fileMenu.add(openMenu);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
}
