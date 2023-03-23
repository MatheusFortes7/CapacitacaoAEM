package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.dao.NoteDao;
import br.com.compass.capacitacao.core.models.ErrorMessage;
import br.com.compass.capacitacao.core.models.Note;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@Component(immediate = true, service = NoteService.class)
public class NoteServiceImpl implements NoteService{

    @Reference
    private DatabaseService databaseService;

    @Reference
    private NoteDao noteDao;

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String id = request.getParameter("id");
        if(id != null){
            try{
                List<Note> note = getNoteByClientId(Integer.parseInt(id));
                if(note.size() > 0){
                    response.getWriter().write(strToJson(note));
                }else{
                    response.getWriter().write(strToJson(new ErrorMessage("No notes found")));
                }
            } catch (Exception e){
                response.getWriter().write(strToJson(new ErrorMessage("Id must be a number")));
            }
        }else{
            try{
                if(getAllNote().size() > 0){
                    response.getWriter().write(strToJson(getAllNote()));
                }else{
                    response.getWriter().write(strToJson(new ErrorMessage("No notes found")));
                }
            } catch (Exception e){
                response.getWriter().write(strToJson(new ErrorMessage("Error")));
            }
        }
    }

    @Override
    public List<Note> getAllNote() {
        return noteDao.getAllNotes();
    }

    @Override
    public List<Note> getNoteByClientId(int id) {
        return noteDao.getNoteByClientId(id);
    }

    @Override
    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
