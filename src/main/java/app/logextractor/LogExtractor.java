package app.logextractor;

import javax.swing.*;

public class LogExtractor {

    public static void main(String[] args) {
        try {
            //OS標準のUIスタイル取得してSwingにセット
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            System.err.println("Windows標準のUIの適用に失敗");
        }
        SwingUtilities.invokeLater(() -> new AppFrame().setVisible(true));
    }
}