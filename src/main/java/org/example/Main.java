package org.example;

import org.example.utils.CSVReader;
import org.example.utils.CustomFileReader;
import org.example.utils.PDFContentSegmentationConfig;
import org.example.utils.Path;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.example.PDFContentSegmenter.segmentPDF;

public class Main {
    public static void main(String[] args) {
//        if (args.length != 3) {
//            System.out.println("Usage: java PDFContentSegmenter <input_pdf> <output_directory> <number_of_cuts>");
//            return;
//        }

//        String inputPdf = args[0];
//        String outputDirectory = args[1];
//        int numberOfCuts = Integer.parseInt(args[2]);
//        String baseDir = System.getProperty("user.dir");
//        System.out.println(baseDir);
//        PDFContentSegmentationConfig.Builder().fileName("Java_Assignment_PDF.pdf").noOfCuts(1).build().execute();
//        String fileName = "Java_Assignment_PDF.pdf";
//        String outputDirectory = Path.getOutputFileDirectoryPath("Java_Assignment_PDF.pdf");
//        String inputPdf = Path.getInputFilePath("Java_Assignment_PDF.pdf");
//        int numberOfCuts = 1;
//        System.out.println("inputPath: " + inputPdf);
//        System.out.println("outputPath: " + outputDirectory);


        try {
//            PDFContentSegmentationConfig.Builder().fileName("Paper1.pdf").noOfCuts(3).build().execute();
//            segmentPDF(inputPdf, outputDirectory, numberOfCuts);
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