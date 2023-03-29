package br.com.compass.capacitacao.core.dao;

import br.com.compass.capacitacao.core.models.Product;
import br.com.compass.capacitacao.core.service.DatabaseService;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ProductDaoImplTest {
    @InjectMocks
    private ProductDaoImpl productDao;

    @Mock
    private DatabaseService databaseService;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement statement;

    @Mock
    private ResultSet resultSet;

    @Test
    @DisplayName("getAllProducts Should not throw exception when products are found")
    void getAllProductsShouldNotThrowExceptionWhenProductsAreFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test");
        when(resultSet.getString("category")).thenReturn("Test");
        when(resultSet.getFloat("price")).thenReturn(1.0F);

        assertDoesNotThrow(() -> productDao.getAllProducts());
    }

    @Test
    @DisplayName("getAllProducts Should not throw exception when no products are found")
    void getAllProductsShouldNotThrowExceptionWhenNoProductsAreFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertDoesNotThrow(() -> productDao.getAllProducts());
    }

    @Test
    @DisplayName("getAllProducts Should throw exception when connection can't be done")
    void getAllProductsShouldThrowExceptionWhenConnectionCantBeDone() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> productDao.getAllProducts(), "Error while getting all products");
    }

    @Test
    @DisplayName("getProductById Should not throw exception when product is found")
    void getProductByIdShouldNotThrowExceptionWhenProductIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test");
        when(resultSet.getString("category")).thenReturn("Test");
        when(resultSet.getFloat("price")).thenReturn(1.0F);

        assertDoesNotThrow(() -> productDao.getProductById(1));
    }

    @Test
    @DisplayName("getProductById Should not throw exception when product is not found")
    void getProductByIdShouldNotThrowExceptionWhenProductIsNotFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertDoesNotThrow(() -> productDao.getProductById(1));
    }

    @Test
    @DisplayName("getProductById Should throw exception when connection can't be done")
    void getProductByIdShouldThrowExceptionWhenConnectionCantBeDone() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> productDao.getProductById(1), "Error while getting product by id");
    }

    @Test
    @DisplayName("getProductByWord Should not throw exception when product is found")
    void getProductByWordShouldNotThrowExceptionWhenProductIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test");
        when(resultSet.getString("category")).thenReturn("Test");
        when(resultSet.getFloat("price")).thenReturn(1.0F);

        assertDoesNotThrow(() -> productDao.getProductByWord("Test"));
    }

    @Test
    @DisplayName("getProductByWord Should not throw exception when product is not found")
    void getProductByWordShouldNotThrowExceptionWhenProductIsNotFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertDoesNotThrow(() -> productDao.getProductByWord("Test"));
    }

    @Test
    @DisplayName("getProductByWord Should throw exception when connection can't be done")
    void getProductByWordShouldThrowExceptionWhenConnectionCantBeDone() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> productDao.getProductByWord("Test"), "Error while getting product by word");
    }

    @Test
    @DisplayName("getProductByCategory Should not throw exception when product is found")
    void getProductByCategoryShouldNotThrowExceptionWhenProductIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test");
        when(resultSet.getString("category")).thenReturn("Test");
        when(resultSet.getFloat("price")).thenReturn(1.0F);

        assertDoesNotThrow(() -> productDao.getProductByCategory("Test"));
    }

    @Test
    @DisplayName("getProductByCategory Should not throw exception when product is not found")
    void getProductByCategoryShouldNotThrowExceptionWhenProductIsNotFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertDoesNotThrow(() -> productDao.getProductByCategory("Test"));
    }

    @Test
    @DisplayName("getProductByCategory Should throw exception when connection can't be done")
    void getProductByCategoryShouldThrowExceptionWhenConnectionCantBeDone() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> productDao.getProductByCategory("Test"), "Error while getting product by category");
    }

    @Test
    @DisplayName("getProductByPrice Should not throw exception when product is found")
    void getProductByPriceShouldNotThrowExceptionWhenProductIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test");
        when(resultSet.getString("category")).thenReturn("Test");
        when(resultSet.getFloat("price")).thenReturn(1.0F);

        assertDoesNotThrow(() -> productDao.getProductByPrice());
    }

    @Test
    @DisplayName("getProductByPrice Should throw exception when connection can't be done")
    void getProductByPriceShouldThrowExceptionWhenConnectionCantBeDone() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> productDao.getProductByPrice(), "Error while getting product by price");
    }

    @Test
    @DisplayName("addProduct Should not throw exception when product is added")
    void addProductShouldNotThrowExceptionWhenProductIsAdded() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> productDao.addProduct(new Product()));
    }

    @Test
    @DisplayName("addProduct Should not throw exception when product is not added")
    void addProductShouldThrowExceptionWhenProductIsNotAdded() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertDoesNotThrow(() -> productDao.addProduct(new Product()));
    }

    @Test
    @DisplayName("addProduct Should throw exception when connection can't be done")
    void addProductShouldThrowExceptionWhenConnectionCantBeDone() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> productDao.addProduct(new Product()), "Error while adding product");
    }

    @Test
    @DisplayName("updateProduct Should not throw exception when product is updated")
    void updateProductShouldNotThrowExceptionWhenProductIsUpdated() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> productDao.updateProduct(new Product()));
    }

    @Test
    @DisplayName("updateProduct Should not throw exception when product is not updated")
    void updateProductShouldThrowExceptionWhenProductIsNotUpdated() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertDoesNotThrow(() -> productDao.updateProduct(new Product()));
    }

    @Test
    @DisplayName("updateProduct Should throw exception when connection can't be done")
    void updateProductShouldThrowExceptionWhenConnectionCantBeDone() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> productDao.updateProduct(new Product()), "Error while updating product");
    }

    @Test
    @DisplayName("deleteProduct Should not throw exception when product is deleted")
    void deleteProductShouldNotThrowExceptionWhenProductIsDeleted() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> productDao.deleteProduct(1));
    }

    @Test
    @DisplayName("deleteProduct Should not throw exception when product is not deleted")
    void deleteProductShouldThrowExceptionWhenProductIsNotDeleted() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        assertDoesNotThrow(() -> productDao.deleteProduct(1));
    }

    @Test
    @DisplayName("deleteProduct Should throw exception when connection can't be done")
    void deleteProductShouldThrowExceptionWhenConnectionCantBeDone() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> productDao.deleteProduct(1), "Error while deleting product");
    }

}