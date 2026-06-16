package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.admin.ProductBuilder;
import ictgradschool.industry.inventory_management.model.Product;

import javax.swing.*;
import java.awt.*;

public class AddProductDialog extends JDialog {
    private JTextField idField;
    private JTextField nameField;
    private JTextField descField;
    private JTextField priceField;
    private JTextField stockField;

    private boolean confirmed = false;
    private Product newProduct = null;

    public AddProductDialog(Frame owner) {
        super(owner, "Add New Product", true);

        JPanel formPanel = new JPanel();
        formPanelUI(formPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanelUI(buttonPanel);

        DialogUI(formPanel, buttonPanel);
    }

    private void DialogUI(JPanel formPanel, JPanel buttonPanel) {
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setResizable(false);
        setLocationRelativeTo(getOwner());

    }

    private void buttonPanelUI(JPanel buttonPanel) {
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        confirmButton.addActionListener(e -> handleConfirm());
        cancelButton.addActionListener(e -> dispose()); // close dialog
    }

    private void handleConfirm() {
        String id = idField.getText();
        String name = nameField.getText();
        String desc = descField.getText();
        String priceStr = priceField.getText();
        String stockStr = stockField.getText();

        if (id.isBlank() || name.isBlank() || priceStr.isBlank() || stockStr.isBlank()) {
            // todo: but this can show up
            JOptionPane.showMessageDialog(getOwner(), "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            ProductBuilder pb = new ProductBuilder();
            newProduct = pb.id(id)
                                   .name(name)
                                   .description(desc)
                                   .unitPrice(price)
                                   .stock(stock)
                                   .build();

            confirmed = true;
            dispose(); // close dialog

        } catch (NumberFormatException e) {
            //todo: why I pass this or getOwner() can't show up.
            JOptionPane.showMessageDialog(null, "Price and Stock must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void formPanelUI(JPanel formPanel) {
        formPanel.setLayout(new GridLayout(5,2,10,10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Description:"));
        descField = new JTextField();
        formPanel.add(descField);

        formPanel.add(new JLabel("Unit Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stock:"));
        stockField = new JTextField();
        formPanel.add(stockField);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Product getNewProduct() {
        return newProduct;
    }
}
