package ictgradschool.industry.inventory_management.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ictgradschool.industry.inventory_management.model.product.Product;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class FilestoreManager {

    public static List<Product> readData(File jsonFile) {
        List<Product> results;
        ObjectMapper mapper = new ObjectMapper();
        try {
            results = mapper.readValue(jsonFile, new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            //catch null outside to let user know didn't readData successfully.
            return null;
        }
        return results;
    }

    public static void saveData(List<Product> products, File jsonFile) {

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(jsonFile, products);
            System.out.println("json file saved successfully!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printReceipt(Map<Product, Integer> map, File file) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            double totalPrice = 0.0;
            writer.println("--------------------------------");

            for (Map.Entry<Product, Integer> entry : map.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();

                String name = product.getName();
                double unitPrice = product.getUnitPrice();
                double itemTotal = unitPrice * quantity;
                totalPrice += itemTotal;

                String unitPriceStr = (quantity > 1) ? String.format("($%.2f)", unitPrice): "";
                String itemTotalStr = String.format("$%.2f", itemTotal);
                writer.printf("%-3d %-10s %8s %8s%n", quantity, name, unitPriceStr, itemTotalStr);
            }
            writer.println("================================");
            String totalSumStr = String.format("$%.2f", totalPrice);
            writer.printf("    %-19s %8s%n", "TOTAL", totalSumStr);
            writer.println("--------------------------------");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
