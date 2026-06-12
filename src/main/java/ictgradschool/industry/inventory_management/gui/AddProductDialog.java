package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.model.Product;

import javax.swing.*;
import java.awt.*;

public class AddProductDialog extends JDialog {
    private boolean confirmed = false;
    private Product product = null;

    public AddProductDialog(Frame owner) {
        super(owner, "Add New Product", true);
//        initUI();
    }
    public boolean isConfirmed() {
        return confirmed;
    }

    public Product getProduct() {
        return product;
    }
}
