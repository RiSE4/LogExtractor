package app.logextractor.ui;

import javax.swing.*;
import java.util.function.IntConsumer;

public class SplitPanel extends JPanel {
    private final JTextField lineCountField = new JTextField("10000", 8);
    private final JButton splitButton = new JButton("行で分割");

    private static final int LINE_MIN = 10000;
    private static final int LINE_MAX = 2000000;

    public SplitPanel(IntConsumer onSplit) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        add(new JLabel("行数: "));
        add(Box.createHorizontalStrut(16));
        add(lineCountField);
        add(Box.createHorizontalStrut(12));
        add(splitButton);

        splitButton.addActionListener(e -> {
            try {
                int v = Integer.parseInt(lineCountField.getText().trim());
                if (v >= LINE_MIN && v <= LINE_MAX) {
                    onSplit.accept(v);
                } else {
                    JOptionPane.showMessageDialog(this, "行数は " + LINE_MIN + " ～ " + LINE_MAX + " で入力してください");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "整数で入力してください");
            }
        });
        splitButton.setEnabled(false);
    }

    public void setFileSelected(boolean enabled) {
        splitButton.setEnabled(enabled);
    }

    public void reset() {
        lineCountField.setText("10000");
        splitButton.setEnabled(false);
    }
}
