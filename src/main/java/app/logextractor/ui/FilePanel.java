package app.logextractor.ui;

import javax.swing.*;
import java.io.File;
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

        selectFileButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int ret = chooser.showOpenDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                fileField.setText(file.getAbsolutePath());
                onFileSelected.accept(file);
            }
        });
    }

    public void reset() {
        fileField.setText("");
    }
}
