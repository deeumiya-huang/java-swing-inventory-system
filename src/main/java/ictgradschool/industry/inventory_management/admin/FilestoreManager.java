package ictgradschool.industry.inventory_management.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ictgradschool.industry.inventory_management.model.Product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
}
