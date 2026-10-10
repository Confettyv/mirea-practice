package com.example.order.util;

import com.example.order.model.Product;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ProductRepository {
    private final Map<String, Product> products = new HashMap<>();

    public ProductRepository() {
        loadProductsFromCsv("data/sample.csv");
    }

    public ProductRepository(String filePath) {
        loadProductsFromCsv(filePath);
    }

    public void loadProductsFromCsv(String filename) {
        products.clear();
        File file = new File(filename);
        if (!file.exists()) {
            initDefaultProducts();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    System.err.println("Warning: Invalid format at line " + lineNum + ": expected 3 columns. Skipping line.");
                    continue; // Skip malformed lines
                }
                String id = parts[0].trim();
                String name = parts[1].trim();
                try {
                    double price = Double.parseDouble(parts[2].trim());
                    if (products.containsKey(id)) {
                        throw new IllegalStateException("Duplicate product ID found: " + id + " at line " + lineNum);
                    }
                    products.put(id, new Product(id, name, price));
                } catch (NumberFormatException e) {
                    System.err.println("Warning: Invalid price format at line " + lineNum + ": " + parts[2].trim() + ". Skipping line.");
                } catch (IllegalArgumentException e) {
                    System.err.println("Warning: Product data validation failed at line " + lineNum + ": " + e.getMessage() + ". Skipping line.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading products from CSV: " + e.getMessage() + ". Initializing default products.");
            initDefaultProducts();
        } catch (IllegalStateException e) {
            System.err.println("Error in CSV data: " + e.getMessage() + ". Initializing default products.");
            initDefaultProducts();
        }
    }

    private void initDefaultProducts() {
        products.clear();
        products.put("P001", new Product("P001", "Keyboard", 50.00));
        products.put("P002", new Product("P002", "Mouse", 25.00));
        products.put("P003", new Product("P003", "Monitor", 200.00));
        products.put("P004", new Product("P004", "Webcam", 30.00));
        products.put("P005", new Product("P005", "Headset", 45.00));
        products.put("P006", new Product("P006", "Microphone", 60.00));
        products.put("P007", new Product("P007", "Desk Chair", 150.00));
        products.put("P008", new Product("P008", "Desk", 300.00));
        products.put("P009", new Product("P009", "Printer", 120.00));
        products.put("P010", new Product("P010", "Scanner", 90.00));
    }

    public Product getProductById(String id) {
        return products.get(id);
    }

    public Map<String, Product> getAllProducts() {
        return Collections.unmodifiableMap(products);
    }
}
