package ictgradschool.industry.inventory_management.gui;

import ictgradschool.industry.inventory_management.admin.FilestoreManager;
import ictgradschool.industry.inventory_management.model.Product;
import ictgradschool.industry.inventory_management.model.Repository;
import ictgradschool.industry.inventory_management.model.RepositoryAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager extends JFrame {
    private final JFrame main;
    private final File file;
    private final Repository repository;
    private final JTable table;
    // for sorting
    private final TableRowSorter<TableModel> sorter;

    public InventoryManager(JFrame main, Repository repository, File file) {
        this.main = main;
        this.file = file;
        this.repository = repository;
        // todo: consider extract a new JPanel for table
        // table for show products list
        table = new JTable();
        RepositoryAdapter tableModel = new RepositoryAdapter(file, repository);
        table.setModel(tableModel);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // add popup menu for delete product
        deleteProductPopupMenu();

        JPanel searchPanel = new SearchPanel();
        JPanel buttonPanel = new ButtonPanel();
        JMenuBar menuBar = new MyMenuBar(this, main);

        buildGui(table, searchPanel, buttonPanel, menuBar);
    }

    private void deleteProductPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete Product");
        popupMenu.add(deleteItem);
        table.setComponentPopupMenu(popupMenu);
        deleteItem.addActionListener(e -> deleteProduct());
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

    private void buildGui(JTable table, JPanel searchPanel, JPanel buttonPanel, JMenuBar menuBar) {
        setTitle("Inventory management");
        setLayout(new BorderLayout(10, 10));
        setJMenuBar(menuBar);

        JScrollPane scrollPane = new JScrollPane(table);
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

    private class SearchPanel extends JPanel {
        private final JComboBox<String> searchFieldCombo;
        private final JTextField searchField;
        private final JComboBox<String> stockFilterCombo;

        public SearchPanel() {
            searchFieldCombo = new JComboBox<>(new String[]{"All Fields", "Id", "Name", "Description"});
            searchField = new JTextField(15);
            JButton clearButton = new JButton("Clear");
            stockFilterCombo = new JComboBox<>(new String[]{"All items", "In Stock (>0)", "Out of Stock (=0)"});

            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override public void insertUpdate(DocumentEvent e) { applyFilters(); }
                @Override public void removeUpdate(DocumentEvent e) { applyFilters(); }
                @Override public void changedUpdate(DocumentEvent e) { applyFilters(); }
            });

            searchFieldCombo.addActionListener(e -> applyFilters());
            stockFilterCombo.addActionListener(e -> applyFilters());

            clearButton.addActionListener(e -> {
                searchField.setText("");
                searchFieldCombo.setSelectedIndex(0);
                stockFilterCombo.setSelectedIndex(0);
            });

            buildPanelGui(searchFieldCombo, searchField ,clearButton, stockFilterCombo);
        }

        private void applyFilters() {
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            String searchText = searchField.getText().trim();
            if (!searchText.isEmpty()) {
                String regex = "(?i)" + searchText; // case-insensitive

                int idCol = 0;
                int nameCol = 1;
                int descCol = 2;

                int selectedSearchIndex = searchFieldCombo.getSelectedIndex();

                switch (selectedSearchIndex) {
                    case 1:
                        filters.add(RowFilter.regexFilter(regex, idCol));
                        break;
                    case 2:
                        filters.add(RowFilter.regexFilter(regex, nameCol));
                        break;
                    case 3:
                        filters.add(RowFilter.regexFilter(regex, descCol));
                        break;
                    default:
                        filters.add(RowFilter.regexFilter(regex, idCol, nameCol, descCol));
                        break;
                }
            }

            int filterIndex = stockFilterCombo.getSelectedIndex();
            if (filterIndex == 1) {
                filters.add(createStockFilter(true));
            } else if (filterIndex == 2) {
                filters.add(createStockFilter(false));
            }

            if (filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }

        }

        private RowFilter<Object, Object> createStockFilter(boolean inStock) {
            return new RowFilter<Object, Object>() {
                @Override
                public boolean include(Entry<?, ?> entry) {
                    int stockCol = 4;
                    Object value = entry.getValue(stockCol);
                    int stock = Integer.parseInt(value.toString());

                    if (inStock) {
                        return stock > 0;
                    } else {
                        return stock == 0;
                    }
                }
            };
        }

        private void buildPanelGui(JComboBox<String> searchFieldCombo, JTextField searchField, JButton clearButton, JComboBox<String> stockFilterCombo) {
            setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
            setBorder(BorderFactory.createEmptyBorder(10,5,0,5));

            add(new JLabel("Search By:"));
            add(searchFieldCombo);
            add(searchField);
            add(clearButton);

            add(Box.createHorizontalStrut(20));
            add(new JLabel("Stock Status:"));
            add(stockFilterCombo);
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
                Product newProduct = addProductDialog.getNewProduct();
                repository.addProduct(newProduct);
                FilestoreManager.saveData(repository.getAllProducts(), file);
            }
        }
    }
}
