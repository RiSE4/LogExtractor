package app.logextractor;

import javax.swing.*;
import java.awt.*;

public class UIUtil {
    public static JPanel createFilePanel(JTextField fileField, JButton selectFileButton) {
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.X_AXIS));
        filePanel.setOpaque(false);
        filePanel.add(new JLabel("ファイル: "));
        filePanel.add(Box.createHorizontalStrut(8));
        fileField.setEditable(false);
        filePanel.add(fileField);
        filePanel.add(Box.createHorizontalStrut(12));
        filePanel.add(selectFileButton);
        return filePanel;
    }

    public static JPanel createLinePanel(JTextField lineCountField, JButton splitByLineButton) {
        JPanel linePanel = new JPanel();
        linePanel.setLayout(new BoxLayout(linePanel, BoxLayout.X_AXIS));
        linePanel.setOpaque(false);
        linePanel.add(new JLabel("行数: "));
        linePanel.add(Box.createHorizontalStrut(16));
        linePanel.add(lineCountField);
        linePanel.add(Box.createHorizontalStrut(12));
        linePanel.add(splitByLineButton);
        return linePanel;
    }

    public static JPanel createKeywordPanel(JTextField keywordField, JTextField contextLinesField, JButton extractByKeywordButton) {
        JPanel keywordPanel = new JPanel(new GridBagLayout());
        keywordPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);

        // パディング
        gbc.fill = GridBagConstraints.BOTH;
        // キーワードラベル
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        keywordPanel.add(new JLabel("キーワード:"), gbc);
        // キーワードフィールド（3/4）
        gbc.gridx = 1;
        gbc.weightx = 0.75;
        keywordField.setPreferredSize(new Dimension(120, 40));
        keywordField.setMinimumSize(new Dimension(60, 30));
        keywordPanel.add(keywordField, gbc);
        // 前後行数ラベル
        gbc.gridx = 2;
        gbc.weightx = 0;
        keywordPanel.add(new JLabel("前後行数:"), gbc);
        // 前後行数フィールド（1/4）
        gbc.gridx = 3;
        gbc.weightx = 0.25;
        contextLinesField.setPreferredSize(new Dimension(60, 40));
        contextLinesField.setMinimumSize(new Dimension(40, 30));
        keywordPanel.add(contextLinesField, gbc);
        // 抽出ボタン
        gbc.gridx = 4;
        gbc.weightx = 0;
        keywordPanel.add(extractByKeywordButton, gbc);
        return keywordPanel;
    }

    public static JPanel createResetPanel(JButton resetButton) {
        JPanel resetPanel = new JPanel();
        resetPanel.setLayout(new BoxLayout(resetPanel, BoxLayout.X_AXIS));
        resetPanel.setOpaque(false);
        resetPanel.add(Box.createHorizontalGlue());
        resetPanel.add(resetButton);
        return resetPanel;
    }

    public static int parsePositiveInt(String str, int defaultValue) {
        try {
            int v = Integer.parseInt(str);
            return v > 0 ? v : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int parseNonNegativeInt(String str, int defaultValue) {
        try {
            int v = Integer.parseInt(str);
            return v >= 0 ? v : defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}