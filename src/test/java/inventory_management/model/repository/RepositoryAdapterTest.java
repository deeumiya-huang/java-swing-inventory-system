package inventory_management.model.repository;

import ictgradschool.industry.inventory_management.model.product.Product;
import ictgradschool.industry.inventory_management.model.repository.Repository;
import ictgradschool.industry.inventory_management.model.repository.RepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryAdapterTest {
    private RepositoryAdapter repositoryAdapter;
    
    @BeforeEach
    public void setUp(@TempDir Path tempDir) {
        // prevent JOptionPane pop up when testing setValueAt method, and throw Headless exception instead.
        System.setProperty("java.awt.headless", "true");

        File tempFile = tempDir.resolve("test.dat").toFile();
        Repository repository = new Repository(tempFile);
        Product p1 = new Product("0000000001","apple","yummy",1.5, 5);
        Product p2 = new Product("0000000002","banana","yummy",1, 2);
        repository.addProduct(p1);
        repository.addProduct(p2);
        
        repositoryAdapter = new RepositoryAdapter(repository);
        assertInstanceOf(TableModel.class, repositoryAdapter);
    }
    
    @Test
    public void testGetRowCount() {
        assertEquals(2, repositoryAdapter.getRowCount());
    }
    
    @Test
    public void testGetColumnCount() {
        assertEquals(5, repositoryAdapter.getColumnCount());
    }
    
    @Test
    public void testGetColumnName() {
        assertEquals("id", repositoryAdapter.getColumnName(0));
        assertEquals("name", repositoryAdapter.getColumnName(1));
        assertEquals("description", repositoryAdapter.getColumnName(2));
        assertEquals("unit price", repositoryAdapter.getColumnName(3));
        assertEquals("stock", repositoryAdapter.getColumnName(4));
    }
    
    @Test
    public void testGetValueAt() {
        assertEquals("0000000001", repositoryAdapter.getValueAt(0,0));
        assertEquals("apple", repositoryAdapter.getValueAt(0,1));
        assertEquals("yummy", repositoryAdapter.getValueAt(0,2));
        assertEquals(1.5, repositoryAdapter.getValueAt(0,3));
        assertEquals(5, repositoryAdapter.getValueAt(0,4));
        // get value null when index is out of bound
        assertNull(repositoryAdapter.getValueAt(2,0));
    }

    @Nested
    @DisplayName("setValueAt()")
    class SetValueAt {
        @Test
        public void setValidValue() {
            repositoryAdapter.setValueAt("0123456789", 0, 0);
            assertEquals("0123456789", repositoryAdapter.getValueAt(0, 0));

            repositoryAdapter.setValueAt("banana", 0, 1);
            assertEquals("banana", repositoryAdapter.getValueAt(0, 1));

            repositoryAdapter.setValueAt("fresh", 0, 2);
            assertEquals("fresh", repositoryAdapter.getValueAt(0, 2));

            repositoryAdapter.setValueAt(2.5, 0, 3);
            assertEquals(2.5, repositoryAdapter.getValueAt(0, 3));

            repositoryAdapter.setValueAt(5, 0, 4);
            assertEquals(5, repositoryAdapter.getValueAt(0, 4));
        }

        @Test
        public void setInvalidValue() {
            assertThrows(HeadlessException.class, () -> repositoryAdapter.setValueAt("001", 0, 0));
            assertEquals("0000000001", repositoryAdapter.getValueAt(0, 0)); // 驗證沒被修改

            assertThrows(HeadlessException.class, () -> repositoryAdapter.setValueAt("", 0, 1));
            assertEquals("apple", repositoryAdapter.getValueAt(0, 1));

            assertThrows(HeadlessException.class, () -> repositoryAdapter.setValueAt(-1, 0, 3));
            assertEquals(1.5, (Double) repositoryAdapter.getValueAt(0, 3), 0.001);

            assertThrows(HeadlessException.class, () -> repositoryAdapter.setValueAt(-1, 0, 4));
            assertEquals(5, repositoryAdapter.getValueAt(0, 4));

            assertThrows(HeadlessException.class, () -> repositoryAdapter.setValueAt("a", 0, 3));
            assertEquals(1.5, (Double) repositoryAdapter.getValueAt(0, 3), 0.001);

            assertThrows(HeadlessException.class, () -> repositoryAdapter.setValueAt("a", 0, 4));
            assertEquals(5, repositoryAdapter.getValueAt(0, 4));
        }
    }
}
