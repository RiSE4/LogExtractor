package app.logextractor.ui;

import javax.swing.*;

public class ResetPanel extends JPanel {
    public ResetPanel(Runnable onReset) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);

        JButton resetButton = new JButton("リセット");
        add(Box.createHorizontalGlue());
        add(resetButton);

        resetButton.addActionListener(e -> onReset.run());
    }
}
