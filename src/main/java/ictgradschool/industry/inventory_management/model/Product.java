package ictgradschool.industry.inventory_management.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ictgradschool.industry.inventory_management.admin.ProductBuilder;

import java.util.Objects;

@JsonDeserialize(builder = ProductBuilder.class)
public class Product {
    private String id;
    private String name;
    private String description;
    private double unitPrice;
    private int stock;

    public Product(String id, String name, String description, double unitPrice, int stock) {
        // force builder to use setter to validate. todo: check
        setId(id);
        setName(name);
        setDescription(description);
        setUnitPrice(unitPrice);
        setStock(stock);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getUnitPrice() { return unitPrice; }
    public int getStock() { return stock; }

    // put validation in setter
    public void setId(String id) {
        if (id == null || !id.matches("^[a-zA-Z0-9]{10}$")) {
            throw new IllegalArgumentException("ID must contain exactly 10 alphanumeric characters!");
        }
        this.id = id;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty!");
        }
        this.name = name;
    }

    public void setDescription(String description) { this.description = description; }

    public void setUnitPrice(double unitPrice) {
        if (unitPrice < 0) {
            throw new IllegalArgumentException("Unit price cannot be less than 0!");
        }
        this.unitPrice = unitPrice;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be less than 0!");
        }
        this.stock = stock;
    }

    public String toString() {
        return id +
                ", " +
                name +
                ", description: " +
                description +
                ", unit price: " +
                unitPrice +
                ", stock quantity: " +
                stock;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
