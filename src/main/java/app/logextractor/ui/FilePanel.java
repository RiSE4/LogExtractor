package app.logextractor.ui;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class FilePanel extends JPanel {
    private final JTextField fileField = new JTextField(30);

    public FilePanel(Consumer<File> onFileSelected) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        JButton selectFileButton = new JButton("ファイルを選択");
        fileField.setEditable(false);

        add(new JLabel("ファイル: "));
        add(Box.createHorizontalStrut(8));
        add(fileField);
        add(Box.createHorizontalStrut(12));
        add(selectFileButton);

        //ボタンでファイルを選択
        selectFileButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int ret = chooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                fileField.setText(file.getAbsolutePath());
                onFileSelected.accept(file);
            }
        });

        //DDでファイルを選択
        fileField.setTransferHandler(new TransferHandler() {
            public boolean canImport(TransferSupport support) {
                return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
            }

            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }
                try {
                    Object data = support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (data instanceof List<?> list) {
                        if (!list.isEmpty() && list.getFirst() instanceof File file) {
                            setFile(file, onFileSelected);
                            return true;
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        });
    }

    private void setFile(File file, Consumer<File> onFileSelected) {
        fileField.setText(file.getAbsolutePath());
        onFileSelected.accept(file);
    }

    public void reset() {
        fileField.setText("");
    }
}
