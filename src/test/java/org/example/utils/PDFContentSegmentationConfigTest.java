package org.example.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class PDFContentSegmentationConfigTest {

    private PDFContentSegmentationConfig config;

    @BeforeEach
    void setUp() {
        config = PDFContentSegmentationConfig.Builder();
    }

    @Test
    void testBuilderInitialization() {
        assertNotNull(config);
        assertEquals("", config.getFileName());
        assertEquals(0, config.getNoOfCuts());
    }

    @Test
    void testValidFileName() {
        config.fileName("test.pdf");
        assertEquals("test.pdf", config.getFileName());
    }

    @Test
    void testInvalidFileName() {
        assertThrows(IllegalArgumentException.class, () -> config.fileName(""));
        assertThrows(IllegalArgumentException.class, () -> config.fileName(null));
    }

    @Test
    void testValidNoOfCuts() {
        config.noOfCuts(5);
        assertEquals(5, config.getNoOfCuts());
    }

    @Test
    void testInvalidNoOfCuts() {
        assertThrows(IllegalArgumentException.class, () -> config.noOfCuts(0));
        assertThrows(IllegalArgumentException.class, () -> config.noOfCuts(-1));
    }

    @Test
    void testBuildWithValidConfig() {
        PDFContentSegmentationConfig builtConfig = config
                .fileName("test.pdf")
                .noOfCuts(3)
                .build();

        assertEquals("test.pdf", builtConfig.getFileName());
        assertEquals(3, builtConfig.getNoOfCuts());
    }

    @Test
    void testBuildWithInvalidConfig() {
        assertThrows(IllegalArgumentException.class, () -> config.build());

        config.fileName("test.pdf");
        assertThrows(IllegalArgumentException.class, () -> config.build());

        config = PDFContentSegmentationConfig.Builder();
        config.noOfCuts(3);
        assertThrows(IllegalArgumentException.class, () -> config.build());
    }

    @Test
    void testValidateFileName() {
        assertDoesNotThrow(() -> PDFContentSegmentationConfig.validateFileName("test.pdf"));
        assertThrows(IllegalArgumentException.class, () -> PDFContentSegmentationConfig.validateFileName(""));
        assertThrows(IllegalArgumentException.class, () -> PDFContentSegmentationConfig.validateFileName(null));
    }

    @Test
    void testValidateNoOfCuts() {
        assertDoesNotThrow(() -> PDFContentSegmentationConfig.validateNoOfCuts(1));
        assertDoesNotThrow(() -> PDFContentSegmentationConfig.validateNoOfCuts(10));
        assertThrows(IllegalArgumentException.class, () -> PDFContentSegmentationConfig.validateNoOfCuts(0));
        assertThrows(IllegalArgumentException.class, () -> PDFContentSegmentationConfig.validateNoOfCuts(-1));
    }
}