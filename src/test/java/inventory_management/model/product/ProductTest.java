package inventory_management.model.product;

import ictgradschool.industry.inventory_management.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class ProductTest {
    private Product product;
    @BeforeEach
    public void setUp() {
        product = new Product("A123456789", "Apple", "Fresh", 10.5, 20);
    }
    @Test
    public void testGetter() {
        assertEquals("A123456789", product.getId());
        assertEquals("Apple", product.getName());
        assertEquals("Fresh", product.getDescription());
        assertEquals(10.5, product.getUnitPrice(),1e-15);
        assertEquals(20, product.getStock());
    }

    @Test
    public void testSetId() {
        assertDoesNotThrow(() -> product.setId("0123456789"));

        assertThrows(IllegalArgumentException.class, ()-> product.setId(""));
        assertThrows(IllegalArgumentException.class, ()-> product.setId("a123456789"));
        assertThrows(IllegalArgumentException.class, ()-> product.setId("123"));
        assertThrows(IllegalArgumentException.class, ()-> product.setId(null));
    }

    @Test
    public void testSetName() {
        assertDoesNotThrow(() -> product.setName("Banana"));

        assertThrows(IllegalArgumentException.class, ()-> product.setName(""));
        assertThrows(IllegalArgumentException.class, ()-> product.setName(" "));
        assertThrows(IllegalArgumentException.class, ()-> product.setName(null));
    }

    @Test
    public void testSetUnitPrice() {
        assertDoesNotThrow(() -> product.setUnitPrice(0));
        assertDoesNotThrow(() -> product.setUnitPrice(10.0));

        assertThrows(IllegalArgumentException.class, ()-> product.setUnitPrice(-1));
        assertThrows(IllegalArgumentException.class, ()-> product.setUnitPrice(-0.1));
    }

    @Test
    public void testSetStock() {
        assertDoesNotThrow(() -> product.setStock(0));
        assertDoesNotThrow(() -> product.setStock(50));

        assertThrows(IllegalArgumentException.class, () -> product.setStock(-1));
    }

    @Test
    public void testToString() {
        assertEquals("A123456789, Apple, description: Fresh, unit price: 10.5, stock quantity: 20", product.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        Product product2 = new Product("A123456789", "Banana", "Yellow", 20, 10);
        Product product3 = new Product("B987654321", "Apple", "Fresh", 10.5, 20);

        assertEquals(product, product2);
        assertNotEquals(product, product3);

        assertEquals(product.hashCode(), product2.hashCode());
    }

}
