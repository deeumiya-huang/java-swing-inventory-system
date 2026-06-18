package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.model.product.BuilderException;
import ictgradschool.industry.inventory_management.model.product.ProductBuilder;
import ictgradschool.industry.inventory_management.model.product.Product;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class AddProductDialog extends JDialog {
    private JTextField idField;
    private JTextField nameField;
    private JTextField descField;
    private JTextField priceField;
    private JTextField stockField;
    private JButton confirmButton;

    ProductBuilder builder = new ProductBuilder();

    private boolean confirmed = false;
    private Product newProduct = null;

    public AddProductDialog(Frame owner) {
        super(owner, "Add New Product", true);

        JPanel formPanel = new JPanel();
        formPanelUI(formPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanelUI(buttonPanel);

        DialogUI(formPanel, buttonPanel);
        updateConfirm(); // disable confirm button in default
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
        confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        confirmButton.addActionListener(e -> handleConfirm());
        cancelButton.addActionListener(e -> dispose()); // close dialog
    }

    private void handleConfirm() {
        try {
            newProduct = builder.build();
            confirmed = true;
            dispose(); // close dialog
        } catch (BuilderException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
//        String id = idField.getText();
//        String name = nameField.getText();
//        String desc = descField.getText();
//        String priceStr = priceField.getText();
//        String stockStr = stockField.getText();
//
//        if (id.isBlank() || name.isBlank() || priceStr.isBlank() || stockStr.isBlank()) {
//            JOptionPane.showMessageDialog(getOwner(), "Please fill in all required fields.", "Input Error", JOptionPane.ERROR_MESSAGE);
//            return;
//        }
//
//        try {
//            double price = Double.parseDouble(priceStr);
//            int stock = Integer.parseInt(stockStr);
//
//            ProductBuilder pb = new ProductBuilder();
//            newProduct = pb.id(id)
//                                   .name(name)
//                                   .description(desc)
//                                   .unitPrice(price)
//                                   .stock(stock)
//                                   .build();
//
//            confirmed = true;
//            dispose(); // close dialog
//
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Price and Stock must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
//        } catch (BuilderException e) {
//            JOptionPane.showMessageDialog(null, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
//        }
    }

    private void formPanelUI(JPanel formPanel) {
        formPanel.setLayout(new GridLayout(5,2,10,10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        formPanel.add(idField);
        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateIdField();
            }
        });

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        nameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateNameField();
            }
        });

        formPanel.add(new JLabel("Description:"));
        descField = new JTextField();
        formPanel.add(descField);
        descField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                builder = builder.description(descField.getText());
            }
        });

        formPanel.add(new JLabel("Unit Price:"));
        priceField = new JTextField();
        formPanel.add(priceField);
        priceField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validatePriceField();
            }
        });

        formPanel.add(new JLabel("Stock:"));
        stockField = new JTextField();
        formPanel.add(stockField);
        stockField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateStockField();
            }
        });
    }

    private void validateIdField() {
        if (idField.getText().isBlank()) {
            idField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
            updateConfirm();
            return;
        }
        try {
            builder = builder.id(idField.getText());
            idField.setBorder(new LineBorder(Color.green));
        } catch (BuilderException _ex) {
            idField.setBorder(new LineBorder(Color.red));
        }
        updateConfirm();
    }

    private void validateNameField() {
        if (nameField.getText().isBlank()) {
            nameField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
            updateConfirm();
            return;
        }
        try {
            builder = builder.name(nameField.getText());
            nameField.setBorder(new LineBorder(Color.green));
        } catch (BuilderException _ex) {
            nameField.setBorder(new LineBorder(Color.red));
        }
        updateConfirm();
    }

    private void validatePriceField() {
        if (priceField.getText().isBlank()) {
            priceField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
            updateConfirm();
            return;
        }
        try {
            double price = Double.parseDouble(priceField.getText());
            builder = builder.unitPrice(price);
            priceField.setBorder(new LineBorder(Color.green));
        } catch (NumberFormatException | BuilderException _ex) {
            priceField.setBorder(new LineBorder(Color.red));
        }
        updateConfirm();
    }

    private void validateStockField() {
        if (stockField.getText().isBlank()) {
            stockField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
            updateConfirm();
            return;
        }
        try {
            int stock = Integer.parseInt(stockField.getText());
            builder = builder.stock(stock);
            stockField.setBorder(new LineBorder(Color.green));
        } catch (NumberFormatException | BuilderException _ex) {
            stockField.setBorder(new LineBorder(Color.red));
        }
        updateConfirm();
    }

    public void updateConfirm() {
        confirmButton.setEnabled(builder.isValid());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Product getNewProduct() {
        return newProduct;
    }

}
