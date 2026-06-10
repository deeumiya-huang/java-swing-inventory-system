package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.model.Repository;
import ictgradschool.industry.inventory_management.model.RepositoryAdapter;

import javax.swing.*;
import java.awt.*;

public class InventoryManager extends JFrame {
    private Repository repository;
    public InventoryManager(Repository repository) {
        this.repository = repository;
        JTable table = new JTable();
        RepositoryAdapter tableModel = new RepositoryAdapter(repository);
        table.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane);
        buildGui();
    }

    private void buildGui() {
        setTitle("Inventory management");
    }
}
