package br.com.compass.capacitacao.core.dao;



import br.com.compass.capacitacao.core.models.Note;

import java.util.List;

public interface NoteDao {

    List<Note> getNoteByClientId(int id);
    List<Note> getNoteByProductId(int id);
    List<Note> getAllNotes();

    void deleteNoteByClientId(int id);
    void deleteNoteByProductId(int id);
}
