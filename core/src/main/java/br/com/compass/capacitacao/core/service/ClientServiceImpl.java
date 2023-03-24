package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.dao.ClientDao;
import br.com.compass.capacitacao.core.dao.NoteDao;
import br.com.compass.capacitacao.core.models.Client;
import br.com.compass.capacitacao.core.models.ErrorMessage;
import br.com.compass.capacitacao.core.models.SucessMessage;
import br.com.compass.capacitacao.core.utils.ResponseContent;
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
    final private ResponseContent responseContent = new ResponseContent();
    @Reference
    private ClientDao clientDao;
    @Reference
    private NoteDao noteDao;

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        final String id = request.getParameter("id");
        if(id != null){
            try{
                if(getClientById(Integer.parseInt(id)) == null){
                    responseContent.FinalMesage(400, "No client found", response);
                } else {
                    responseContent.getRequest(200, response);
                    response.getWriter().write(strToJson(getClientById(Integer.parseInt(id))));
                }
            } catch (Exception e){
                responseContent.FinalMesage(400, "Id must be a number", response);
            }
        }else{
            try{
                final List<Client> clients = getAllClients();
                if(clients.size() == 0){
                    responseContent.FinalMesage(400, "No client found", response);
                } else {
                    responseContent.getRequest(200, response);
                    response.getWriter().write(strToJson(clients));
                }
            } catch (Exception e){
                responseContent.FinalMesage(400, "Error", response);
            }
        }
    }

    @Override
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        addClient(request, response);
    }

    @Override
    public void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        deleteClient(request, response);
    }

    @Override
    public void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        updateClient(request, response);
    }

    @Override
    public List<Client> getAllClients() {
        return clientDao.getAllClients();
    }

    @Override
    public Client getClientById(int id) {
        return clientDao.getClientById(id);
    }

    @Override
    public void addClient(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            final BufferedReader reader = request.getReader();
            final Type type = new TypeToken<List<Client>>() {}.getType();
            List<Client> client = null;
            try {
                client = new Gson().fromJson(reader, type);
            } catch (Exception e) {
                responseContent.FinalMesage(400, "Json error", response);
                return;
            }
            for(Client c : client){
                if(c.getName() == null || c.getName().isEmpty()){
                    responseContent.FinalMesage(400, "Name is required", response);
                } else {
                    clientDao.addClient(c);
                    responseContent.FinalMesage(200, "Client added", response);
                }
            }
        } catch (IOException e) {
            try {
                responseContent.FinalMesage(400, "Json error", response);
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
                responseContent.FinalMesage(400, "Name is required", response);
            } else {
                if(client.getId() == 0){
                    responseContent.FinalMesage(400, "Id is required", response);
                } else if(clientDao.getClientById(client.getId()) == null){
                    responseContent.FinalMesage(400, "Client not found", response);
                } else {
                    clientDao.updateClient(client);
                    responseContent.FinalMesage(200, "Client updated", response);
                }
            }
        } catch (Exception e){
            try {
                responseContent.FinalMesage(400, "Json error", response);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    @Override
    public void deleteClient(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try{
            final BufferedReader reader = request.getReader();
            final Type type = new TypeToken<List<Client>>() {}.getType();
            List<Client> client = null;
            try {
                client = new Gson().fromJson(reader, type);
            } catch (Exception e) {
                responseContent.FinalMesage(400, "Json error", response);
                return;
            }
            for(Client c : client){
                try{
                    if(clientDao.getClientById(c.getId()) == null){
                        responseContent.FinalMesage(400, "Client not found", response);
                    } else {
                        if(noteDao.getNoteByClientId(c.getId()) != null){
                            noteDao.deleteNoteByClientId(c.getId());
                        }
                        clientDao.deleteClient(c.getId());
                        responseContent.FinalMesage(200, "Client deleted", response);
                    }
                } catch (Exception e){
                    response.setStatus(400);
                    response.getWriter().write(strToJson(new ErrorMessage("Error")));
                }
            }
        } catch (IOException e) {
            try {
                responseContent.FinalMesage(400, "Json error", response);
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
