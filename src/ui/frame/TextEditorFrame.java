package ui.frame;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public final class TextEditorFrame extends EditorFrame {
    private final JEditorPane editorPane = new JEditorPane();
    private boolean isFileChanged = false;

    public TextEditorFrame(File file, MainFrame mainFrame) {
        super(file, mainFrame);
        setupEditorPane();
        loadFile(file);
    }

    public TextEditorFrame(int count, MainFrame mainFrame) {
        super("Novo arquivo de texto " + count, mainFrame);
        setupEditorPane();
    }

    private void setupEditorPane() {
        add(editorPane);
        editorPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!isFileChanged)
                    isFileChanged = true;
            }
        });
    }

    @Override
    public boolean isFileChanged() {
        return isFileChanged;
    }

    @Override
    protected void writeFile(File file) {
        try {
            try (var fileWriter = new FileWriter(file)) {
                try (var bufferedWriter = new BufferedWriter(fileWriter)) {
                    bufferedWriter.write(editorPane.getText());
                }
            }
            isFileChanged = false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFile(File file) {
        try {
            var bufferSize = 1024;
            var contentBuilder = new StringBuilder();

            try (var fileReader = new FileReader(file)) {
                try (var bufferedReader = new BufferedReader(fileReader)) {
                    var buffer = new char[bufferSize];
                    while (bufferedReader.read(buffer) != -1) {
                        contentBuilder.append(new String(buffer));
                        buffer = new char[bufferSize];
                    }
                }
            }

            editorPane.setText(contentBuilder.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
