package org.example.metrics;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MetricsCSVWriter {
    private static final String CSV_HEADER = "timestamp,algorithm,n,comparisons,max_depth,time_ms\n";

    public static void writeMetrics(String filePath, String algorithm, int n, AlgorithmMetrics metrics) {
        boolean fileExists = java.nio.file.Files.exists(java.nio.file.Paths.get(filePath));

        try (FileWriter writer = new FileWriter(filePath, true)) {
            if (!fileExists) {
                writer.write(CSV_HEADER);
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String line = String.format("%s,%s,%d,%d,%d,%.3f\n",
                    timestamp, algorithm, n,
                    metrics.getComparisons(),
                    metrics.getMaxRecursionDepth(),
                    metrics.getDurationMs()
            );

            writer.write(line);
        } catch (IOException e) {
            System.err.println("Error writing metrics to CSV: " + e.getMessage());
        }
    }
}