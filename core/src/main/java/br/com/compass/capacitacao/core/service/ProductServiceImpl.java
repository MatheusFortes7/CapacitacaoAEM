package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.dao.NoteDao;
import br.com.compass.capacitacao.core.dao.ProductDao;
import br.com.compass.capacitacao.core.models.Product;
import br.com.compass.capacitacao.core.utils.ResponseContent;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.tika.io.IOUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Component(immediate = true, service = ProductService.class)
public class ProductServiceImpl implements ProductService {

    @Reference
    private DatabaseService databaseService;
    @Reference
    private ResponseContent responseContent = new ResponseContent();
    @Reference
    private ProductDao productDao;
    @Reference
    private NoteDao noteDao;

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        final String idString = request.getParameter("id");
        final String name = request.getParameter("name");
        final String category = request.getParameter("category");
        final String price = request.getParameter("price");
        int id = 0;
        if(idString != null){
            try{
                id = Integer.parseInt(idString);
            } catch (Exception e){
                responseContent.FinalMesage(400, "Id must be a number", response);
            }
            final Product products = getProductById(id);
            if(products == null){
                responseContent.FinalMesage(400, "No product found", response);
            }else{
                responseContent.getRequest(200, response);
                response.getWriter().write(strToJson(products));
            }
        }else if(name != null ){
            try{
                final List<Product> products = getProductByWord(name);
                if(products.size() == 0){
                    responseContent.FinalMesage(400, "No product found", response);
                }else{
                    responseContent.getRequest(200, response);
                    response.getWriter().write(strToJson(products));
                }
            } catch (Exception e){
                responseContent.FinalMesage(400, "Name must be a string", response);
            }
        }else if(category != null ){
            try{
                final List<Product> products = getProductByCategory(category);
                if(products.size() == 0){
                    responseContent.FinalMesage(400, "No product found", response);
                }else{
                    responseContent.getRequest(200, response);
                    response.getWriter().write(strToJson(products));
                }
            } catch (Exception e){
                responseContent.FinalMesage(400, "Category must be a string", response);
            }
        }else if(price != null) {
            try{
                final List<Product> products = getProductByPrice();
                if(products.size() == 0){
                    responseContent.FinalMesage(400, "No product found", response);
                }else{
                    responseContent.getRequest(200, response);
                    response.getWriter().write(strToJson(products));
                }
            } catch (Exception e){
                responseContent.FinalMesage(400, "Price must be a number", response);
            }
        }else{
                try{
                    final List<Product> products = getAllProduct();
                    if(products.size() == 0){
                        responseContent.FinalMesage(400, "No product found", response);
                    }else{
                        responseContent.getRequest(200, response);
                        response.getWriter().write(strToJson(products));
                    }
                } catch (Exception e){
                    responseContent.FinalMesage(400, "Error", response);
                }
        }
    }


    @Override
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        addProduct(request, response);
    }

    @Override
    public void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        deleteProduct(request, response);
    }

    @Override
    public void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        updateProduct(request, response);
    }

    @Override
    public List<Product> getAllProduct() {
        return productDao.getAllProducts();
    }

    @Override
    public Product getProductById(int id) {
        return productDao.getProductById(id);
    }

    @Override
    public List<Product> getProductByWord(String name) {
        return productDao.getProductByWord(name);
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        return productDao.getProductByCategory(category);
    }

    @Override
    public List<Product> getProductByPrice() {
        return productDao.getProductByPrice();
    }

    @Override
    public void addProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try{
            Product[] products = null;
            try{
                products = new Gson().fromJson(responseContent.readJson(request,response), Product[].class);
            } catch (Exception e) {
                responseContent.FinalMesage(400, "Error", response);
                return;
            }
            try{
                for(Product product : products) {
                    if(product.getName() == null || product.getName().equals("")){
                        responseContent.FinalMesage(400, "Name is required", response);
                    } else if(product.getCategory() == null || product.getCategory().equals("")){
                        responseContent.FinalMesage(400, "Category is required", response);
                    } else if(product.getPrice() == 0){
                        responseContent.FinalMesage(400, "Price is required", response);
                    } else {
                        productDao.addProduct(product);
                        responseContent.FinalMesage(200, "Product added", response);
                    }
                }
            } catch (Exception e) {
                responseContent.FinalMesage(400, "Error", response);
            }
        } catch (IOException e) {
            try {
                responseContent.FinalMesage(400, "Error", response);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void updateProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) {
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
                responseContent.FinalMesage(400, "Id is required", response);
            } else if(product.getName() == null || product.getName().isEmpty()){
                responseContent.FinalMesage(400, "Name is required", response);
            } else if(product.getCategory() == null || product.getCategory().isEmpty()){
                responseContent.FinalMesage(400, "Category is required", response);
            } else if(productDao.getProductById(product.getId()) == null){
                responseContent.FinalMesage(400, "Product not found", response);
            } else {
                productDao.updateProduct(product);
                responseContent.FinalMesage(200, "Product updated", response);
            }
        } catch (Exception e) {
            try {
                responseContent.FinalMesage(400, "Error", response);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void deleteProduct(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try{
            Product[] products = null;
            try{
                products = new Gson().fromJson(responseContent.readJson(request,response), Product[].class);
            } catch (Exception e) {
                responseContent.FinalMesage(400, "Error", response);
                return;
            }
            for(Product product : products) {
                try{
                    if(productDao.getProductById(product.getId()) == null){
                        responseContent.FinalMesage(400, "Product not found", response);
                    } else {
                        if(noteDao.getNoteByProductId(product.getId()) != null)
                            noteDao.deleteNoteByProductId(product.getId());
                        productDao.deleteProduct(product.getId());
                        responseContent.FinalMesage(200, "Product deleted", response);
                    }
                } catch (Exception e) {
                    responseContent.FinalMesage(400, "Error", response);
                }
            }
        } catch (IOException e) {
            try {
                responseContent.FinalMesage(400, "Error", response);
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
