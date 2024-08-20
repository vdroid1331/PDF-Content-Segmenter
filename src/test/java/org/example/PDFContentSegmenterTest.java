package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PDFContentSegmenterTest {

    private PDFContentSegmenter segmenter;
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        segmenter = new PDFContentSegmenter();
    }

    @Test
    void testAnalyzeWhitespace() throws IOException {
        PDDocument document = createTestPDF();
        List<Float> whitespaces = segmenter.analyzeWhitespace(document);
        assertFalse(whitespaces.isEmpty());
        // Add more specific assertions based on your test PDF
    }

    @Test
    void testEdgeCaseOnePagePDF() throws IOException {
        PDDocument document = new PDDocument();
        document.addPage(new PDPage());
        String inputPdf = tempDir.resolve("one_page.pdf").toString();
        document.save(inputPdf);
        document.close();

        String outputDirectory = tempDir.toString();
        int numberOfCuts = 2;

        segmenter.segmentPDF(inputPdf, outputDirectory, numberOfCuts);

        File outputDir = new File(outputDirectory);
        File[] segments = outputDir.listFiles((dir, name) -> name.startsWith("segment_") && name.endsWith(".pdf"));
        assertNotNull(segments);
        assertEquals(1, segments.length);
    }

    private PDDocument createTestPDF() throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            contentStream.beginText();
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Test PDF Content");
            contentStream.newLineAtOffset(0, -50);
            contentStream.showText("With some whitespace");
            contentStream.endText();
        }

        return document;
    }

    private String createTestPDFFile() throws IOException {
        PDDocument document = createTestPDF();
        String filePath = tempDir.resolve("test.pdf").toString();
        document.save(filePath);
        document.close();
        return filePath;
    }
}