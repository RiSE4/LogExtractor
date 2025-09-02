package app.logextractor;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

public class SplitUtil {
    private static String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }

    private static String[] getBaseNameAndExtension(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot > 0 && dot < name.length() - 1) {
            return new String[]{name.substring(0, dot), name.substring(dot + 1)};
        } else {
            return new String[]{name, ""};
        }
    }

    /**
     * ファイルを行数で分割して出力する * @return 生成ファイル数
     */
    public static int splitFileByLine(File file, int linesPerFile, Consumer<String> logger) {
        int outputCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String[] nameParts = getBaseNameAndExtension(file);
            String baseName = nameParts[0];
            String ext = nameParts[1];
            String parent = file.getParent();
            String timestamp = getCurrentTimestamp();
            int fileIndex = 1;
            int lineCount = 0;
            BufferedWriter writer = null;
            String line;
            while ((line = reader.readLine()) != null) {
                if (lineCount % linesPerFile == 0) {
                    if (writer != null) writer.close();
                    String outName = String.format("%s_part%d_%s%s%s", baseName, fileIndex++, timestamp, ext.isEmpty() ? "" : ".", ext);
                    writer = new BufferedWriter(new FileWriter(new File(parent, outName)));
                    logger.accept("出力ファイル: " + outName);
                    outputCount++;
                }
                writer.write(line);
                writer.newLine();
                lineCount++;
            }
            if (writer != null) writer.close();
        } catch (IOException ex) {
            logger.accept("エラー: " + ex.getMessage());
        }
        return outputCount;
    }

    /**
     * キーワードを含む行とその前後N行を抽出
     */
    public static void extractByKeywordWithContext(File file, String keyword, int contextLines, Consumer<String> logger) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String[] nameParts = getBaseNameAndExtension(file);
            String baseName = nameParts[0];
            String ext = nameParts[1];
            String parent = file.getParent();
            String timestamp = getCurrentTimestamp();
            String outName = String.format("%s_抽出_%s%s%s", baseName, timestamp, ext.isEmpty() ? "" : ".", ext);
            File outFile = new File(parent, outName);
            List<String> allLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
            }
            int size = allLines.size();
            boolean[] contextMarks = new boolean[size];
            int matched = 0, written = 0;
            for (int i = 0; i < size; i++) {
                if (allLines.get(i).contains(keyword)) {
                    matched++;
                    int start = Math.max(0, i - contextLines);
                    int end = Math.min(size - 1, i + contextLines);
                    for (int j = start; j <= end; j++) {
                        contextMarks[j] = true;
                    }
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
                for (int i = 0; i < size; i++) {
                    if (contextMarks[i]) {
                        writer.write(allLines.get(i));
                        writer.newLine();
                        written++;
                    }
                }
            }
            logger.accept("抽出完了。出力ファイル: " + outName);
            logger.accept("総行数: " + size + ", キーワード該当行数: " + matched + ", 出力行数: " + written);
        } catch (IOException ex) {
            logger.accept("エラー: " + ex.getMessage());
        }
    }
}