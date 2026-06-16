package ictgradschool.industry.inventory_management.model;

import ictgradschool.industry.inventory_management.admin.FilestoreManager;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Repository implements Iterable<Product>{
    private final File file;
    // for quick search by id
    private final ConcurrentHashMap<String, Product> products;
    // for index search
    private final List<Product> indexedProducts;

    private final List<RepositoryListener> listeners;

    public Repository(File file) {
        this.file = file;
        products = new ConcurrentHashMap<>();
        indexedProducts = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public void save(){
        FilestoreManager.saveData(this.indexedProducts, this.file);
    }

    //todo: check this.

    /* return -1 when load data failed, return 1 when success. */
    public int loadData() {
        List<Product> data = FilestoreManager.readData(this.file);
        if (data == null) {
            return -1;
        } else {
            // Populate products list with the loaded data.
            for (Product product : data) {
                addProduct(product);
            }
            return 1;
        }
    }

    public void addProduct(Product product) {
        // if the product ID already exist, reject it.
        if(products.containsKey(product.getId())) {
            throw new IllegalArgumentException("ID has already been used!");
        }
        products.put(product.getId(), product);
        indexedProducts.add(product);

        notifyListener();
    }

    public void removeProductBy(String productId) {
        Product removedProduct = products.remove(productId);
        if (removedProduct != null) {
            indexedProducts.remove(removedProduct);
            notifyListener();
        }
    }

    public Product getProduct(String productID) {
        return products.get(productID);
    }
    // todo: delete it if not used finally
    public List<Product> getAllProducts() { return indexedProducts;}

    public Product getProductAt(int index) {
        if (index < 0 || index >= products.size()) {
            return null;
        } else {
            return indexedProducts.get(index);
        }
    }

    @Override
    public Iterator<Product> iterator() {
        return indexedProducts.iterator();
    }

    public int size() { return products.size();}

    public void addRepositoryListener (RepositoryListener listener) {
        listeners.add(listener);
    }

    public void removeRepositoryListener (RepositoryListener listener) {
        listeners.remove(listener);
    }

    public void notifyListener() {
        for (RepositoryListener listener : listeners) {
            listener.repositoryHasChanged(this);
        }
    }
}
