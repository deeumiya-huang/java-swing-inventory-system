package inventory_management.model.cart;

import ictgradschool.industry.inventory_management.model.cart.Cart;
import ictgradschool.industry.inventory_management.model.cart.CartAdapter;
import ictgradschool.industry.inventory_management.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.table.TableModel;

import static org.junit.jupiter.api.Assertions.*;

public class CartAdapterTest {
    private CartAdapter cartAdapter;

    @BeforeEach
    public void setUp() {
        Cart cart = new Cart();
        Product product1 = new Product("0000000001","apple","yummy",1.5, 5);
        Product product2 = new Product("0000000002","banana","yummy",1, 2);
        cart.addToCart(product1);
        cart.addToCart(product2);

        cartAdapter = new CartAdapter(cart);
        assertInstanceOf(TableModel.class, cartAdapter);
    }

    @Test
    public void testGetRowCount() {
        assertEquals(2, cartAdapter.getRowCount());
    }

    @Test
    public void testGetColumnCount() {
        assertEquals(3, cartAdapter.getColumnCount());
    }

    @Test
    public void testGetColumnName() {
        assertEquals("product", cartAdapter.getColumnName(0));
        assertEquals("quantity", cartAdapter.getColumnName(1));
        assertEquals("unit price", cartAdapter.getColumnName(2));
    }

    @Test
    public void testGetValueAt() {
        assertEquals("apple", cartAdapter.getValueAt(0,0));
        assertEquals(1, cartAdapter.getValueAt(0,1));
        assertEquals(1.5, cartAdapter.getValueAt(0,2));
        // get value null when index is out of bound
        assertNull(cartAdapter.getValueAt(2,0));
    }
}
