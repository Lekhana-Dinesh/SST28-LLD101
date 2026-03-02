package com.example.metrics;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads metrics from properties file into the singleton registry.
 */
public class MetricsLoader {
    public MetricsRegistry loadFromFile(String path) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(path)) {
            props.load(fis);
        }
        MetricsRegistry registry = MetricsRegistry.getInstance();
        for (String key : props.stringPropertyNames()) {
            String value = props.getProperty(key, "0").trim();
            long count;
            try {
                count = Long.parseLong(value);
            } catch (NumberFormatException e) {
                count = 0L;
            }
            registry.setCount(key, count);
        }
        return registry;
    }
}