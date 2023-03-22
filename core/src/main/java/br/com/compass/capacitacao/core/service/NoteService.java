package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.models.Note;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public interface NoteService {

    void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;

    List<Note> getAllNote();
    List<Note> getNoteByClientId(int id);

    String strToJson(Object obj);
}
