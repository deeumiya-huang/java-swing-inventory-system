package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.admin.FilestoreManager;
import ictgradschool.industry.inventory_management.model.Product;
import ictgradschool.industry.inventory_management.model.Repository;
import ictgradschool.industry.inventory_management.model.RepositoryAdapter;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class InventoryManager extends JFrame {
    private final File file;
    private final Repository repository;
    private final JTable table;
    private final RepositoryAdapter tableModel;
    public InventoryManager(Repository repository, File file) {
        this.file = file;
        this.repository = repository;
        // todo: consider extract a new JPanel for table
        // table for show products list
        table = new JTable();
        tableModel = new RepositoryAdapter(file, repository);
        table.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // popup menu for delete product
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete Product");
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);
        deleteItem.addActionListener(e -> deleteProduct());

        JPanel searchPanel = new SearchPanel();
        JPanel buttonPanel = new ButtonPanel();

        buildGui(scrollPane, searchPanel, buttonPanel);
    }

    private void deleteProduct() {
        int selectedRow = table.getSelectedRow();

        // if any row has been chosen, then execute.
        if (selectedRow != -1) {
            // convert to the real index in Model, because row sorter will change the order.
            int modelRow = table.convertRowIndexToModel(selectedRow);

            Product selectedProduct = repository.getProductAt(modelRow);
            String productId = selectedProduct.getId();
            String productName = selectedProduct.getName();
            String message = String.format(
                "Are you sure you want to delete this product?\n\nID: %s\nName: %s",
                productId, productName
            );
            // show confirm dialog
            int answer = JOptionPane.showConfirmDialog(
                InventoryManager.this,
                message,
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );

            if (answer == JOptionPane.YES_OPTION) {
                repository.removeProductAt(selectedProduct);
                FilestoreManager.saveData(repository.getAllProducts(), file);
            }
        }
    }

    private void buildGui(JScrollPane scrollPane, JPanel searchPanel, JPanel buttonPanel) {
        setTitle("Inventory management");
        setLayout(new BorderLayout(10, 10));

        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 10, 0, 10),
                scrollPane.getBorder() // get default border back, or it will disappear because of the empty border above
        ));
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // todo: separate ui later
    private class SearchPanel extends JPanel {
        public SearchPanel() {
            setLayout(new BorderLayout(5,5));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
            JLabel searchLabel = new JLabel("Search Product:");
            JTextField searchField = new JTextField();
            add(searchLabel, BorderLayout.WEST);
            add(searchField, BorderLayout.CENTER);
        }
    }

    private class ButtonPanel extends JPanel {
        public ButtonPanel() {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
            JButton addButton = new JButton("Add Product");
            add(addButton);

            addButton.addActionListener(e -> showAddProductDialog());
        }

        private void showAddProductDialog() {
            AddProductDialog addProductDialog = new AddProductDialog(InventoryManager.this);
            addProductDialog.setVisible(true);

            if (addProductDialog.isConfirmed()) {
                Product newProduct = addProductDialog.getProduct();
                repository.addProduct(newProduct);
                FilestoreManager.saveData(repository.getAllProducts(), file);
            }
        }
    }
}
