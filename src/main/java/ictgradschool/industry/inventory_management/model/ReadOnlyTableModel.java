package ictgradschool.industry.inventory_management.model;

public class ReadOnlyTableModel extends RepositoryAdapter{
    public ReadOnlyTableModel(Repository repository) {
        super(repository);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
