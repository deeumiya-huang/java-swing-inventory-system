package ictgradschool.industry.inventory_management.admin;

import ictgradschool.industry.inventory_management.model.Product;

public class ProductBuilder {
    private String id;
    private String name;
    private String description;
    private double unitPrice;
    private int stock;

    public ProductBuilder() {
        this.reset();
    }

    public void reset() {
        this.id = null;
        this.name = null;
        this.description = null;
        this.unitPrice = 0;
        this.stock = 0;
    }

    public ProductBuilder id(String id) {
        // todo: add restriction
        this.id = id;
        return this;
    }

    public ProductBuilder name(String name) throws BuilderException{
        if (name == null) {
            throw new BuilderException("ProductBuilder.name :: value for name cannot be null");
        }

        this.name = name;
        return this;
    }

    public ProductBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder unitPrice(double unitPrice) throws BuilderException{
        if (unitPrice < 0) {
            throw new BuilderException("ProductBuilder.unitPrice :: value for unitPrice has to be positive");
        }

        this.unitPrice = unitPrice;
        return this;
    }

    public ProductBuilder stock(int stock) throws BuilderException{
        if (stock < 0) {
            throw new BuilderException("ProductBuilder.stock :: value for stock has to be positive");
        }

        this.stock = stock;
        return this;
    }

    public Product build(boolean shouldReset) throws BuilderException {
        if (this.id == null || this.name == null) {
            throw new BuilderException("ProductBuilder.build :: unable to construct Product without id or name.");
        }

        Product p = new Product(this.id, this.name, this.description,this.unitPrice, this.stock);
        if (shouldReset) {
            this.reset();
        }
        return p;
    }

    public Product build() throws BuilderException {
        return this.build(true);
    }


}
