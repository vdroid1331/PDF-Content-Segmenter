package org.example;

import org.example.utils.Path;

import java.io.File;
import java.io.IOException;

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
        String inputDir = Path.getInputDirectoryPath();
        String fileName = "Java_Assignment_PDF.pdf";
        String outputDirectory = Path.getOutputDirectoryPath() + File.separator + Path.getOutputFileDirectoryName("Java_Assignment_PDF.pdf");
        String inputPdf = inputDir + File.separator + "Java_Assignment_PDF.pdf";
        int numberOfCuts = 1;
        System.out.println("inputPath: " + inputPdf);
        System.out.println("outputPath: " + outputDirectory);


        try {
            segmentPDF(inputPdf, outputDirectory, numberOfCuts);
            System.out.println("PDF segmentation completed successfully.");
        } catch (IOException e) {
            System.err.println("Error processing PDF: " + e.getMessage());
        }
    }

}