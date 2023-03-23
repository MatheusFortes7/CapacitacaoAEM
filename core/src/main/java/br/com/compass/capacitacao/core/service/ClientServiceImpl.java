package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.dao.ClientDao;
import br.com.compass.capacitacao.core.dao.NoteDao;
import br.com.compass.capacitacao.core.models.Client;
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
                    response.getWriter().write("No client found");
                } else {
                    response.getWriter().write(strToJson(getClientById(Integer.parseInt(id))));
                }
            } catch (Exception e){
                throw new RuntimeException("Id must be a number");
            }
        }else{
            try{
                List<Client> clients = getAllClient();
                if(clients.size() == 0){
                    response.getWriter().write("No client found");
                } else {
                    response.getWriter().write(strToJson(clients));
                }
            } catch (Exception e){
                throw new RuntimeException("Error");
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
                throw new RuntimeException(e);
            }
            for(Client c : client){
                if(c.getName() == null || c.getName().isEmpty()){
                    throw new RuntimeException("Name is required");
                } else {

                    clientDao.addClient(c);
                    response.getWriter().write("Client added");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
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
                throw new RuntimeException("Name is required");
            } else {
                if(client.getId() == 0){
                    throw new RuntimeException("Id is required");
                } else if(clientDao.getClientById(client.getId()) == null){
                    throw new RuntimeException("Client not found");
                } else {
                    clientDao.updateClient(client);
                    response.getWriter().write("Client updated");
                }
            }
        } catch (Exception e){
            throw new RuntimeException(e);
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
                throw new RuntimeException(e.getMessage());
            }
            for(Client c : client){
                response.getWriter().write("Cheguei no for");
                if(clientDao.getClientById(c.getId()) == null){
                    throw new RuntimeException("Client not found");
                } else {
                    if(noteDao.getNoteByClientId(c.getId()) != null){
                        noteDao.deleteNoteByClientId(c.getId());
                    }
                    clientDao.deleteClient(c.getId());
                    response.getWriter().write("Client deleted");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
