package ictgradschool.industry.inventory_management.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import ictgradschool.industry.inventory_management.model.Product;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FilestoreManager {

    public static List<Product> readData(File jsonFile) {
        List<Product> results = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            results = mapper.readValue(jsonFile, new TypeReference<List<Product>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
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
