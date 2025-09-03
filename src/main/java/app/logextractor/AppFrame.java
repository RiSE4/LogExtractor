package app.logextractor;

import app.logextractor.ui.*;
import app.logextractor.util.LogicUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * アプリ全体のフレーム
 */
public class AppFrame extends JFrame {
    /** 読み込んだログファイル */
    private File selectedFile;

    /** パネル - ファイル選択 */
    private final FilePanel filePanel;
    /** パネル - 行数で分割 */
    private final SplitPanel splitPanel;
    /** パネル - キーワードで抽出 */
    private final KeywordPanel keywordPanel;
    /** パネル - リセット */
    private final ResetPanel resetPanel;
    /** パネル - 実行ログ */
    private final LogPanel logPanel;

    public AppFrame() {
        setTitle("LogExtractor | ログの検索と分割");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        logPanel = new LogPanel();
        filePanel = new FilePanel(this::onFileSelected);
        splitPanel = new SplitPanel(this::onSplitByLine);
        keywordPanel = new KeywordPanel(this::onExtractByKeyword);
        resetPanel = new ResetPanel(this::reset);

        initLayout();
    }

    private void initLayout() {
        JPanel main = new JPanel();
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        main.add(filePanel);
        main.add(Box.createVerticalStrut(18));
        main.add(splitPanel);
        main.add(Box.createVerticalStrut(12));
        main.add(keywordPanel);
        main.add(Box.createVerticalStrut(6));
        main.add(resetPanel);
        main.add(Box.createVerticalStrut(10));
        main.add(logPanel.getScrollPane());

        add(main);
    }

    /** ファイル選択 */
    private void onFileSelected(File file) {
        this.selectedFile = file;
        logPanel.log("ファイル選択: " + file.getName());
        splitPanel.setFileSelected(true);
        keywordPanel.setFileSelected(true);
    }

    /** 行数分割処理 */
    private void onSplitByLine(int lines) {
        if (selectedFile == null) {
            logPanel.log("ファイルを選択してください。");
            return;
        }
        logPanel.log("分割処理開始: " + lines + "行ごと");
        int parts = LogicUtil.splitFileByLine(selectedFile, lines, logPanel::log);
        logPanel.log("分割完了。生成ファイル数: " + parts);
    }

    /** キーワード抽出処理 */
    private void onExtractByKeyword(String keyword, int contextLines) {
        if (selectedFile == null) {
            logPanel.log("ファイルを選択してください。");
            return;
        }
        logPanel.log("抽出処理開始: '" + keyword + "' 前後行数=" + contextLines);
        LogicUtil.extractByKeywordWithContext(selectedFile, keyword, contextLines, logPanel::log);
    }

    /** アプリ全体のリセット */
    private void reset() {
        selectedFile = null;
        filePanel.reset();
        splitPanel.reset();
        keywordPanel.reset();
        logPanel.clear();
    }
}
