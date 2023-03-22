package br.com.compass.capacitacao.core.dao;



import br.com.compass.capacitacao.core.models.Client;

import java.util.List;

public interface ClientDao {

    Client getClientById(int id);
    List<Client> getAllClients();

    void addClient(Client client);

    void updateClient(Client client);

    void deleteClient(int id);
}
