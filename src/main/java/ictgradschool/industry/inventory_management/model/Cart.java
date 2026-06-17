package ictgradschool.industry.inventory_management.model;

import ictgradschool.industry.inventory_management.admin.FilestoreManager;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private List<CartItem> cartItems;
    private final List<CartListener> listeners = new ArrayList<>();

    public Cart() {
        cartItems = new ArrayList<>();
    }

    public void clearAllCart() {
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            product.setStock(product.getStock() + quantity);
        }
        cartItems = new ArrayList<>();
        notifyListener();
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

    public double getTotalPrice() {
        double totalPrice = 0;
        for (CartItem cartItem : cartItems) {
            double unitPrice = cartItem.getProduct().getUnitPrice();
            int quantity = cartItem.getQuantity();
            totalPrice += unitPrice * quantity;
        }
        return totalPrice;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
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

    public boolean checkout(File selectedFile) {
        Map<Product, Integer> map = new HashMap<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int quantity = cartItem.getQuantity();

            map.merge(product, quantity, Integer::sum);
        }
        try {
            FilestoreManager.printReceipt(map, selectedFile);
            cartItems = new ArrayList<>();
            notifyListener();
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }
}
