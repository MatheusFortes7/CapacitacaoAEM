package br.com.compass.capacitacao.core.dao;

import br.com.compass.capacitacao.core.models.Product;

import java.util.List;

public interface ProductDao {

    List<Product> getAllProducts();
    Product getProductById(int id);
    List<Product> getProductByWord(String word);
    List<Product> getProductByCategory(String category);
    List<Product> getProductByPrice();

    void addProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(int id);
}
