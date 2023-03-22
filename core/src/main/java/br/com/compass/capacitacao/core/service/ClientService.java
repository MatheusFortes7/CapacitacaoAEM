package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.models.Client;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

public interface ClientService {

    void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;
    void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;
    void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;
    void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException;


    List<Client> getAllClient();
    Client getClientById(int id);
    void addClient(SlingHttpServletRequest request, SlingHttpServletResponse response);
    void updateClient(SlingHttpServletRequest request, SlingHttpServletResponse response);
    void deleteClient(SlingHttpServletRequest request, SlingHttpServletResponse response);

    String strToJson(Object obj);
}
