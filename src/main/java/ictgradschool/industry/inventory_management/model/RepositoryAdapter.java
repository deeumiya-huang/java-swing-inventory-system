package ictgradschool.industry.inventory_management.model;

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
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Product product = repository.getProductAt(rowIndex);

        switch (columnIndex) {
            case 0 -> product.setId(String.valueOf(aValue));
            case 1 -> product.setName(String.valueOf(aValue));
            case 2 -> product.setDescription(String.valueOf(aValue));
            case 3 -> product.setUnitPrice(Integer.parseInt(String.valueOf(aValue)));
            case 4 -> product.setStock(Integer.parseInt(String.valueOf(aValue)));
            // todo: validate in setter, or use builder to modify exist product rather than setter?
        }
        fireTableDataChanged();
        // todo: only fire cell change or all table? already exist product modification and add or remove data use different fire event?
    }

    @Override
    public void repositoryHasChanged(Repository repository) {
        fireTableDataChanged();
    }
}
