package br.com.compass.capacitacao.core.dao;

import br.com.compass.capacitacao.core.models.Product;
import br.com.compass.capacitacao.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component(service = ProductDao.class, immediate = true)
public class ProductDaoImpl implements ProductDao{

    DatabaseService databaseService;
    @Override
    public List<Product> getAllProducts() {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM produto";
            List<Product> products = null;
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        Product product = new Product();
                        product.setId(resultSet.getInt("id"));
                        product.setName(resultSet.getString("name"));
                        product.setCategory(resultSet.getString("category"));
                        product.setPrice(resultSet.getFloat("price"));
                        products.add(product);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error getting all products.");
            }
            return products;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Product getProductById(int id) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM produto WHERE id = ?";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        Product product = new Product();
                        product.setId(resultSet.getInt("id"));
                        product.setName(resultSet.getString("name"));
                        product.setCategory(resultSet.getString("category"));
                        product.setPrice(resultSet.getFloat("price"));
                        return product;
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error getting the product by this id.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    @Override
    public List<Product> getProductByWord(String word) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM produto WHERE name LIKE ?";
            List<Product> products = null;
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, word);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        Product product = new Product();
                        product.setId(resultSet.getInt("id"));
                        product.setName(resultSet.getString("name"));
                        product.setCategory(resultSet.getString("category"));
                        product.setPrice(resultSet.getFloat("price"));
                        products.add(product);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error getting the product by this word.");
            }
            return products;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM produto WHERE category = ?";
            List<Product> products = null;
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, category);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        Product product = new Product();
                        product.setId(resultSet.getInt("id"));
                        product.setName(resultSet.getString("name"));
                        product.setCategory(resultSet.getString("category"));
                        product.setPrice(resultSet.getFloat("price"));
                        products.add(product);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error getting the product by this category.");
            }
            return products;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Product> getProductByPrice(float price) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM produto ORDER BY price ASC";
            List<Product> products = null;
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setFloat(1, price);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        Product product = new Product();
                        product.setId(resultSet.getInt("id"));
                        product.setName(resultSet.getString("name"));
                        product.setCategory(resultSet.getString("category"));
                        product.setPrice(resultSet.getFloat("price"));
                        products.add(product);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error getting the product by this price.");
            }
            return products;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void addProduct(Product product) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO produto (name, category, price) VALUES (?, ?, ?)";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, product.getName());
                statement.setString(2, product.getCategory());
                statement.setFloat(3, product.getPrice());
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error adding a new product.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateProduct(Product product) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "UPDATE produto SET name = ?, category = ?, price = ? WHERE id = ?";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, product.getName());
                statement.setString(2, product.getCategory());
                statement.setFloat(3, product.getPrice());
                statement.setInt(4, product.getId());
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error updating the product.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteProduct(int id) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM produto WHERE id = ?";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error deleting the product.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
