package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.models.Product;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public interface ProductService {

    void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;
    void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;
    void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;
    void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;

    List<Product> getAllProduct();
    Product getProductById(int id);
    List<Product> getProductByWord(String name);
    List<Product> getProductByCategory(String category);
    List<Product> getProductByPrice();
    void addProduct(SlingHttpServletRequest request, SlingHttpServletResponse response);
    void updateProduct(SlingHttpServletRequest request, SlingHttpServletResponse response);
    void deleteProduct(SlingHttpServletRequest request, SlingHttpServletResponse response);

    String strToJson(Object obj);

}
