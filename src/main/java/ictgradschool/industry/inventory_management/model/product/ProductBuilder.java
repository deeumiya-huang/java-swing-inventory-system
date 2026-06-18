package ictgradschool.industry.inventory_management.model.product;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
public class ProductBuilder {
    private String id;
    private String name;
    private String description;
    private double unitPrice;
    private int stock;

    public ProductBuilder(){}

    public ProductBuilder id(String id) {
        this.id = id;
        return this;
    }

    public ProductBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder description(String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder unitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public ProductBuilder stock(int stock) {
        this.stock = stock;
        return this;
    }

    public Product build() throws BuilderException {
        try {
            return new Product(this.id, this.name, this.description, this.unitPrice, this.stock);
        } catch (IllegalArgumentException e){
            throw new BuilderException("ProductBuilder.build :: " + e.getMessage(), e);
            // todo: delete builder exception?
        }
    }
}
