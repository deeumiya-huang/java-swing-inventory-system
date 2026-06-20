package inventory_management.admin;

import ictgradschool.industry.inventory_management.admin.FilestoreManager;
import ictgradschool.industry.inventory_management.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FilestoreManagerTest {
    private List<Product> testProducts;
    private Product p1;
    private Product p2;

    @BeforeEach
    public void setUp() {
        testProducts = new ArrayList<>();
        p1 = new Product("0000000001", "apple", "yummy", 1.5, 5);
        p2 = new Product("0000000002", "banana", "good", 2.0, 10);
        testProducts.add(p1);
        testProducts.add(p2);
    }

    @Test
    public void testSaveAndReadData(@TempDir Path tempDir) {
        File tempJsonFile = tempDir.resolve("test.json").toFile();

        assertDoesNotThrow(()-> FilestoreManager.saveData(testProducts,tempJsonFile));
        assertTrue(tempJsonFile.exists());

        List<Product> savedProducts = FilestoreManager.readData(tempJsonFile);
        assertNotNull(savedProducts);
        assertEquals(2, savedProducts.size());

        assertEquals("0000000001", savedProducts.get(0).getId());
        assertEquals(2.0, savedProducts.get(1).getUnitPrice());
    }

    @Test
    public void testReadDataWithInvalidContent(@TempDir Path tempDir) throws IOException {
        File invalidFile = tempDir.resolve("invalid_format.json").toFile();
        Files.writeString(invalidFile.toPath(), "Not a JSON content!");

        List<Product> results = FilestoreManager.readData(invalidFile);
        assertNull(results, "When the file format was wrong, it should return null");
    }

    @Test
    public void testPrintReceipt(@TempDir Path temDir) throws IOException {
        File receiptFile = temDir.resolve("receipt.txt").toFile();

        Map<Product, Integer> testMap = new HashMap<>();
        testMap.put(p1, 3);
        testMap.put(p2, 1);

        assertDoesNotThrow(()-> FilestoreManager.printReceipt(testMap, receiptFile));
        assertTrue(receiptFile.exists());

        String content = Files.readString(receiptFile.toPath());

        assertTrue(content.contains("apple"));
        assertTrue(content.contains("banana"));
        assertTrue(content.contains("TOTAL"));
        assertTrue(content.contains("$6.50"));//1.5*3 + 2*1
        assertTrue(content.contains("($1.50)"));
        assertFalse(content.contains("($2.0)"));
    }
}
