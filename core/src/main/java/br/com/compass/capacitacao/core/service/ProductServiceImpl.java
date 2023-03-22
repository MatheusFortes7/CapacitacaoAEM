package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.dao.NoteDao;
import br.com.compass.capacitacao.core.dao.ProductDao;
import br.com.compass.capacitacao.core.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.tika.io.IOUtils;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class ProductServiceImpl implements ProductService {

    @Reference
    private DatabaseService databaseService;

    @Reference
    private ProductDao productDao;
    @Reference
    private NoteDao noteDao;

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String idString = request.getParameter("id");
        String name = request.getParameter("name");
        String category = request.getParameter("category");
        int id = 0;
        if(idString != null || !idString.isEmpty()){
            try{
                id = Integer.parseInt(idString);
            } catch (Exception e){
                throw new RuntimeException("Id must be a number");
            }
            response.getWriter().write(strToJson(getProductById(id)));
        }else if(name != null || !name.isEmpty()){
            try{
                response.getWriter().write(strToJson(getProductByName(name)));
            } catch (Exception e){
                throw new RuntimeException("Name must be a string");
            }
        }else if(category != null || !category.isEmpty()){
            try{
                response.getWriter().write(strToJson(getProductByCategory(category)));
            } catch (Exception e){
                throw new RuntimeException("Category must be a string");
            }
        }else{
            try{
                response.getWriter().write(strToJson(getAllProduct()));
            } catch (Exception e){
                throw new RuntimeException("Error");
            }
        }
    }

    @Override
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        addProduct(request, response);
    }

    @Override
    public void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        deleteProduct(request, response);
    }

    @Override
    public void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        updateProduct(request, response);
    }

    @Override
    public List<Product> getAllProduct() {
        List<Product> products = productDao.getAllProducts();
        return products;
    }

    @Override
    public Product getProductById(int id) {
        Product product = productDao.getProductById(id);
        return product;
    }

    @Override
    public List<Product> getProductByName(String name) {
        List<Product> products = productDao.getProductByWord(name);
        return products;
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        List<Product> products = productDao.getProductByCategory(category);
        return products;
    }

    @Override
    public List<Product> getProductByPrice(float price) {
        List<Product> products = productDao.getProductByPrice(price);
        return products;
    }

    @Override
    public void addProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try{
            BufferedReader reader = request.getReader();
            Type type = new TypeToken<Product>(){}.getType();
            List<Product> products = null;
            try{
                products = new Gson().fromJson(reader, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(Product product : products) {
                if(product.getName() == null || product.getName().equals("")){
                    throw new RuntimeException("Name is required");
                } else if(product.getCategory() == null || product.getCategory().equals("")){
                    throw new RuntimeException("Category is required");
                } else {
                    productDao.addProduct(product);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        String user = null;
        try{
            user = IOUtils.toString(request.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Product product;
        try{
            product = new Gson().fromJson(user, Product.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(product.getId() == 0){
            throw new RuntimeException("Id is required");
        } else if(product.getName() == null || product.getName().isEmpty()){
            throw new RuntimeException("Name is required");
        } else if(product.getCategory() == null || product.getCategory().isEmpty()){
            throw new RuntimeException("Category is required");
        } else if(productDao.getProductById(product.getId()) == null){
            throw new RuntimeException("Product not found");
        } else {
            productDao.updateProduct(product);
        }
    }

    @Override
    public void deleteProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try{
            BufferedReader reader = request.getReader();
            Type type = new TypeToken<Product>(){}.getType();
            List<Product> products = null;
            try{
                products = new Gson().fromJson(reader, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
            for(Product product : products) {
                if(productDao.getProductById(product.getId()) == null){
                    throw new RuntimeException("Product not found");
                } else {
                    if(noteDao.getNoteByProductId(product.getId()) != null)
                        noteDao.deleteNoteByProductId(product.getId());
                    productDao.deleteProduct(product.getId());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
