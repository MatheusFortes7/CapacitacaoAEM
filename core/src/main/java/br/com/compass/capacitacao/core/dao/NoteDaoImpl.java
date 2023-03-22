package br.com.compass.capacitacao.core.dao;

import br.com.compass.capacitacao.core.models.Note;
import br.com.compass.capacitacao.core.service.DatabaseService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component(service = NoteDao.class, immediate = true)
public class NoteDaoImpl implements NoteDao{

    @Reference
    private DatabaseService databaseService;

    public List<Note> getNoteByClientId(int id) { //TODO: tratamento de exceções
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM nota WHERE idclient = ?";
            List<Note> notes = new LinkedList<>();
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        Note note = new Note();
                        note.setNumber(resultSet.getInt("number"));
                        note.setIdProduct(resultSet.getInt("idproduct"));
                        note.setIdClient(resultSet.getInt("idclient"));
                        note.setPrice(resultSet.getFloat("price"));
                        notes.add(note);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error getting the note by this client id.");
            }
            return notes;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Note> getNoteByProductId(int id) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "SELECT * FROM nota WHERE idproduct = ?";
            List<Note> notes = new LinkedList<>();
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery()) {
                    while(resultSet.next()) {
                        Note note = new Note();
                        note.setNumber(resultSet.getInt("number"));
                        note.setIdProduct(resultSet.getInt("idproduct"));
                        note.setIdClient(resultSet.getInt("idclient"));
                        note.setPrice(resultSet.getFloat("price"));
                        notes.add(note);
                    }
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error getting the note by this client id.");
            }
            return notes;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Note> getAllNotes() { //TODO: tratamento de exceções
            try(Connection connection = databaseService.getConnection()) {
                String sql = "SELECT * FROM nota";
                List<Note> notes = new LinkedList<>();
                try(PreparedStatement statement = connection.prepareStatement(sql)) {
                    try(ResultSet resultSet = statement.executeQuery()) {
                        while (resultSet.next()) {
                            Note note = new Note();
                            note.setNumber(resultSet.getInt("number"));
                            note.setIdProduct(resultSet.getInt("idproduct"));
                            note.setIdClient(resultSet.getInt("idclient"));
                            note.setPrice(resultSet.getFloat("price"));
                            notes.add(note);
                        }
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex.getMessage());
                }
                return notes;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
    }

    @Override
    public void deleteNoteByClientId(int id) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM nota WHERE idclient = ?";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error deleting the note by this client id.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteNoteByProductId(int id) {
        try(Connection connection = databaseService.getConnection()) {
            String sql = "DELETE FROM nota WHERE idproduct = ?";

            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage() + " Error deleting the note by this product id.");
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
