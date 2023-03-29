package br.com.compass.capacitacao.core.dao;

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
class NoteDaoImplTest {

    @InjectMocks
    private NoteDaoImpl noteDao;

    @Mock
    private DatabaseService databaseService;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement statement;

    @Mock
    private ResultSet resultSet;

    @Test
    @DisplayName("getAllNotes Should not throw exception when notes are found")
    void getAllNotesShouldNotThrowExceptionWhenNotesAreFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("number")).thenReturn(1);
        when(resultSet.getInt("idproduct")).thenReturn(1);
        when(resultSet.getInt("idclient")).thenReturn(1);
        when(resultSet.getFloat("price")).thenReturn(1.0F);

        assertDoesNotThrow(() -> noteDao.getAllNotes());
    }

    @Test
    @DisplayName("getAllNotes Should throw exception when no notes are found")
    void getAllNotesShouldThrowExceptionWhenNoNotesAreFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);


        assertThrows(RuntimeException.class, () -> noteDao.getAllNotes(), "Error while getting all notes");

    }

    @Test
    @DisplayName("getNoteByClientId Should not throw exception when note is found")
    void getNoteByClientIdShouldNotThrowExceptionWhenNoteIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("number")).thenReturn(1);
        when(resultSet.getInt("idproduct")).thenReturn(1);
        when(resultSet.getInt("idclient")).thenReturn(1);
        when(resultSet.getFloat("price")).thenReturn(1.0F);

        assertDoesNotThrow(() -> noteDao.getNoteByClientId(1));
    }

    @Test
    @DisplayName("getNoteByProductId Should not throw exception when note is found")
    void getNoteByProductIdShouldNotThrowExceptionWhenNoteIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("number")).thenReturn(1);
        when(resultSet.getInt("idproduct")).thenReturn(1);
        when(resultSet.getInt("idclient")).thenReturn(1);
        when(resultSet.getFloat("price")).thenReturn(1.0F);

        assertDoesNotThrow(() -> noteDao.getNoteByProductId(1));
    }

    @Test
    @DisplayName("getNoteByClientId Should throw exception when no note is found")
    void getNoteByClientIdShouldThrowExceptionWhenNoNoteIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);


        assertThrows(RuntimeException.class, () -> noteDao.getNoteByClientId(1), "Error while getting note by client id");
    }

    @Test
    @DisplayName("getNoteByProductId Should throw exception when no note is found")
    void getNoteByProductIdShouldThrowExceptionWhenNoNoteIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThrows(RuntimeException.class, () -> noteDao.getNoteByProductId(1), "Error while getting note by product id");

    }

    @Test
    @DisplayName("deleteNoteByClientId Should not throw exception when note is found")
    void deleteNoteByClientIdShouldNotThrowExceptionWhenNoteIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> noteDao.deleteNoteByClientId(1));
    }

    @Test
    @DisplayName("deleteNoteByProductId Should not throw exception when note is found")
    void deleteNoteByProductIdShouldNotThrowExceptionWhenNoteIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(1);

        assertDoesNotThrow(() -> noteDao.deleteNoteByProductId(1));
    }

    @Test
    @DisplayName("deleteNoteByClientId Should throw exception when no note is found")
    void deleteNoteByClientIdShouldThrowExceptionWhenNoNoteIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);


        assertThrows(RuntimeException.class, () -> noteDao.deleteNoteByClientId(1), "Error while deleting note by client id");

    }

    @Test
    @DisplayName("deleteNoteByProductId Should throw exception when no note is found")
    void deleteNoteByProductIdShouldThrowExceptionWhenNoNoteIsFound() throws SQLException {
        when(databaseService.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenThrow(SQLException.class);


        assertThrows(RuntimeException.class, () -> noteDao.deleteNoteByProductId(1), "Error while deleting note by product id");
    }

}