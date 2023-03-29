package br.com.compass.capacitacao.core.dao;

import br.com.compass.capacitacao.core.models.Client;
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
class ClientDaoImplTest {
    @InjectMocks
    private ClientDaoImpl clientDao;

    @Mock
    private DatabaseService databaseService;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement statement;

    @Mock
    private ResultSet resultSet;

    @Test
    @DisplayName("getClientById Should not throw exception when client is found")
    void getClientByIdShouldNotThrowExceptionWhenClientIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test");

        assertDoesNotThrow(() -> clientDao.getClientById(1));
    }

    @Test
    @DisplayName("getClientById Should not throw exception when clients are null")
    void getClientByIdShouldNotThrowExceptionWhenClientAreNull() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertDoesNotThrow(() -> clientDao.getClientById(1));
    }

    @Test
    @DisplayName("getClientById Should throw exception when client is not found")
    void getClientByIdShouldThrowSQLExceptionWhenClientIsNotFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

       assertThrows(RuntimeException.class, () -> clientDao.getClientById(1), "Error getting the client by this id.");
    }

    @Test
    @DisplayName("getAllClients Should not throw exception when clients are found")
    void getAllClientsShouldNotThrowExceptionWhenClientsAreFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt(1)).thenReturn(1);
        when(resultSet.getString(2)).thenReturn("Test");

        assertDoesNotThrow(() -> clientDao.getAllClients());
    }

    @Test
    @DisplayName("getAllClients Should  throw exception when clients are not found")
    void getAllClientsShouldThrowExceptionWhenClientsAreNotFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> clientDao.getAllClients(), "Error getting all clients.");
    }

    @Test
    @DisplayName("addClient Should not throw exception when client is created")
    void addClientShouldNotThrowExceptionWhenClientIsAdded() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);


        assertDoesNotThrow(() -> clientDao.addClient(new Client()));
    }

    @Test
    @DisplayName("addClient Should throw exception when client is not created")
    void addClientShouldThrowExceptionWhenClientIsNotAdded() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> clientDao.addClient(new Client()), "Error adding client.");
    }

    @Test
    @DisplayName("updateClient Should not throw exception when client is updated")
    void updateClientShouldNotThrowExceptionWhenClientIsUpdated() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);


        assertDoesNotThrow(() -> clientDao.updateClient(new Client()));
    }

    @Test
    @DisplayName("updateClient Should throw exception when client is not updated")
    void updateClientShouldThrowExceptionWhenClientIsNotUpdated() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> clientDao.updateClient(new Client()), "Error updating client.");
    }

    @Test
    @DisplayName("deleteClient Should not throw exception when client is deleted")
    void deleteClientShouldNotThrowExceptionWhenClientIsDeleted() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);


        assertDoesNotThrow(() -> clientDao.deleteClient(1));
    }

    @Test
    @DisplayName("deleteClient Should throw exception when client is not deleted")
    void deleteClientShouldThrowExceptionWhenClientIsNotDeleted() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> clientDao.deleteClient(1), "Error deleting client.");
    }
}