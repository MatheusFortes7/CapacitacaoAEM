package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.dao.ClientDao;
import br.com.compass.capacitacao.core.dao.NoteDao;
import br.com.compass.capacitacao.core.models.Client;
import br.com.compass.capacitacao.core.utils.ResponseContent;
import com.google.gson.Gson;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.tika.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ClientServiceImplTest {

    private final Gson GSON = new Gson();
    @Mock
    private SlingHttpServletRequest request;
    @Mock
    private SlingHttpServletResponse response;

    @InjectMocks
    private ClientService clientService = new ClientServiceImpl();

    @Mock
    private ClientDao clientDaoMock;
    @Mock
    private NoteDao noteDaoMock;

    SlingHttpServletResponse realResponse;
    SlingHttpServletRequest realRequest;

    @Mock
    ResponseContent responseContent;

    @Mock
    IOUtils ioUtils;

    @BeforeEach
    void setUp(AemContext context) {
        MockitoAnnotations.openMocks(this);

        realResponse = context.response();
        realRequest = context.request();
    }

    @Test
    @DisplayName("doGet Should return Client when query param is passed")
    void doGetShouldReturnClientWhenQueryParamIsPassed() throws IOException {
        when(request.getParameter("id")).thenReturn("1");

        when(clientDaoMock.getClientById(1)).thenReturn(getClientList().get(0));
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> clientService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet Should not throw exception when id is not a number")
    void doGetShouldNotThrowExceptionWhenIdIsNotANumber() throws IOException {
        when(request.getParameter("id")).thenReturn("a");

        assertDoesNotThrow(() -> clientService.doGet(request, response));
    }


    @Test
    @DisplayName("doGet Should not throw exception when no client is found by the id")
    void doGetShouldNotThrowExceptionWhenNoClientIsFoundById() {
        when(request.getParameter("id")).thenReturn("1");

        when(clientDaoMock.getClientById(1)).thenReturn(null);

        assertDoesNotThrow(() -> clientService.doGet(request, response));
    }


    @Test
    @DisplayName("doGet Should return all clients when no query param is passed")
    void doGetShouldReturnAllClientsWhenNoQueryParamIsPassed() throws IOException {

        when(clientDaoMock.getAllClients()).thenReturn(getClientList());
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> clientService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet Should not throw exception when output is null")
    void doGetShouldNotThrowExceptionWhenOutputIsNull() {

        when(clientDaoMock.getAllClients()).thenReturn(null);

        assertDoesNotThrow(() -> clientService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet Should not throw exception when no clients are found")
    void doGetShouldNotThrowExceptionWhenNoClientsAreFound() {

        when(clientDaoMock.getAllClients()).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> clientService.doGet(request, response));
    }

    @Test
    @DisplayName("doPost Should return status code 200 when client is created")
    void doPostShouldNotThrowExceptionWhenClientIsCreated() throws IOException {
        final Client client = new Client("Teste");
        String clientJson = new Gson().toJson(client);

        StringBuilder sb = new StringBuilder(clientJson);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());
        doNothing().when(clientDaoMock).addClient(any());

        assertDoesNotThrow(() -> Mockito.doCallRealMethod().when(responseContent).FinalMesage(Mockito.anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));


        assertDoesNotThrow(() -> clientService.doPost(request, response));
    }

    @Test
    @DisplayName("doPost should not throw exception when client is not created")
    void doPostShouldNotThrowExceptionWhenClientIsNotCreated() throws IOException {
        final Client client = new Client("");
        String clientJson = new Gson().toJson(client);

        StringBuilder sb = new StringBuilder(clientJson);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        assertDoesNotThrow(() -> Mockito.doCallRealMethod().when(responseContent).FinalMesage(Mockito.anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));


        assertDoesNotThrow(() -> clientService.doPost(request, response));
    }

    @Test
    @DisplayName("doPost should not throw exception when json is wrong")
    void doPostShouldNotThrowExceptionWhenJsonIsWrong() throws IOException {
        final Client client = new Client("");
        String clientJson = new Gson().toJson(client);

        StringBuilder sb = new StringBuilder(clientJson);
        sb.append("]");
        sb.insert(0, "");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        assertDoesNotThrow(() -> Mockito.doCallRealMethod().when(responseContent).FinalMesage(Mockito.anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));


        assertDoesNotThrow(() -> clientService.doPost(request, response));
    }

    @Test
    @DisplayName("doPost Should Throw Exception When FinalMesage Not Work")
    void doPostShouldThrowExceptionWhenFinalMesageNotWork() throws IOException {
        final Client client = new Client("Teste");
        String clientJson = new Gson().toJson(client);

        StringBuilder sb = new StringBuilder(clientJson);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());
        doNothing().when(clientDaoMock).addClient(any());

        doThrow(IOException.class).when(responseContent).FinalMesage(anyInt(),anyString(),any());

        assertThrows(RuntimeException.class,()-> clientService.doPost(request, response));
    }



    @Test
    @DisplayName("doPut should not throw exception when client is not found")
    void doPutShouldNotThrowExceptionWhenClientIsNotFound() throws IOException {
        final Client client = new Client(1,"Teste");
        String clientJson = new Gson().toJson(client);
        StringBuilder sb = new StringBuilder(clientJson);

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));

        when(clientDaoMock.getClientById(1)).thenReturn(null);

        assertDoesNotThrow(()-> clientService.doPut(request, response));

    }

    @Test
    @DisplayName("doPut should throw exception when IOUtils throws exception")
    void doPutShouldThrowExceptionWhenIOUtilsThrowsException() throws IOException {
        final Client client = new Client(1,"Teste");
        String clientJson = new Gson().toJson(client);
        StringBuilder sb = new StringBuilder(clientJson);

        when(request.getReader()).thenThrow(IOException.class);

        assertThrows(RuntimeException.class,()-> clientService.doPut(request, response));
    }

    @Test
    @DisplayName("doPut should throw exception when FinalMesage dont work")
    void doPutShouldThrowExceptionWhenFinalMesageDontWork() throws IOException {
        final Client client = new Client(1,"Teste");
        String clientJson = new Gson().toJson(client);
        StringBuilder sb = new StringBuilder(clientJson);

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));

        when(clientDaoMock.getClientById(1)).thenReturn(getClientList().get(0));
        doThrow(IOException.class).when(responseContent).FinalMesage(anyInt(),anyString(),any());

        assertThrows(RuntimeException.class,()-> clientService.doPut(request, response));
    }

    @Test
    @DisplayName("doPut should not throw exception when id is 0")
    void doPutShouldNotThrowExceptionWhenIdIsZero() throws IOException {
        final Client client = new Client(0,"Teste");
        String clientJson = new Gson().toJson(client);
        StringBuilder sb = new StringBuilder(clientJson);

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));


        assertDoesNotThrow(()-> clientService.doPut(request, response));
    }

    @Test
    @DisplayName("doPut should not throw exception when name is null")
    void doPutShouldNotThrowExceptionWhenNameIsNull() throws IOException {
        final Client client = new Client(1,null);
        String clientJson = new Gson().toJson(client);
        StringBuilder sb = new StringBuilder(clientJson);

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));

        assertDoesNotThrow(()-> clientService.doPut(request, response));
    }

    @Test
    @DisplayName("doPut should not throw exception when client is updated")
    void doPutShouldNotThrowExceptionWhenClientIsUpdated() throws IOException {
        final Client client = new Client(1,"Teste");
        String clientJson = new Gson().toJson(client);
        StringBuilder sb = new StringBuilder(clientJson);

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));

        when(clientDaoMock.getClientById(1)).thenReturn(client);

        assertDoesNotThrow(()-> clientService.doPut(request, response));
    }

    @Test
    @DisplayName("doDelete should not throw exception when client is not found")
    void doDeleteShouldNotThrowExceptionWhenClientIsNotFound() throws IOException {
        final Client client = new Client(1,"Teste");
        String clientJson = new Gson().toJson(client);

        StringBuilder sb = new StringBuilder(clientJson);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());


        assertDoesNotThrow(() -> Mockito.doCallRealMethod().when(responseContent).FinalMesage(Mockito.anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));


        assertDoesNotThrow(() -> clientService.doDelete(request, response));
    }

    @Test
    @DisplayName("doDelete should not throw exception when client is deleted")
    void doDeleteShouldNotThrowExceptionWhenClientIsDeleted() throws IOException {
        final Client client = new Client(1,"Teste");
        String clientJson = new Gson().toJson(client);

        StringBuilder sb = new StringBuilder(clientJson);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        when(clientDaoMock.getClientById(1)).thenReturn(client);
        when(noteDaoMock.getNoteByClientId(1)).thenReturn(null);

        assertDoesNotThrow(() -> Mockito.doCallRealMethod().when(responseContent).FinalMesage(Mockito.anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> clientService.doDelete(request, response));
    }

    @Test
    @DisplayName("doDelete should not throw exception when note is deleted")
    void doDeleteShouldNotThrowExceptionWhenNoteIsDeleted() throws IOException {
        final Client client = new Client(1,"Teste");
        String clientJson = new Gson().toJson(client);

        StringBuilder sb = new StringBuilder(clientJson);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        when(clientDaoMock.getClientById(1)).thenReturn(client);
        when(noteDaoMock.getNoteByClientId(1)).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> Mockito.doCallRealMethod().when(responseContent).FinalMesage(Mockito.anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> clientService.doDelete(request, response));
    }

    @Test
    @DisplayName("doDelete should not throw exception when json is wrong")
    void doDeleteShouldNotThrowExceptionWhenJsonIsWrong() throws IOException {
        final Client client = new Client(1,"Teste");
        String clientJson = new Gson().toJson(client);

        StringBuilder sb = new StringBuilder(clientJson);
        sb.append("]");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        assertDoesNotThrow(() -> Mockito.doCallRealMethod().when(responseContent).FinalMesage(Mockito.anyInt(),Mockito.anyString(), any(SlingHttpServletResponse.class)));

        when(responseContent.strToJson(any())).thenCallRealMethod();
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> clientService.doDelete(request, response));
    }

    @Test
    @DisplayName("doDelete should throw exception when FinalMesage Dont Work")
    void doDeleteShouldThrowExceptionWhenFinalMesageDontWork() throws IOException {
        final Client client = new Client("Teste");
        String clientJson = new Gson().toJson(client);

        StringBuilder sb = new StringBuilder(clientJson);
        sb.append("]");
        sb.insert(0, "[");

        when(responseContent.readJson(request,response)).thenReturn(sb.toString());

        doThrow(IOException.class).when(responseContent).FinalMesage(anyInt(),anyString(),any());

        assertThrows(RuntimeException.class,()-> clientService.doDelete(request, response));
    }

    List<Client> getClientList(){
        List<Client> clients = new ArrayList<>();
        clients.add(new Client(1, "teste"));
        clients.add(new Client(2, "teste2"));
        return clients;
    }
}