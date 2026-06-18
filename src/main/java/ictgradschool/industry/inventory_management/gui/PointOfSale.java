package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.model.*;
import ictgradschool.industry.inventory_management.model.product.Product;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.io.File;
import java.io.IOException;

public class PointOfSale extends JFrame {
    private final Repository repository;
    private final Cart cart;
    private JLabel totalAmountLabel;

    public PointOfSale(JFrame main, Repository repository) {
        this.repository = repository;
        cart = new Cart();
        JPanel inventoryPanel = new InventoryPanel();
        JPanel cartPanel = new CartPanel();
        JMenuBar menuBar = new MyMenuBar(this, main, cart::clearAllCart);

        buildGui(inventoryPanel, cartPanel, menuBar);

    }

    private void buildGui(JPanel inventoryPanel, JPanel cartPanel, JMenuBar menuBar) {
        setTitle("Point of sale");
        setSize(1000, 600);
        setJMenuBar(menuBar);
        setLayout(new GridLayout(1,2,10,0));

        add(inventoryPanel);
        add(cartPanel);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private class InventoryPanel extends JPanel{
        private final JTable inventoryTable;
        public InventoryPanel() {
            JButton addToCartButton = new JButton("Add to Cart");
            inventoryTable = new JTable();
            ReadOnlyTableModel tableModel = new ReadOnlyTableModel(repository);
            inventoryTable.setModel(tableModel);
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
            sorter.setRowFilter(createStockFilter());
            inventoryTable.setRowSorter(sorter);

            addToCartButton.addActionListener(e -> addToCart());

            buildPanelGui(addToCartButton, inventoryTable);
        }

        private void addToCart() {
            int selectedRow = inventoryTable.getSelectedRow();

            if (selectedRow != -1) {
                String productId = (String)inventoryTable.getValueAt(selectedRow, 0);
                Product selectedProduct = repository.getProduct(productId);
                cart.addToCart(selectedProduct);
                // notify table to change because one of the product's stock has changed.
                repository.notifyListener();
                updateTotalAmount();
            }
        }

        private RowFilter<Object, Object> createStockFilter() {
            return new RowFilter<>() {
                @Override
                public boolean include(Entry<?, ?> entry) {
                    int stockCol = 4;
                    Object value = entry.getValue(stockCol);
                    int stock = Integer.parseInt(value.toString());
                    return stock > 0;
                }
            };
        }

        private void buildPanelGui(JButton addToCartButton, JTable inventoryTable) {
            setLayout(new BorderLayout(5,5));
            setBorder(BorderFactory.createTitledBorder("Inventory"));


            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(addToCartButton);
            add(topPanel, BorderLayout.NORTH);

            JScrollPane scrollPane = new JScrollPane(inventoryTable);
            add(scrollPane, BorderLayout.CENTER);
        }
    }

    private class CartPanel extends JPanel {
        private final JTable cartTable;
        public CartPanel() {
            JButton removeButton = new JButton("Remove Item");
            cartTable = new JTable();
            CartAdapter tableModel = new CartAdapter(cart);
            cartTable.setModel(tableModel);

            totalAmountLabel = new JLabel("Total Amount: $0.00");
            JButton clearButton = new JButton("Clear");
            JButton checkoutButton = new JButton("Checkout");

            removeButton.addActionListener(e -> removeFromCart());
            
            clearButton.addActionListener(e -> {
                cart.clearAllCart();
                repository.notifyListener();
                updateTotalAmount();
            });
            
            checkoutButton.addActionListener(e -> checkout());
            
            buildPanelGui(removeButton, cartTable, clearButton, checkoutButton, totalAmountLabel);
        }

        private void checkout() {
            List<CartItem> cartItems = cart.getCartItems();
            if (cartItems.isEmpty()) {
                JOptionPane.showMessageDialog(null, "The cart is empty");
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showSaveDialog(PointOfSale.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (selectedFile.exists()) {
                    int response = JOptionPane.showConfirmDialog(PointOfSale.this,
                            "The file already exists! Would you like to overwrite it? (Note: Old data will be lost)",
                            "Confirm Overwrite",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE
                    );
                    if (response == JOptionPane.NO_OPTION) {
                        return;
                    }
                } else {
                    try {
                        boolean isCreated = selectedFile.createNewFile();
                        if (!isCreated) {
                            throw  new IOException("Failed to create a new file.");
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }

                boolean isCheckOut = cart.checkout(selectedFile);
                if (isCheckOut) {
                    repository.save();
                    repository.notifyListener();
                    updateTotalAmount();
                    JOptionPane.showMessageDialog(null, "successfully checkout! The receipt was printed.");
                } else {
                    JOptionPane.showMessageDialog(null, "checkout failed because of system problem", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        private void removeFromCart() {
            int selectedRow = cartTable.getSelectedRow();
            if (selectedRow != -1) {
                CartItem selectedItem = cart.getCartItemAt(selectedRow);
                if (selectedItem != null) {
                    cart.removeFromCart(selectedItem);
                    // notify table to change because one of the product's stock has changed.
                    repository.notifyListener();
                    updateTotalAmount();
                }
            }
        }

        private void buildPanelGui(JButton removeButton, JTable cartTable, JButton cancelButton, JButton checkoutButton, JLabel totalAmountLabel) {
            setLayout(new BorderLayout(5,5));
            setBorder(BorderFactory.createTitledBorder("Shopping Cart"));

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(removeButton);
            add(topPanel, BorderLayout.NORTH);

            JScrollPane scrollPane = new JScrollPane(cartTable);
            add(scrollPane, BorderLayout.CENTER);

            checkoutButton.setBackground(new Color(34, 139, 34));
            checkoutButton.setForeground(Color.WHITE);

            JPanel southContainer = new JPanel();
            southContainer.setLayout(new BoxLayout(southContainer, BoxLayout.Y_AXIS));

            JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
            amountPanel.add(totalAmountLabel);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
            buttonPanel.add(cancelButton);
            buttonPanel.add(checkoutButton);

            southContainer.add(amountPanel);
            southContainer.add(buttonPanel);

            add(southContainer, BorderLayout.SOUTH);
        }
    }

    private void updateTotalAmount() {
        double total = cart.getTotalPrice();
        totalAmountLabel.setText(String.format("Total Amount: $%.2f", total));
    }
}
