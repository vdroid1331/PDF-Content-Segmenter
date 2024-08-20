package org.example.utils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class Path {
    public static String getBaseDirectoryPath() {
        return System.getProperty("user.dir");
    }
    public static String getInputDirectoryPath() {
        return System.getProperty("user.dir") + File.separator + Constants.FILE_INPUT_DIRECTORY;
    }
    public static String getOutputDirectoryPath() {
        return System.getProperty("user.dir") + File.separator + Constants.FILE_OUTPUT_DIRECTORY;
    }

    public static String getInputFilePath(String fileName) {
        return getInputDirectoryPath() + File.separator + fileName;
    }

    public static String getCSVFilePath() {
        return getBaseDirectoryPath() + File.separator + Constants.FILE_CSV_INPUT;
    }

    public static String getOutputFileDirectoryPath(String fileNameWithExtension) {
        StringBuilder builder = new StringBuilder();
        String fileNameWithoutExtension = fileNameWithExtension;
        if (fileNameWithExtension.contains(".")) {
            fileNameWithoutExtension = fileNameWithExtension.substring(0, fileNameWithExtension.lastIndexOf('.'));
        }
        builder.append(fileNameWithoutExtension);
        LocalDateTime now = LocalDateTime.now();
        builder.append("_").append(now.getYear()).append("-").append(now.getMonthValue()).append("-").append(now.getDayOfMonth()).append("_").append(now.getHour()).append("-").append(now.getMinute()).append("-").append(now.getSecond());
        String outputFileDirectoryName = builder.toString();
        String outputFileDirPath = getOutputDirectoryPath() + File.separator + outputFileDirectoryName;
        File outputDir = new File(outputFileDirPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        return outputFileDirPath;
    }
}
