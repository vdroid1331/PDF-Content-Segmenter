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

    public static void segmentPDF(String inputPdf, String outputDirectory, int numberOfCuts) throws IOException {
        try (PDDocument document = Loader.loadPDF(new File(inputPdf))) {
            int totalPages = document.getNumberOfPages();
            numberOfCuts = Math.min(numberOfCuts, totalPages - 1);

            List<Float> whitespaces = analyzeWhitespace(document);
            List<Integer> cutPositions = determineCutPositions(whitespaces, numberOfCuts, totalPages);

            if (cutPositions.isEmpty()) {
                // If no suitable cut positions found, create a single segment with all pages
                PDDocument segment = new PDDocument();
                for (int j = 0; j < totalPages; j++) {
                    segment.addPage(document.getPage(j));
                }
                String outputPath = outputDirectory + File.separator + "segment_1.pdf";
                segment.save(outputPath);
                segment.close();
                System.out.println("No suitable cut positions found. Created a single segment with all pages.");
            } else {
                // Process segments based on cut positions
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
            }
        }
    }

    private static List<Float> analyzeWhitespace(PDDocument document) throws IOException {
        List<Float> whitespaces = new ArrayList<>();
        PDFTextStripper stripper = new PDFTextStripper() {
            @Override
            protected void writeString(String text, List<TextPosition> textPositions) throws IOException {
                if (!textPositions.isEmpty()) {
                    TextPosition lastPosition = textPositions.get(textPositions.size() - 1);
                    TextPosition firstPosition = textPositions.get(0);

                    // Check for significant whitespace
                    if (lastPosition.getEndY() - firstPosition.getEndY() > 2 * getAverageLineHeight()) {
                        whitespaces.add(lastPosition.getEndY());
                    }
                }
                super.writeString(text, textPositions);
            }

            private float getAverageLineHeight() {
                // TODO: Implement logic to calculate average line height
                return 6f; // Placeholder value
            }
        };

        stripper.setSortByPosition(true);
        stripper.getText(document);

        return whitespaces;
    }

    private static List<Integer> determineCutPositions(List<Float> whitespaces, int numberOfCuts, int totalPages) {
        List<Float> sortedWhitespaces = new ArrayList<>(whitespaces);
        Collections.sort(sortedWhitespaces, Collections.reverseOrder());

        List<Integer> cutPositions = new ArrayList<>();
        for (int i = 0; i < numberOfCuts && i < sortedWhitespaces.size(); i++) {
            float cutWhitespace = sortedWhitespaces.get(i);
            int cutPosition = whitespaces.indexOf(cutWhitespace);
            if (cutPosition > 0 && cutPosition < totalPages - 1) {
                cutPositions.add(cutPosition);
            }
        }

        // If no cuts were found and there's more than one page, add a cut at the middle
        if (cutPositions.isEmpty() && totalPages > 1) {
            cutPositions.add(totalPages / 2);
        }

        Collections.sort(cutPositions);
        return cutPositions;
    }

}

