package org.example;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PDFContentSegmenter {
    // takes in input path of the pdf, output dir and no of cuts, and divides into n+1 segments or no of pages whichever is smaller.
    public static void segmentPDF(String inputPdf, String outputDirectory, int numberOfCuts) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(inputPdf))) {
            int totalPages = document.getNumberOfPages();
            numberOfCuts = Math.min(numberOfCuts, totalPages - 1);

            List<Float> whitespaces = analyzeWhitespace(document);
            List<Integer> cutPositions = determineCutPositions(whitespaces, numberOfCuts, totalPages);

            // Ensure we have the correct number of segments
            while (cutPositions.size() < numberOfCuts) {
                addEvenlyDistributedCut(cutPositions, totalPages);
            }

            for (int i = 0; i <= cutPositions.size(); i++) {
                int startPage = (i == 0) ? 0 : cutPositions.get(i - 1);
                int endPage = (i == cutPositions.size()) ? totalPages : cutPositions.get(i);

                PDDocument segment = new PDDocument();
                for (int j = startPage; j < endPage; j++) {
                    segment.addPage(document.getPage(j));
                }

                String outputPath = outputDirectory + File.separator + "segment_" + (i + 1) + ".pdf";
                segment.save(outputPath);
                segment.close();
            }

            System.out.println("Created " + (cutPositions.size() + 1) + " segments.");
        }
    }

    static List<Float> analyzeWhitespace(PDDocument document) throws IOException {
        List<Float> whitespaces = new ArrayList<>();
        PDFTextStripper stripper = new PDFTextStripper() {
            float lastY = -1;
            float significantWhitespaceThreshold = 20; // Adjust this value as needed

            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                if (!textPositions.isEmpty()) {
                    float currentY = textPositions.get(0).getY();
                    if (lastY != -1) {
                        float whitespace = currentY - lastY;
                        if (whitespace > significantWhitespaceThreshold) {
                            whitespaces.add(currentY);
                        }
                    }
                    lastY = currentY;
                }
                super.writeString(text, textPositions);
            }
        };

        stripper.setSortByPosition(true);
        stripper.getText(document);

        return whitespaces;
    }

    static List<Integer> determineCutPositions(List<Float> whitespaces, int numberOfCuts, int totalPages) {
        List<Float> sortedWhitespaces = new ArrayList<>(whitespaces);
        Collections.sort(sortedWhitespaces);

        List<Integer> cutPositions = new ArrayList<>();
        float pageHeight = 792; // Assuming standard US Letter size, adjust if needed

        for (int i = 0; i < sortedWhitespaces.size() && cutPositions.size() < numberOfCuts; i++) {
            float whitespace = sortedWhitespaces.get(i);
            int page = (int) (whitespace / pageHeight);
            if (page > 0 && page < totalPages - 1 && !cutPositions.contains(page)) {
                cutPositions.add(page);
            }
        }

        return cutPositions;
    }

    private static void addEvenlyDistributedCut(List<Integer> cutPositions, int totalPages) {
        for (int i = 1; i < totalPages; i++) {
            if (!cutPositions.contains(i)) {
                cutPositions.add(i);
                Collections.sort(cutPositions);
                return;
            }
        }
    }
}
