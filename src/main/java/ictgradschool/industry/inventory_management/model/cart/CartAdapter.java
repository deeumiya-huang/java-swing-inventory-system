package ictgradschool.industry.inventory_management.model.cart;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends AbstractTableModel implements CartListener {
    private final Cart cart;
    private final List<String> columnNames = new ArrayList<>(List.of("product", "quantity", "unit price"));

    public CartAdapter(Cart cart) {
        this.cart = cart;
        cart.addCartListener(this);
    }

    @Override
    public int getRowCount() {
        return cart.size();
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
        if (rowIndex < 0 || rowIndex >= cart.size()) { return null;}
        CartItem item = cart.getCartItemAt(rowIndex);
        return  switch (columnIndex) {
            case 0 -> item.getProduct().getName();
            case 1 -> item.getQuantity();
            case 2 -> item.getProduct().getUnitPrice();
            default -> null;
        };
    }

    @Override
    public void cartHasChanged(Cart cart) {
        fireTableDataChanged();
    }
}
