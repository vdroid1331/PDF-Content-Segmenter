package org.example;

import org.example.utils.CSVReader;
import org.example.utils.CustomFileReader;
import org.example.utils.PDFContentSegmentationConfig;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            CustomFileReader csvFileReader = new CSVReader();
            List<PDFContentSegmentationConfig> pdfContentSegmentationConfigList = csvFileReader.readFile();
            for (PDFContentSegmentationConfig pdfContentSegmentationConfig : pdfContentSegmentationConfigList) {
                pdfContentSegmentationConfig.execute();
            }
            System.out.println("PDF segmentation completed successfully.");
        } catch (Exception e) {
            System.err.println("Error processing PDF: " + e.getMessage());
        }
    }
}