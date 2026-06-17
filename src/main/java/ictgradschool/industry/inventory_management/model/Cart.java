package ictgradschool.industry.inventory_management.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<CartItem> cartItems;
    private final List<CartListener> listeners = new ArrayList<>();

    public Cart() {
        cartItems = new ArrayList<>();
    }

    public void addToCart(Product product) {
        if (product.getStock() <= 0) return;

        product.setStock(product.getStock() - 1);

        if (!cartItems.isEmpty() && cartItems.get(cartItems.size() - 1).getProduct().equals(product)) {
            CartItem lastItem = cartItems.get(cartItems.size() - 1);
            lastItem.setQuantity(lastItem.getQuantity() + 1);
        } else {
            cartItems.add(new CartItem(product, 1));
        }
        notifyListener();
    }

    public void removeFromCart(CartItem selectedItem) {
        Product product = selectedItem.getProduct();
        product.setStock(product.getStock() + 1);

        if (selectedItem.getQuantity() > 1) {
            selectedItem.setQuantity(selectedItem.getQuantity() - 1);
        } else {
            cartItems.remove(selectedItem);
        }

        notifyListener();
    }

    public int size() {return cartItems.size();}

    public CartItem getCartItemAt(int index) {
        if (index < 0 || index >= cartItems.size()) {
            return null;
        } else {
            return cartItems.get(index);
        }
    }

    public void addCartListener (CartListener listener) {listeners.add(listener);}
    public void notifyListener() {
        for (CartListener listener : listeners) {
            listener.cartHasChanged(this);
        }
    }
}
