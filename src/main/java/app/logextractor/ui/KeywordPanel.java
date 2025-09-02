package app.logextractor.ui;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiConsumer;

public class KeywordPanel extends JPanel {
    private final JTextField keywordField = new JTextField("", 12);
    private final JTextField contextLinesField = new JTextField("10", 4);
    private final JButton extractButton = new JButton("キーワードで抽出");

    public KeywordPanel(BiConsumer<String, Integer> onExtract) {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.BOTH;

        // キーワード
        gbc.gridx = 0;
        add(new JLabel("キーワード:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.75;
        keywordField.setPreferredSize(new Dimension(120, 40));
        add(keywordField, gbc);

        // 前後行数
        gbc.gridx = 2;
        gbc.weightx = 0;
        add(new JLabel("前後行数:"), gbc);
        gbc.gridx = 3;
        gbc.weightx = 0.25;
        contextLinesField.setPreferredSize(new Dimension(60, 40));
        add(contextLinesField, gbc);

        // ボタン
        gbc.gridx = 4;
        gbc.weightx = 0;
        add(extractButton, gbc);

        extractButton.addActionListener(e -> {
            String keyword = keywordField.getText().trim();
            if (keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "キーワードを入力してください");
                return;
            }
            int ctx = 10;
            try {
                ctx = Integer.parseInt(contextLinesField.getText().trim());
                if (ctx < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "前後行数は0以上の整数で入力してください");
                return;
            }
            onExtract.accept(keyword, ctx);
        });

        extractButton.setEnabled(false);
    }

    public void setFileSelected(boolean enabled) {
        extractButton.setEnabled(enabled);
    }

    public void reset() {
        keywordField.setText("");
        contextLinesField.setText("10");
        extractButton.setEnabled(false);
    }
}
