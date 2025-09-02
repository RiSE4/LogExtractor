package app.logextractor.ui;

import javax.swing.*;
import java.awt.*;

public class LogPanel {
    private final JTextArea logArea = new JTextArea(12, 50);
    private final JScrollPane scrollPane;

    public LogPanel() {
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        logArea.setBackground(new Color(235, 238, 245));
        scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("処理ログ"));
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public void clear() {
        logArea.setText("");
    }
}
