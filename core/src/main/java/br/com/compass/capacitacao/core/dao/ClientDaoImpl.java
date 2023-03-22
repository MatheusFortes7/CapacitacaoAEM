package br.com.compass.capacitacao.core.dao;

import br.com.compass.capacitacao.core.models.Client;
import br.com.compass.capacitacao.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component(service = ClientDao.class, immediate = true)
public class ClientDaoImpl implements ClientDao{

    @Reference
    private DatabaseService databaseService;

    public Client getClientById(int id) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM cliente WHERE id = ?";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        Client client = new Client();
                        client.setId(resultSet.getInt("id"));
                        client.setName(resultSet.getString("name"));
                        return client;
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error getting the client by this id.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return null;
    }

    public List<Client> getAllClients() {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM cliente";
            List<Client> clients = null;
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        Client client = new Client();
                        client.setId(resultSet.getInt("id"));
                        client.setName(resultSet.getString("name"));
                        clients.add(client);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error getting all clients.");
            }
            return clients;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void addClient(Client client) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO cliente (name) VALUES (?)";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, client.getName());
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error adding a new client.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void updateClient(Client client) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "UPDATE cliente SET name = ? WHERE id = ?";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, client.getName());
                statement.setInt(2, client.getId());
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error updating this client.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void deleteClient(int id) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM cliente WHERE id = ?";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error deleting this client.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}