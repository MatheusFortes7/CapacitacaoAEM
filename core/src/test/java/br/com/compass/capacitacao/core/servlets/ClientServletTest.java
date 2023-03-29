package br.com.compass.capacitacao.core.servlets;


import br.com.compass.capacitacao.core.service.ClientService;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ClientServletTest {

    @Mock
    private SlingHttpServletRequest requestMock;

    @Mock
    private SlingHttpServletResponse responseMock;

    @InjectMocks
    private ClientServlet clientServlet;

    @Mock
    private ClientService clientService;

    @Test
    void doGetShouldCallClientServiceDoGet() throws ServletException, IOException {
        assertDoesNotThrow(() -> clientServlet.doGet(requestMock, responseMock));


    }

    @Test
    void doPostShouldCallClientServiceDoPost() throws ServletException, IOException {
        assertDoesNotThrow(() -> clientServlet.doPost(requestMock, responseMock));
    }

    @Test
    void doDeleteShouldCallClientServiceDoDelete() throws ServletException, IOException {
        assertDoesNotThrow(() -> clientServlet.doDelete(requestMock, responseMock));
    }

    @Test
    void doPutShouldCallClientServiceDoPut() throws ServletException, IOException {
        assertDoesNotThrow(() -> clientServlet.doPut(requestMock, responseMock));
    }

}