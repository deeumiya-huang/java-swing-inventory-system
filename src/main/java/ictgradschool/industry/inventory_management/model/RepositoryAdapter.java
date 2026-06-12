package ictgradschool.industry.inventory_management.model;

import ictgradschool.industry.inventory_management.admin.FilestoreManager;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends AbstractTableModel implements RepositoryListener{
    private final File file;
    private final Repository repository;
    private final List<String> columnNames = new ArrayList<>(List.of("id", "name", "description", "unit price", "stock"));

    public RepositoryAdapter(File file, Repository repository) {
        this.file = file;
        this.repository = repository;
        repository.addRepositoryListener(this);
    }

    @Override
    public int getRowCount() {
        return repository.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Product product = repository.getProductAt(rowIndex);
        return switch (columnIndex) {
            case 0 -> product.getId();
            case 1 -> product.getName();
            case 2 -> product.getDescription();
            case 3 -> product.getUnitPrice();
            case 4 -> product.getStock();
            default -> null;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // todo: change to model index when using row sorter
        Product product = repository.getProductAt(rowIndex);

        try {
            switch (columnIndex) {
                case 0 -> {
                    String id = (String) aValue;
                    if (!id.matches("^[a-zA-Z0-9]+$")) {
                        throw new IllegalArgumentException("ID must contain alphanumeric characters only!");
                    }
                    product.setId(id);
                }
                case 1 -> {
                    String name = (String) aValue;
                    if (name == null || name.isBlank()) {
                        throw new IllegalArgumentException("Product name cannot be empty!");
                    }
                    product.setName(name);
                }
                case 3 -> {
                    double price = Double.parseDouble(aValue.toString());
                    if (price < 0) {
                        throw new IllegalArgumentException("Unit price cannot be less than 0!");
                    }
                    product.setUnitPrice(price);
                }
                case 4 -> {
                    int stock = Integer.parseInt(aValue.toString());
                    if (stock < 0) {
                        throw new IllegalArgumentException("Stock cannot be less than 0!");
                    }
                    product.setStock(stock);
                }
            }
            // todo: do we save Data directly here by pass file into this adapter.
            FilestoreManager.saveData(repository.getAllProducts(), file);
            fireTableCellUpdated(rowIndex, columnIndex);

        } catch (NumberFormatException e) {
            // todo: should I pass exception up to let Inventory manager deal with the error? but how to do that? pass InventoryManager into this adapter?
            JOptionPane.showMessageDialog(null, "Please enter a valid numeric format!", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void repositoryHasChanged(Repository repository) {
        fireTableDataChanged();
    }
}
