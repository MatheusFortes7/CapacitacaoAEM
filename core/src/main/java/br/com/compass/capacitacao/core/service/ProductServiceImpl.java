package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.dao.NoteDao;
import br.com.compass.capacitacao.core.dao.ProductDao;
import br.com.compass.capacitacao.core.models.ErrorMessage;
import br.com.compass.capacitacao.core.models.SucessMessage;
import br.com.compass.capacitacao.core.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.tika.io.IOUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Component(immediate = true, service = ProductService.class)
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
        String price = request.getParameter("price");
        int id = 0;
        if(idString != null){
            try{
                id = Integer.parseInt(idString);
            } catch (Exception e){
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Id must be a number")));
            }
            Product products = getProductById(id);
            if(products == null){
                response.setStatus(404);
                response.getWriter().write(strToJson(new ErrorMessage("No product found")));
            }else{
                response.setStatus(200);
                response.getWriter().write(strToJson(products));
            }
        }else if(name != null ){
            try{
                List<Product> products = getProductByWord(name);
                if(products.size() == 0){
                    response.setStatus(404);
                    response.getWriter().write(strToJson(new ErrorMessage("No product found")));
                }else{
                    response.setStatus(200);
                    response.getWriter().write(strToJson(products));
                }
            } catch (Exception e){
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Name must be a string")));
            }
        }else if(category != null ){
            try{
                List<Product> products = getProductByCategory(category);
                if(products.size() == 0){
                    response.setStatus(404);
                    response.getWriter().write(strToJson(new ErrorMessage("No product found")));
                }else{
                    response.getWriter().write(strToJson(products));
                }
            } catch (Exception e){
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Category must be a string")));
            }
        }else if(price != null) {
            try{
                List<Product> products = getProductByPrice();
                if(products.size() == 0){
                    response.setStatus(404);
                    response.getWriter().write(strToJson(new ErrorMessage("No product found")));
                }else{
                    response.getWriter().write(strToJson(products));
                }
            } catch (Exception e){
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Price must be a number")));
            }
        }else{
                try{
                    List<Product> products = getAllProduct();
                    if(products.size() == 0){
                        response.setStatus(404);
                        response.getWriter().write(strToJson(new ErrorMessage("No product found")));
                    }else{
                        response.setStatus(200);
                        response.getWriter().write(strToJson(products));
                    }
                } catch (Exception e){
                    response.setStatus(400);
                    response.getWriter().write(strToJson(new ErrorMessage("Error")));
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
    public List<Product> getProductByWord(String name) {
        List<Product> products = productDao.getProductByWord(name);
        return products;
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        List<Product> products = productDao.getProductByCategory(category);
        return products;
    }

    @Override
    public List<Product> getProductByPrice() {
        List<Product> products = productDao.getProductByPrice();
        return products;
    }

    @Override
    public void addProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try{
            BufferedReader reader = request.getReader();
            Type type = new TypeToken<List<Product>>(){}.getType();//erro de tipo
            List<Product> products = null;
            try{
                products = new Gson().fromJson(reader, type);
            } catch (Exception e) {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("json error")));
                return;
            }
            try{
                for(Product product : products) {
                    if(product.getName() == null || product.getName().equals("")){
                        response.setStatus(400);
                        response.getWriter().write(strToJson(new ErrorMessage("Name is required")));
                    } else if(product.getCategory() == null || product.getCategory().equals("")){
                        response.setStatus(400);
                        response.getWriter().write(strToJson(new ErrorMessage("Category is required")));
                    } else if(product.getPrice() == 0){
                        response.setStatus(400);
                        response.getWriter().write(strToJson(new ErrorMessage("Price is required")));
                    } else {
                        productDao.addProduct(product);
                        response.setStatus(201);
                        response.getWriter().write(strToJson(new SucessMessage("Product added")));
                    }
                }
            } catch (Exception e) {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Error")));
            }
        } catch (IOException e) {
            try {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("json error")));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
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
            if(product.getId() == 0){
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Id is required")));
            } else if(product.getName() == null || product.getName().isEmpty()){
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Name is required")));
            } else if(product.getCategory() == null || product.getCategory().isEmpty()){
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Category is required")));
            } else if(productDao.getProductById(product.getId()) == null){
                response.setStatus(404);
                response.getWriter().write(strToJson(new ErrorMessage("Product not found")));
            } else {
                productDao.updateProduct(product);
                response.setStatus(202);
                response.getWriter().write(strToJson(new SucessMessage("Product updated")));
            }
        } catch (Exception e) {
            try {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("json error")));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void deleteProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try{
            BufferedReader reader = request.getReader();
            Type type = new TypeToken<List<Product>>(){}.getType();
            List<Product> products = null;
            try{
                products = new Gson().fromJson(reader, type);
            } catch (Exception e) {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("json error")));
                return;
            }
            for(Product product : products) {
                try{
                    if(productDao.getProductById(product.getId()) == null){
                        response.setStatus(404);
                        response.getWriter().write(strToJson(new ErrorMessage("Product not found")));
                    } else {
                        if(noteDao.getNoteByProductId(product.getId()) != null)
                            noteDao.deleteNoteByProductId(product.getId());
                        productDao.deleteProduct(product.getId());
                        response.setStatus(202);
                        response.getWriter().write(strToJson(new SucessMessage("Product deleted")));
                    }
                } catch (Exception e) {
                    response.setStatus(400);
                    response.getWriter().write(strToJson(new ErrorMessage("Error")));
                }
            }
        } catch (IOException e) {
            try {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("json error")));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
