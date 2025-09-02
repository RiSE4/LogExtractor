package app.logextractor.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

    public static String getTimestamp() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public static String[] getBaseNameAndExtension(File file) {
        String name = file.getName();
        int dot = name.lastIndexOf('.');
        if (dot > 0 && dot < name.length() - 1) {
            return new String[]{name.substring(0, dot), name.substring(dot + 1)};
        } else {
            return new String[]{name, ""};
        }
    }
}
