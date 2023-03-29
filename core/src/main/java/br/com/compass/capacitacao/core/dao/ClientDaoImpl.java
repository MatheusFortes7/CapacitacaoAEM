package br.com.compass.capacitacao.core.dao;

import br.com.compass.capacitacao.core.models.Client;
import br.com.compass.capacitacao.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
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
            }
        } catch (SQLException ex) {
            throw new RuntimeException((ex) + " Error getting the client by this id.");
        }
        return null;
    }

    public List<Client> getAllClients() {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM cliente";
            List<Client> clients = new LinkedList<>();
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet resultSet = statement.executeQuery()) {
                    while(resultSet.next()) {
                        Client client = new Client(resultSet.getInt(1), resultSet.getString(2));
                        clients.add(client);
                    }
                }
            }
            return clients;
        } catch (SQLException ex) {
            throw new RuntimeException(ex + " Error getting all clients.");
        }
    }

    public void addClient(Client client) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "INSERT INTO cliente (name) VALUES (?)";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, client.getName());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException((ex) + " Error adding this client.");
        }
    }

    public void updateClient(Client client) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "UPDATE cliente SET name = ? WHERE id = ?";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, client.getName());
                statement.setInt(2, client.getId());
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException((ex) + " Error updating this client.");
        }
    }

    public void deleteClient(int id) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM cliente WHERE id = ?";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException((ex) + " Error deleting this client.");
        }
    }
}
