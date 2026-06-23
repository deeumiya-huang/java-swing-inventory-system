package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.model.product.BuilderException;
import ictgradschool.industry.inventory_management.model.product.ProductBuilder;
import ictgradschool.industry.inventory_management.model.product.Product;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

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

        validateAllFields(); // disable confirm button in default
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
    }

    private void formPanelUI(JPanel formPanel) {
        formPanel.setLayout(new GridLayout(5,2,10,10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        DocumentListener instantValidationListener = new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { validateAllFields(); }
            @Override public void removeUpdate(DocumentEvent e) { validateAllFields(); }
            @Override public void changedUpdate(DocumentEvent e) { validateAllFields(); }
        };

        formPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        idField.getDocument().addDocumentListener(instantValidationListener);
        formPanel.add(idField);

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        nameField.getDocument().addDocumentListener(instantValidationListener);
        formPanel.add(nameField);

        formPanel.add(new JLabel("Description:"));
        descField = new JTextField();
        descField.getDocument().addDocumentListener(instantValidationListener);
        formPanel.add(descField);

        formPanel.add(new JLabel("Unit Price:"));
        priceField = new JTextField();
        priceField.getDocument().addDocumentListener(instantValidationListener);
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stock:"));
        stockField = new JTextField();
        stockField.getDocument().addDocumentListener(instantValidationListener);
        formPanel.add(stockField);
    }

    private void validateAllFields() {
        Border defaultBorder = UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border");

        validateId(defaultBorder);

        validateName(defaultBorder);
        // description field can be blank, set green as default
        builder.description(descField.getText());
        descField.setBorder(new LineBorder(Color.GREEN));

        validateUnitPrice(defaultBorder);

        validateStock(defaultBorder);

        // decide confirm button can be used or not
        confirmButton.setEnabled(builder.isValid());
    }

    private void validateStock(Border defaultBorder) {
        String stockText = stockField.getText();
        if (stockText.isBlank()) {
            stockField.setBorder(defaultBorder);
            builder.invalidateStock();
        } else {
            try {
                int stock = Integer.parseInt(stockText);
                builder.stock(stock);
                stockField.setBorder(new LineBorder(Color.GREEN));
            } catch (NumberFormatException | BuilderException ex) {
                stockField.setBorder(new LineBorder(Color.RED));
                builder.invalidateStock(); // same reason for NumberFormatException above
            }
        }
    }

    private void validateUnitPrice(Border defaultBorder) {
        String priceText = priceField.getText();
        if (priceText.isBlank()) {
            priceField.setBorder(defaultBorder);
            builder.invalidatePrice();
        } else {
            try {
                double price = Double.parseDouble(priceText);
                builder.unitPrice(price);
                priceField.setBorder(new LineBorder(Color.GREEN));
            } catch (NumberFormatException | BuilderException ex) {
                priceField.setBorder(new LineBorder(Color.RED));
                builder.invalidatePrice();
                /* if cause NumberFormatException, we need to manually set price field invalid in builder.
                Because if parseDouble failed, it will go to catch exception field before execute builder.unitPrice,
                which means the isValidUnitPrice status still remain in the last time validation, which might be valid(true).
                 */
            }
        }
    }

    private void validateName(Border defaultBorder) {
        String nameText = nameField.getText();
        if (nameText.isBlank()) {
            nameField.setBorder(defaultBorder);
            builder.invalidateName();
        } else {
            try {
                builder.name(nameText);
                nameField.setBorder(new LineBorder(Color.GREEN));
            } catch (BuilderException ex) {
                nameField.setBorder(new LineBorder(Color.RED));
            }
        }
    }

    private void validateId(Border defaultBorder) {
        String idText = idField.getText();
        if (idText.isBlank()) {
            // if field is blank, set border back to default and set builder id field invalid.
            idField.setBorder(defaultBorder);
            builder.invalidateId();
        } else {
            try {
                builder.id(idText);
                idField.setBorder(new LineBorder(Color.GREEN));
            } catch (BuilderException ex) {
                idField.setBorder(new LineBorder(Color.RED));
            }
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Product getNewProduct() {
        return newProduct;
    }

}
