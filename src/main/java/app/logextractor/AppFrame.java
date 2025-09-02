package app.logextractor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

public class AppFrame extends JFrame {
    private final JTextField fileField = new JTextField(30);
    private final JTextField lineCountField = new JTextField("10000", 8);
    private final JButton splitByLineButton = new JButton("行で分割");
    private final JTextField keywordField = new JTextField("", 12);
    private final JTextField contextLinesField = new JTextField("10", 4);
    private final JButton extractByKeywordButton = new JButton("キーワードで抽出");
    private final JTextArea logArea = new JTextArea(12, 50);
    private static final int LINE_MIN = 10000;
    private static final int LINE_MAX = 2000000;
    private static final String LINECOUNT_TOOLTIP = "行数は10000～2000000で入力してください";
    private File selectedFile;

    public AppFrame() {
        setTitle("LogExtractor | ログファイルの検索と分割");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JButton selectFileButton = new JButton("ファイルを選択");
        panel.add(UIUtil.createFilePanel(fileField, selectFileButton));
        panel.add(Box.createVerticalStrut(18));
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(UIUtil.createLinePanel(lineCountField, splitByLineButton));
        centerPanel.add(Box.createVerticalStrut(12));
        centerPanel.add(UIUtil.createKeywordPanel(keywordField, contextLinesField, extractByKeywordButton));
        panel.add(centerPanel);
        panel.add(Box.createVerticalStrut(6));
        JButton resetButton = new JButton("リセット");
        panel.add(UIUtil.createResetPanel(resetButton));
        panel.add(Box.createVerticalStrut(10));
        logArea.setEditable(false);
        logArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        logArea.setBackground(new Color(235, 238, 245));
        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("処理ログ"));
        panel.add(scrollPane);
        add(panel);
        selectFileButton.addActionListener(this::onSelectFile);
        splitByLineButton.addActionListener(this::onSplitByLine);
        extractByKeywordButton.addActionListener(this::onExtractByKeyword);
        resetButton.addActionListener(e -> reset());
        lineCountField.setToolTipText(LINECOUNT_TOOLTIP);
        lineCountField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validateLineCountField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validateLineCountField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validateLineCountField();
            }
        });
        keywordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateButtons();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateButtons();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateButtons();
            }
        });
        updateButtons();
    }

    /**
     * 行数入力フィールドの値を検証し、UIの状態（背景色、ツールチップ）を更新します。 * その後、ボタン全体の有効性を更新します。
     */
    private void validateLineCountField() {
        String text = lineCountField.getText().trim();
        // 1. ツールチップのテキストと背景色を設定 (前回と同じロジック)
        if (text.isEmpty()) {
            lineCountField.setToolTipText("整数で入力してください");
            lineCountField.setBackground(new Color(255, 220, 220));
        } else {
            try {
                int value = Integer.parseInt(text);
                if (value >= LINE_MIN && value <= LINE_MAX) {
                    lineCountField.setToolTipText(LINECOUNT_TOOLTIP);
                    lineCountField.setBackground(Color.WHITE);
                } else {
                    lineCountField.setToolTipText("行数は " + LINE_MIN + " ～ " + LINE_MAX + " の範囲で入力してください");
                    lineCountField.setBackground(new Color(255, 220, 220));
                }
            } catch (NumberFormatException e) {
                lineCountField.setToolTipText("整数で入力してください");
                lineCountField.setBackground(new Color(255, 220, 220));
            }
        }
        // ★★★★★★★★★★★★★★★★★★★★★ 修正箇所 ★★★★★★★★★★★★★★★★★★★★★
        // 2. 表示中のツールチップを強制的に非表示にする
        Action hideTip = lineCountField.getActionMap().get("hideTip");
        if (hideTip != null) {
            hideTip.actionPerformed(new ActionEvent(lineCountField, ActionEvent.ACTION_PERFORMED, "hideTip"));
        }
        // 3. 新しい内容でツールチップの表示をトリガーする
        // マウスがコンポーネント上にあれば、設定した遅延(100ms)後に表示される
        Action postTip = lineCountField.getActionMap().get("postTip");
        if (postTip != null) {
            postTip.actionPerformed(new ActionEvent(lineCountField, ActionEvent.ACTION_PERFORMED, "postTip"));
        } // ★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★
        // 4. 最後にボタンの状態を更新
        updateButtons();
    }

    private void onSelectFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            selectedFile = chooser.getSelectedFile();
            fileField.setText(selectedFile.getAbsolutePath());
            log("ファイル選択: " + selectedFile.getName());
            updateButtons();
        }
    }

    private void onSplitByLine(ActionEvent e) {
        if (!ensureFileSelected()) return;
        int lineCount = UIUtil.parsePositiveInt(lineCountField.getText(), -1);
        if (lineCount <= 0) {
            log("行数は正の整数で入力してください。");
            return;
        }
        log("行数で分割処理開始: " + lineCount + "行ごと");
        int parts = SplitUtil.splitFileByLine(selectedFile, lineCount, this::log);
        if (parts > 0) {
            log("分割完了。生成ファイル数: " + parts);
        }
    }

    private void onExtractByKeyword(ActionEvent e) {
        if (!ensureFileSelected()) return;
        String keyword = keywordField.getText().trim();
        if (keyword.isEmpty()) {
            log("キーワードを入力してください。");
            return;
        }
        int contextLines = UIUtil.parseNonNegativeInt(contextLinesField.getText(), 10);
        if (contextLines < 0) {
            log("前後行数は0以上の整数で入力してください。");
            return;
        }
        log("キーワード抽出処理開始: '" + keyword + "' 前後行数: " + contextLines);
        SplitUtil.extractByKeywordWithContext(selectedFile, keyword, contextLines, this::log);
    }

    private boolean ensureFileSelected() {
        if (selectedFile == null) {
            log("ファイルを選択してください。");
            return false;
        }
        return true;
    }

    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void reset() {
        selectedFile = null;
        fileField.setText("");
        lineCountField.setText("10000");
        keywordField.setText("");
        contextLinesField.setText("10");
        logArea.setText("");
        updateButtons();
    }

    private void updateButtons() {
        boolean fileSelected = selectedFile != null;
        boolean validLineCount = false;
        try {
            int v = Integer.parseInt(lineCountField.getText().trim());
            validLineCount = (v >= LINE_MIN && v <= LINE_MAX);
        } catch (NumberFormatException ignored) {
        }
        splitByLineButton.setEnabled(fileSelected && validLineCount);
        boolean keywordEntered = !keywordField.getText().trim().isEmpty();
        extractByKeywordButton.setEnabled(fileSelected && keywordEntered);
    }
}