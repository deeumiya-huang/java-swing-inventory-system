package ictgradschool.industry.inventory_management.model;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class RepositoryAdapter extends AbstractTableModel implements RepositoryListener{
    private final Repository repository;
    private final List<String> columnNames = new ArrayList<>(List.of("id", "name", "description", "unit price", "stock"));

    public RepositoryAdapter(Repository repository) {
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
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> String.class;      // id
            case 1 -> String.class;      // name
            case 2 -> String.class;      // description
            case 3 -> Double.class;      // unit price
            case 4 -> Integer.class;     // stock
            default -> Object.class;
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // todo: it will automatically change to model index when using row sorter?
        Product product = repository.getProductAt(rowIndex);

        try {
            switch (columnIndex) {
                case 0 -> {
                    String id = (String) aValue;
                    if (!id.matches("^[a-zA-Z0-9]{10}$")) {
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
                case 2 -> {
                    String desc = (String) aValue;
                    product.setDescription(desc);
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
            // save data to filestore
            repository.save();
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
