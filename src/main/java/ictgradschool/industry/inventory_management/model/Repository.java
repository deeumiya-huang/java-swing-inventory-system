package ictgradschool.industry.inventory_management.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Repository implements Iterable<Product>{
    // for quick search by id
//    private final Hashtable<String, Product> products;
    private final ConcurrentHashMap<String, Product> products;
    // for index search
    private final List<Product> indexedProducts;

    private final List<RepositoryListener> listeners;

    public Repository() {
        products = new ConcurrentHashMap<>();
        indexedProducts = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public void addProduct(Product product) {
        // todo: if the product ID already exist, modify to the new one, or reject?
        if(products.containsKey(product.getId())) {
            indexedProducts.remove(products.get(product.getId()));
        }
        products.put(product.getId(), product);
        indexedProducts.add(product);
    }

    public void removeProductAt(Product product) {
        products.remove(product.getId());
        indexedProducts.remove(product);

        for (RepositoryListener listener : listeners) {
            listener.repositoryHasChanged(this);
        }
    }

    public Product getProduct(String productID) {
        return products.get(productID);
    }

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
}
