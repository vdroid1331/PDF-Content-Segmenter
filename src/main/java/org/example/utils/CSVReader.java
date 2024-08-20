package org.example.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader implements CustomFileReader{

    // reads the input.csv file and returns a list a PDFContentSegmentationConfig
    @Override
    public List<PDFContentSegmentationConfig> readFile() {
        List<PDFContentSegmentationConfig> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Path.getCSVFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                // Assuming the CSV format is: fileName (string), noOfCuts (int)
                if (values.length == 2) {
                    String fileName = values[0].trim();
                    int noOfCuts = Integer.parseInt(values[1].trim());

                    // Create a new instance of Person and add it to the ArrayList
                    list.add(PDFContentSegmentationConfig.Builder().fileName(fileName).noOfCuts(noOfCuts).build());
                } else {
                    System.out.println("Invalid CSV line: " + line);
                }
            }
            return list;
        } catch (Exception e) {
            System.out.println("Error Reading File");
            e.printStackTrace();
            return null;
        }
    }
}
