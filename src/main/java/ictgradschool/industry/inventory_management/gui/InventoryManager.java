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
    public InventoryManager(Repository repository, File file) {
        this.file = file;
        this.repository = repository;
        table = new JTable();
        RepositoryAdapter tableModel = new RepositoryAdapter(file, repository);
        table.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete Product");
        popupMenu.add(deleteItem);

        table.setComponentPopupMenu(popupMenu);

        deleteItem.addActionListener(e -> deleteProduct());

        add(scrollPane);
        buildGui();
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

    private void buildGui() {

        setTitle("Inventory management");
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
