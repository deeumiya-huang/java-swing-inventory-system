package ictgradschool.industry.inventory_management.model;

public class Product {
    public int productID;
    public String productName;
    public String productDescription;
    public double unitPrice;
    public int stock;

    public Product(int productID, String productName, String productDescription, double unitPrice, int stock) {
        this.productID = productID;
        this.productName = productName;
        this.productDescription = productDescription;
        this.unitPrice = unitPrice;
        this.stock = stock;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(productID);
        sb.append(", ");
        sb.append(productName);
        sb.append(", description: ");
        sb.append(productDescription);
        sb.append(", unit price: ");
        sb.append(unitPrice);
        sb.append(", stock quantity: ");
        sb.append(stock);

        return sb.toString();
    }
}
