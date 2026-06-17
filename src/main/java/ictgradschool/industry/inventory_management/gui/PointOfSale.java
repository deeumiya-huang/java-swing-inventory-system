package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.model.ReadOnlyTableModel;
import ictgradschool.industry.inventory_management.model.Repository;
import ictgradschool.industry.inventory_management.model.RepositoryAdapter;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class PointOfSale extends JFrame {
    private final Repository repository;
    public PointOfSale(JFrame main, Repository repository) {
        this.repository = repository;
        JPanel inventoryPanel = new InventoryPanel();
        JPanel cartPanel = new CartPanel();
        JMenuBar menuBar = new MyMenuBar(this, main);

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

        public InventoryPanel() {
            JButton addToCartButton = new JButton("Add to Cart");
            JTable inventoryTable = new JTable();
            ReadOnlyTableModel tableModel = new ReadOnlyTableModel(repository);
            inventoryTable.setModel(tableModel);
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
            sorter.setRowFilter(createStockFilter());
            inventoryTable.setRowSorter(sorter);

            buildPanelGui(addToCartButton, inventoryTable);
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
        public CartPanel() {
            JButton removeButton = new JButton("Remove Item");
            JTable cartTable = new JTable();

            JButton cancelButton = new JButton("Cancel");
            JButton checkoutButton = new JButton("Checkout");
            buildPanelGui(removeButton, cartTable, cancelButton, checkoutButton);
        }

        private void buildPanelGui(JButton removeButton, JTable cartTable, JButton cancelButton, JButton checkoutButton) {
            setLayout(new BorderLayout(5,5));
            setBorder(BorderFactory.createTitledBorder("Shopping Cart"));

            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(removeButton);
            add(topPanel, BorderLayout.NORTH);

            JScrollPane scrollPane = new JScrollPane(cartTable);
            add(scrollPane, BorderLayout.CENTER);

            checkoutButton.setBackground(new Color(34, 139, 34));
            checkoutButton.setForeground(Color.WHITE);

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
            bottomPanel.add(cancelButton);
            bottomPanel.add(checkoutButton);
            add(bottomPanel, BorderLayout.SOUTH);

        }
    }
}
