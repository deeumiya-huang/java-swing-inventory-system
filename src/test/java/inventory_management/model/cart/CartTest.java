package inventory_management.model.cart;

import ictgradschool.industry.inventory_management.model.cart.Cart;
import ictgradschool.industry.inventory_management.model.cart.CartItem;
import ictgradschool.industry.inventory_management.model.cart.CartListener;
import ictgradschool.industry.inventory_management.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {
    private Cart cart;
    private Product product1;
    private Product product2;
    private Product product3;

    @BeforeEach
    public void setUp() {
        cart = new Cart();
        product1 = new Product("0000000001","apple","yummy",1.5, 5);
        product2 = new Product("0000000002","banana","yummy",1, 2);
        product3 = new Product("0000000003","guava","yummy",1, 0);
    }

    @Test
    public void testClearAllCart() {
        cart.addToCart(product1);
        cart.addToCart(product2);

        cart.clearAllCart();
        assertEquals(0, cart.size());
        assertEquals(5, product1.getStock());
        assertEquals(2, product2.getStock());
    }

    @Nested
    @DisplayName("addToCart()")
    class AddToCart {
        @Test
        public void addOne() {
            cart.addToCart(product1);
            assertEquals(1, cart.size());
            assertEquals(4, product1.getStock());
            assertEquals(1, cart.getCartItemAt(0).getQuantity());
        }

        @Test
        public void addMultipleSameProduct() {
            cart.addToCart(product1);
            cart.addToCart(product1);

            assertEquals(1, cart.size());
            assertEquals(3, product1.getStock());
            assertEquals(2, cart.getCartItemAt(0).getQuantity());
        }

        @Test
        public void addDifferentProduct() {
            cart.addToCart(product1);
            cart.addToCart(product2);
            cart.addToCart(product1);

            assertEquals(3, cart.size());
            assertEquals(1, cart.getCartItemAt(2).getQuantity());
            assertEquals(3, product1.getStock());
        }

        @Test
        public void outOfStock() {
            cart.addToCart(product3);

            assertEquals(0, cart.size());
        }
    }

    @Nested
    @DisplayName("removeFromCart()")
    class RemoveFromCart {
        @Test
        public void decreaseQuantity() {
            cart.addToCart(product1);
            cart.addToCart(product1);
            CartItem item = cart.getCartItemAt(0);

            cart.removeFromCart(item);

            assertEquals(1, item.getQuantity());
            assertEquals(4, product1.getStock());
        }

        @Test
        public void completelyRemove() {
            cart.addToCart(product1);
            CartItem item = cart.getCartItemAt(0);

            cart.removeFromCart(item);

            assertEquals(0, cart.size());
            assertEquals(5, product1.getStock());
        }
    }

    @Test
    public void testGetTotalPrice() {
        cart.addToCart(product1);
        cart.addToCart(product2);

        assertEquals(2.5, cart.getTotalPrice(), 1e-15);
    }

    @Test
    public void testGetCartItems() {
        List<CartItem> itemsBefore = cart.getCartItems();
        // assert the list is empty but not null.
        assertNotNull(itemsBefore);
        assertTrue(itemsBefore.isEmpty());

        cart.addToCart(product1);
        List<CartItem> itemsAfter = cart.getCartItems();
        assertEquals(1, itemsAfter.size());
        assertEquals(product1, itemsAfter.get(0).getProduct());
    }

    @Nested
    @DisplayName("getCartItemAt()")
    class GetCartItemAt {
        @Test
        public void validIndex() {
            cart.addToCart(product1);

            CartItem item0 = cart.getCartItemAt(0);
            assertNotNull(item0);
            assertSame(product1, item0.getProduct());
        }

        @Test void invalidIndex() {
            cart.addToCart(product1);

            assertNull(cart.getCartItemAt(-1));
            assertNull(cart.getCartItemAt(1));
        }
    }

    @Test
    public void testAddAndNotifyCartListener() {
        final boolean[] isCalled = {false};
        class TestListener implements CartListener{
            @Override
            public void cartHasChanged(Cart cart) {
                isCalled[0] = true;
            }
        }
        cart.addCartListener(new TestListener());
        // this should call notifyListener(), then cartHasChanged should be called.
        cart.addToCart(product1);
        assertTrue(isCalled[0]);
    }
}
