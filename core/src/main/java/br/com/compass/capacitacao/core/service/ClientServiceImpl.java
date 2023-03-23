package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.dao.ClientDao;
import br.com.compass.capacitacao.core.dao.NoteDao;
import br.com.compass.capacitacao.core.models.Client;
import br.com.compass.capacitacao.core.models.ErrorMessage;
import br.com.compass.capacitacao.core.models.SucessMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.tika.io.IOUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.ServletException;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@Component(immediate = true, service = ClientService.class)
public class ClientServiceImpl implements ClientService{

    @Reference
    private DatabaseService databaseService;

    @Reference
    private ClientDao clientDao;
    @Reference
    private NoteDao noteDao;

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String id = request.getParameter("id");
        if(id != null){
            try{
                if(getClientById(Integer.parseInt(id)) == null){
                    response.setStatus(404);
                    response.getWriter().write(strToJson(new ErrorMessage("No client found")));
                } else {
                    response.setStatus(200);
                    response.getWriter().write(strToJson(getClientById(Integer.parseInt(id))));
                }
            } catch (Exception e){
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Id must be a number")));
            }
        }else{
            try{
                List<Client> clients = getAllClient();
                if(clients.size() == 0){
                    response.setStatus(404);
                    response.getWriter().write(strToJson(new ErrorMessage("No client found")));
                } else {
                    response.setStatus(200);
                    response.getWriter().write(strToJson(clients));
                }
            } catch (Exception e){
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Error")));
            }
        }
    }

    @Override
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        addClient(request, response);
    }

    @Override
    public void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        deleteClient(request, response);
    }

    @Override
    public void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        updateClient(request, response);
    }

    @Override
    public List<Client> getAllClient() {
        List<Client> clients = clientDao.getAllClients();
        return clients;
    }

    @Override
    public Client getClientById(int id) {
        Client client = clientDao.getClientById(id);
        return client;
    }

    @Override
    public void addClient(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            BufferedReader reader = request.getReader();
            Type type = new TypeToken<List<Client>>() {}.getType();
            List<Client> client = null;
            try {
                client = new Gson().fromJson(reader, type);
            } catch (Exception e) {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Json error")));
                return;
            }
            for(Client c : client){
                if(c.getName() == null || c.getName().isEmpty()){
                    response.setStatus(400);
                    response.getWriter().write(strToJson(new ErrorMessage("Name is required")));
                } else {

                    clientDao.addClient(c);
                    response.setStatus(201);
                    response.getWriter().write(strToJson(new SucessMessage("Client added")));
                }
            }
        } catch (IOException e) {
            try {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Json error")));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

    }
    @Override
    public void updateClient(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        String user = null;
        try{
            user = IOUtils.toString(request.getReader());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Client client;
        try{
            client = new Gson().fromJson(user, Client.class);
            if(client.getName() == null || client.getName().isEmpty()){
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Name is required")));
            } else {
                if(client.getId() == 0){
                    response.setStatus(400);
                    response.getWriter().write(strToJson(new ErrorMessage("Id is required")));
                } else if(clientDao.getClientById(client.getId()) == null){
                    response.setStatus(404);
                    response.getWriter().write(strToJson(new ErrorMessage("Client not found")));
                } else {
                    clientDao.updateClient(client);
                    response.setStatus(202);
                    response.getWriter().write(strToJson(new SucessMessage("Client updated")));
                }
            }
        } catch (Exception e){
            try {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Json error")));
            } catch (IOException ex) {
                response.setStatus(400);
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void deleteClient(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try{
            BufferedReader reader = request.getReader();
            Type type = new TypeToken<List<Client>>() {}.getType();
            List<Client> client = null;
            try {
                client = new Gson().fromJson(reader, type);
            } catch (Exception e) {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Json error / " + e.getMessage())));
                return;
            }
            for(Client c : client){
                try{
                    if(clientDao.getClientById(c.getId()) == null){
                        response.setStatus(400);
                        response.getWriter().write(strToJson(new ErrorMessage("Client not found")));
                    } else {
                        if(noteDao.getNoteByClientId(c.getId()) != null){
                            noteDao.deleteNoteByClientId(c.getId());
                        }
                        clientDao.deleteClient(c.getId());
                        response.setStatus(202);
                        response.getWriter().write(strToJson(new SucessMessage("Client deleted")));
                    }
                } catch (Exception e){
                    response.setStatus(400);
                    response.getWriter().write(strToJson(new ErrorMessage("Error")));
                }
            }
        } catch (IOException e) {
            try {
                response.setStatus(400);
                response.getWriter().write(strToJson(new ErrorMessage("Error" + e.getMessage())));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
