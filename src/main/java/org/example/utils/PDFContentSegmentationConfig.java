package org.example.utils;

import org.example.PDFContentSegmenter;

import java.io.IOException;

public class PDFContentSegmentationConfig {
    private String fileName;
    private int noOfCuts;
    private PDFContentSegmentationConfig() {
        fileName = "";
        noOfCuts = 0;
    }
    public static PDFContentSegmentationConfig Builder() {
        return new PDFContentSegmentationConfig();
    }
    public String getFileName() {
        return fileName;
    }
    public int getNoOfCuts() {
        return noOfCuts;
    }
    public static void validateFileName(String fileName) throws IllegalArgumentException {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
    }
    public PDFContentSegmentationConfig fileName(String fileName) throws IllegalArgumentException {
        validateFileName(fileName);
        this.fileName = fileName;
        return this;
    }
    public static void validateNoOfCuts(int noOfCuts) throws IllegalArgumentException {
        if (noOfCuts < 1) {
            throw new IllegalArgumentException("Number of segments cannot be less than 1");
        }
    }
    public PDFContentSegmentationConfig noOfCuts(int noOfCuts) throws IllegalArgumentException {
        validateNoOfCuts(noOfCuts);
        this.noOfCuts = noOfCuts;
        return this;
    }
    public PDFContentSegmentationConfig build() throws IllegalArgumentException {
        validateNoOfCuts(this.noOfCuts);
        validateFileName(this.fileName);
        return this;
    }
    public void execute() throws Exception {
        try {
            PDFContentSegmenter.segmentPDF(Path.getInputFilePath(fileName), Path.getOutputFileDirectoryPath(fileName), noOfCuts);
        } catch (Exception e) {
            System.err.println("Error processing PDF: " + e.getMessage());
        }
    }
}
