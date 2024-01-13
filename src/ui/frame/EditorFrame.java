package ui.frame;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.beans.PropertyVetoException;
import java.io.File;

public abstract class EditorFrame extends JInternalFrame {
    private File file;
    protected final static int defaultWidth = 640;
    protected final static int defaultHeight = 480;

    protected EditorFrame(File file, MainFrame mainFrame) {
        this(file.getName(), mainFrame);
        this.file = file;
    }

    protected EditorFrame(String title, MainFrame mainFrame) {
        super(title, true, true, true);
        mainFrame.getDesktopPane().add(this);

        var activeFrame = mainFrame.getActiveEditorFrame();
        int x = 20, y = 20;

        if (activeFrame != null) {
            x = activeFrame.getX() + 20;
            y = activeFrame.getY() + 20;
        }

        setBounds(x, y, defaultWidth, defaultHeight);
        setVisible(true);

        try {
            if (activeFrame != null)
                activeFrame.setSelected(false);
            setSelected(true);
        } catch (PropertyVetoException ignored) { }

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                if (trySave())
                    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                else
                    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        });
    }

    public boolean trySave() {
        if (isFileChanged()) {
            var messageResponse = JOptionPane.showConfirmDialog(
                    getParent(),
                    "Deseja salvar as alterações no arquivo '" + this.getTitle() + "'?",
                    "Salvar alterações",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            switch (messageResponse) {
                case JOptionPane.YES_OPTION -> { return save(); }
                case JOptionPane.NO_OPTION -> { return true; }
                case JOptionPane.CANCEL_OPTION -> { return false; }
            }
        }

         return true;
    }

    public abstract boolean isFileChanged();

    protected abstract void writeFile(File file);

    public boolean save() {
        if (file != null) {
            writeFile(file);
            return true;
        }

        try {
            var fileChooser = new JFileChooser();
            var fileChooserResponse = fileChooser.showSaveDialog(getParent());

            if (fileChooserResponse == JFileChooser.APPROVE_OPTION) {
                var file = fileChooser.getSelectedFile();
                var created = file.createNewFile();

                if (!created) {
                    var messageResponse = JOptionPane.showConfirmDialog(
                            getParent(),
                            "O arquivo selecionado já existe. Deseja sobrescrever o arquivo existente?",
                            "Sobrescrever arquivo",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (messageResponse == JOptionPane.NO_OPTION)
                        return false;
                }

                writeFile(file);
                setTitle(file.getName());
                this.file = file;
                return true;
            }

            return false;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
