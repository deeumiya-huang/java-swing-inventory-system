package inventory_management.model.product;

import ictgradschool.industry.inventory_management.model.product.BuilderException;
import ictgradschool.industry.inventory_management.model.product.Product;
import ictgradschool.industry.inventory_management.model.product.ProductBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductBuilderTest {
    ProductBuilder builder;
    @BeforeEach
    public void setUp() {
        builder = new ProductBuilder();
    }

    @Test
    public void testBuild() {
        Product product = builder
                .id("A123456789")
                .name("Apple")
                .description("Fresh")
                .unitPrice(10.5)
                .stock(10)
                .build();

        assertEquals("A123456789", product.getId());
        assertEquals("Apple", product.getName());
    }

    @Test
    public void inValidId() {
        BuilderException ex =  assertThrows(BuilderException.class,()-> {
            builder.id("001");
        });
        assertFalse(builder.isValid());
        assertEquals("ID must contain exactly 10 characters, and consist only of numbers and uppercase letters!", ex.getMessage());
    }

    @Test
    public void inValidName() {
        assertThrows(BuilderException.class, ()-> {
            builder.name("");
        });
        assertFalse(builder.isValid());
    }

    @Test
    public void inValidUnitPrice() {
        BuilderException ex = assertThrows(BuilderException.class, () -> {
            builder.unitPrice(-10.0);
        });
        assertEquals("Invalid Price", ex.getMessage());
        assertFalse(builder.isValid());
    }

    @Test
    public void invalidStock() {
        BuilderException ex = assertThrows(BuilderException.class, ()-> {
            builder.stock(-1);
        });
        assertFalse(builder.isValid());
    }

    @Test
    public void incompleteBuild() {
        builder.name("Only Name");
        assertFalse(builder.isValid());
        assertThrows(BuilderException.class, ()->{
            builder.build();
        });
    }

}
