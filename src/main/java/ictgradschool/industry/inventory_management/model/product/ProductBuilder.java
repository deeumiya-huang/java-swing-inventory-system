package ictgradschool.industry.inventory_management.model.product;

import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
public class ProductBuilder {
    Product p = new Product();
    private boolean isIdValid, isNameValid, isUnitPriceValid, isStockValid;

    public ProductBuilder(){}

    public ProductBuilder id(String id) {
        try {
            p.setId(id);
            isIdValid = true;
        } catch (IllegalArgumentException e) {
            isIdValid = false;
            throw new BuilderException(e.getMessage(), e);
        }
        return this;
    }

    public ProductBuilder name(String name) {
        try {
            p.setName(name);
            isNameValid = true;
        } catch (IllegalArgumentException e) {
            isNameValid = false;
            throw new BuilderException("Invalid Name", e);
        }
        return this;
    }

    public ProductBuilder description(String description) {
        p.setDescription(description);
        return this;
    }

    public ProductBuilder unitPrice(double unitPrice) {
        try {
            p.setUnitPrice(unitPrice);
            isUnitPriceValid = true;
        } catch (IllegalArgumentException e) {
            isUnitPriceValid = false;
            throw new BuilderException("Invalid Price", e);
        }
        return this;
    }

    public ProductBuilder stock(int stock) {
        try {
            p.setStock(stock);
            isStockValid = true;
        } catch (IllegalArgumentException e) {
            isStockValid = false;
            throw new BuilderException("Invalid Stock", e);
        }
        return this;
    }

    public boolean isValid() {
        return isIdValid && isNameValid && isUnitPriceValid && isStockValid;
    }

    public Product build() throws BuilderException {
        if (!isValid()) {
            throw new BuilderException("Product is not in a valid state. Please fill them in correct way.");
        }
        return p;
    }
}
