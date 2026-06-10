package ictgradschool.industry.inventory_management.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ictgradschool.industry.inventory_management.admin.ProductBuilder;

@JsonDeserialize(builder = ProductBuilder.class)
public class Product {
    private String id;
    private String name;
    private String description;
    private double unitPrice;
    private int stock;

    public Product(String id, String name, String description, double unitPrice, int stock) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unitPrice = unitPrice;
        this.stock = stock;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getUnitPrice() { return unitPrice; }
    public int getStock() { return stock; }

    public void setId(String id) {
        if (id.matches("^[a-zA-Z0-9]+$")) {
            this.id = id;
        }
    }

    public void setName(String name) {
        if (name != null) {
            this.name = name;
        }
    }

    public void setDescription(String description) { this.description = description; }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice >= 0) {
            this.unitPrice = unitPrice;
        }
    }

    public void setStock(int stock) {
        if (stock >= 0) {
            this.stock = stock;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(", ");
        sb.append(name);
        sb.append(", description: ");
        sb.append(description);
        sb.append(", unit price: ");
        sb.append(unitPrice);
        sb.append(", stock quantity: ");
        sb.append(stock);

        return sb.toString();
    }
}
