package ictgradschool.industry.inventory_management.admin;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import ictgradschool.industry.inventory_management.model.Product;

@JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
public class ProductBuilder {
    private String id;
    private String name;
    private String description;
    private double unitPrice;
    private int stock;

    public ProductBuilder(){}

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

    public Product build() throws BuilderException {
        if (this.id == null || this.name == null) {
            throw new BuilderException("ProductBuilder.build :: unable to construct Product without id or name.");
        }

        return new Product(this.id, this.name, this.description, this.unitPrice, this.stock);
    }
}
