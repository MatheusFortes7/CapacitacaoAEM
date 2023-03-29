package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.dao.NoteDao;
import br.com.compass.capacitacao.core.dao.ProductDao;
import br.com.compass.capacitacao.core.models.Product;
import br.com.compass.capacitacao.core.utils.ResponseContent;
import com.google.gson.Gson;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ProductServiceImplTest {

    @Mock
    private SlingHttpServletRequest request;
    @Mock
    private SlingHttpServletResponse response;

    @InjectMocks
    private ProductService productService = new ProductServiceImpl();

    @Mock
    private ProductDao productDaoMock;

    @Mock
    private NoteDao noteDaoMock;

    SlingHttpServletResponse realResponse;
    SlingHttpServletRequest realRequest;

    @Mock
    ResponseContent responseContent;

    @BeforeEach
    void setUp(AemContext context) {
        MockitoAnnotations.openMocks(this);

        realResponse = context.response();
        realRequest = context.request();
    }

    @Test
    @DisplayName("doGet should not throw exception all products are returned")
    void doGetAllProductsShouldReturnAllProducts() throws IOException {
        when(productDaoMock.getAllProducts()).thenReturn(getAllProducts());

        PrintWriter writer = realResponse.getWriter();
        assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when json has a error")
    void doGetAllProductsShouldNotThrowExceptionWhenJsonHasError() throws IOException {
        when(productDaoMock.getAllProducts()).thenReturn(getAllProducts());

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when all products is zero")
    void doGetShouldNotThrowExceptionWhenAllProductsIsZero(){
        when(productDaoMock.getAllProducts()).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when id is not a number")
    void doGetShouldNotThrowExceptionWhenIdIsNotANumber(){
        when(request.getParameter("id")).thenReturn("a");
        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when product is not found")
    void doGetShouldNotThrowExceptionWhenProductIsNotFound(){
        when(request.getParameter("id")).thenReturn("1");

        when(productDaoMock.getProductById(1)).thenReturn(null);

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when product is found")
    void doGetShouldNotThrowExceptionWhenProductIsFound() throws IOException {
        when(request.getParameter("id")).thenReturn("1");

        when(productDaoMock.getProductById(1)).thenReturn(getAllProducts().get(0));
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when product name is correct")
    void doGetShouldNotThrowExceptionWhenProductNameIsCorrect() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn("Product 1");

        when(productDaoMock.getProductByWord("Product 1")).thenReturn(getAllProducts());;
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when product name is not correct")
    void doGetShouldNotThrowExceptionWhenProductNameIsNotCorrect() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn("Product 4");

        when(productDaoMock.getProductByWord("Product 4")).thenReturn(new ArrayList<>());;

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when Json cant be done")
    void doGetShouldNotThrowExceptionWhenJsonCantBeDone() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn("Product4");

        when(productDaoMock.getProductByWord("Product4")).thenReturn(getAllProducts());;

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when product by category is found")
    void doGetShouldNouThrowExceptionWhenProductByCategoryIsFound() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("category")).thenReturn("Category 1");

        when(productDaoMock.getProductByCategory("Category 1")).thenReturn(getAllProducts());;
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when product by category is not found")
    void doGetShouldNouThrowExceptionWhenProductByCategoryIsNotFound() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("category")).thenReturn("Category 4");

        when(productDaoMock.getProductByCategory("Category 4")).thenReturn(new ArrayList<>());;

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when Json cant be done")
    void doGetShouldNotThrowExceptionWhenHasErrorInJson() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("category")).thenReturn("Category 4");

        when(productDaoMock.getProductByCategory("Category 4")).thenReturn(getAllProducts());

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when price is correct")
    void doGetShouldNotThrowExceptionWhenPriceIsCorrect() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("price")).thenReturn("10.0");

        when(productDaoMock.getProductByPrice()).thenReturn(getAllProducts());;
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when there is no products")
    void doGetShouldNotThrowExceptionWhenPriceIsNotCorrect() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("price")).thenReturn("40.0");

        when(productDaoMock.getProductByPrice()).thenReturn(new ArrayList<>());;

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when price json has error")
    void doGetShouldNotThrowExceptionWhenPriceJsonHasError() throws IOException {
        when(request.getParameter("id")).thenReturn(null);
        when(request.getParameter("name")).thenReturn(null);
        when(request.getParameter("category")).thenReturn(null);
        when(request.getParameter("price")).thenReturn("10.0");

        when(productDaoMock.getProductByPrice()).thenReturn(getAllProducts());;

        assertDoesNotThrow(() -> productService.doGet(request, response));
    }

    @Test
    @DisplayName("doPost should not throw exception when product is added")
    void doPostShouldNotThrowExceptionWhenProductIsAdded() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());
        doNothing().when(productDaoMock).addProduct(any());

        assertDoesNotThrow(() -> doCallRealMethod().when(responseContent).FinalMesage(anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doPost(request, response));
    }

    @Test
    @DisplayName("doPost should not throw exception when name is missing")
    void doPostShouldNotThrowExceptionWhenNameIsMissing() throws IOException {
        final Product product = new Product(4, null, "Description 4", 40.0F);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        assertDoesNotThrow(() -> doCallRealMethod().when(responseContent).FinalMesage(anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doPost(request, response));
    }

    @Test
    @DisplayName("doPost should not throw exception when category is missing")
    void doPostShouldNotThrowExceptionWhenCategoryIsMissing() throws IOException {
        final Product product = new Product(4, "Product 4", null, 40.0F);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        assertDoesNotThrow(() -> doCallRealMethod().when(responseContent).FinalMesage(anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doPost(request, response));
    }

    @Test
    @DisplayName("doPost should not throw exception when price is missing")
    void doPostShouldNotThrowExceptionWhenPriceIsMissing() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 0);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        assertDoesNotThrow(() -> doCallRealMethod().when(responseContent).FinalMesage(anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doPost(request, response));
    }

    @Test
    @DisplayName("doPost should not throw exception when has a error in json")
    void doPostShouldNotThrowExceptionWhenHasAErrorInJson() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[aa");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        assertDoesNotThrow(() -> doCallRealMethod().when(responseContent).FinalMesage(anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doPost(request, response));
    }

    @Test
    @DisplayName("doPost should throw exception when FinalMesaage has a error")
    void doPostShouldThrowExceptionWhenFinalMesaageHasAError() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        doThrow(IOException.class).when(responseContent).FinalMesage(anyInt(),anyString(),any());

        assertThrows(RuntimeException.class, () -> productService.doPost(request, response));
    }

    @Test
    @DisplayName("doPut should not throw exception when product is deleted")
    void doDeleteShouldNotThrowExceptionWhenProductIsDeleted() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());
        when(productDaoMock.getProductById(anyInt())).thenReturn(product);
        when(noteDaoMock.getNoteByProductId(anyInt())).thenReturn(null);

        assertDoesNotThrow(() -> doCallRealMethod().when(responseContent).FinalMesage(anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doDelete(request, response));
    }

    @Test
    @DisplayName("doPut should not throw exception when notes are found")
    void doDeleteShouldNotThrowExceptionWhenNotesAreFound() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());
        when(productDaoMock.getProductById(anyInt())).thenReturn(product);
        when(noteDaoMock.getNoteByProductId(anyInt())).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> doCallRealMethod().when(responseContent).FinalMesage(anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> productService.doDelete(request, response));
    }

    @Test
    @DisplayName("doPut should not throw exception when product is not found")
    void doDeleteShouldNotThrowExceptionWhenProductIsNotFound() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());
        when(productDaoMock.getProductById(anyInt())).thenReturn(null);

        assertDoesNotThrow(() -> productService.doDelete(request, response));
    }

    @Test
    @DisplayName("doPut should not throw exception when json is wrong")
    void doDeleteShouldNotThrowExceptionWhenJsonIsWrong() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[aa");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        assertDoesNotThrow(() -> productService.doDelete(request, response));
    }

    @Test
    @DisplayName("doPut should throw exception when FinalMesaage has a error")
    void doDeleteShouldThrowExceptionWhenFinalMesaageHasAError() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String json = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(json);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        doThrow(IOException.class).when(responseContent).FinalMesage(anyInt(),anyString(),any());

        assertThrows(RuntimeException.class, () -> productService.doDelete(request, response));
    }

    @Test
    @DisplayName("doPut should not throw exception when product is updated")
    void doPutShouldNotThrowExceptionWhenProductIsUpdated() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String productJson = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(productJson);

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));

        when(productDaoMock.getProductById(anyInt())).thenReturn(product);

        assertDoesNotThrow(()-> productService.doPut(request, response));
    }

   @Test
   @DisplayName("doPut should not throw exception when product is not found")
   void doPutShouldNotThrowExceptionWhenIdIsMissing() throws IOException {
       final Product product = new Product(0, "Product 4", "Description 4", 40.0F);
       String productJson = new Gson().toJson(product);
       StringBuilder sb = new StringBuilder(productJson);

       Reader reader = new StringReader(sb.toString());
       when(request.getReader()).thenReturn(new BufferedReader(reader));

       assertDoesNotThrow(()-> productService.doPut(request, response));
   }

    @Test
    @DisplayName("doPut should not throw exception when Name is missing")
    void doPutShouldNotThrowExceptionWhenNameIsMissing() throws IOException {
        final Product product = new Product(4, null, "Description 4", 40.0F);
        String productJson = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(productJson);

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));

        assertDoesNotThrow(()-> productService.doPut(request, response));
    }

    @Test
    @DisplayName("doPut should not throw exception when Category is missing")
    void doPutShouldNotThrowExceptionWhenCategoryIsMissing() throws IOException {
        final Product product = new Product(4, "Product", null, 40.0F);
        String productJson = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(productJson);

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));

        assertDoesNotThrow(()-> productService.doPut(request, response));
    }

    @Test
    void doPutShouldNotThrowExceptionWhenProductIsNotFound() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String productJson = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(productJson);

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));

        when(productDaoMock.getProductById(anyInt())).thenReturn(null);

        assertDoesNotThrow(()-> productService.doPut(request, response));
    }

    @Test
    @DisplayName("doPut should throw exception when IOUtils throws exception")
    void doPutShouldThrowExceptionWhenIOUtilsThrowException() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String productJson = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(productJson);

        when(request.getReader()).thenThrow(IOException.class);

        assertThrows(RuntimeException.class,()-> productService.doPut(request, response));
    }

    @Test
    @DisplayName("doPut should throw exception when FinalMesaage has a error")
    void doPutShouldThrowExceptionWhenFinalMesaageHasAError() throws IOException {
        final Product product = new Product(4, "Product 4", "Description 4", 40.0F);
        String productJson = new Gson().toJson(product);
        StringBuilder sb = new StringBuilder(productJson);

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));

        doThrow(IOException.class).when(responseContent).FinalMesage(anyInt(),anyString(),any());

        assertThrows(RuntimeException.class,()-> productService.doPut(request, response));
    }

    List<Product> getAllProducts(){
        Product product = new Product(1, "Product 1", "Description 1", 10.0F);
        Product product2 = new Product(2, "Product 2", "Description 2", 20.0F);
        Product product3 = new Product(3, "Product 3", "Description 3", 30.0F);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product2);
        products.add(product3);
        return products;
    }


}