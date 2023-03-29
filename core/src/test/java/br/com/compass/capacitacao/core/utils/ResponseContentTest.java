package br.com.compass.capacitacao.core.utils;


import br.com.compass.capacitacao.core.models.Client;
import com.google.gson.Gson;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class ResponseContentTest {

    private final Gson GSON = new Gson();
    @Mock
    private SlingHttpServletRequest request;
    @Mock
    private SlingHttpServletResponse response;
    @InjectMocks
    private ResponseContent responseContent;

    SlingHttpServletResponse realResponse;
    SlingHttpServletRequest realRequest;

    @BeforeEach
    void setUp(AemContext context) {
        MockitoAnnotations.openMocks(this);

        realResponse = context.response();
        realRequest = context.request();
    }

    @Test
    @DisplayName("FinalMesage should not throw exception when status is 200")
    void FinalMesageShouldNotThrowExceptionWhenStatusIs200() throws Exception {
        final int status = 200;
        final String message = "message";

        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> responseContent.FinalMesage(status, message, response));
    }

    @Test
    @DisplayName("FinalMesage should not throw exception when status is 400")
    void FinalMesageShouldNotThrowExceptionWhenStatusIs400() throws Exception {
        final int status = 400;
        final String message = "message";

        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> responseContent.FinalMesage(status, message, response));
    }

    @Test
    @DisplayName("FinalMesage should not throw exception when status is different from 200 and 400")
    void FinalMesageShouldNotThrowExceptionWhenStatusIsDifferentFrom200And400() throws Exception {
        final int status = 500;
        final String message = "message";

        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> responseContent.FinalMesage(status, message, response));
    }

    @Test
    @DisplayName("getRequest should not throw exception when status is passed")
    void getRequestShouldNotThrowExceptionWhenStatusIsPassed() throws Exception {
        final int status = 200;

        assertDoesNotThrow(() -> responseContent.getRequest(status, response));
    }

    @Test
    @DisplayName("FinalMesage should not throw exception when reader cant be created")
    void readJsonShouldThrowExceptionWhenReaderCantBeCreated() throws Exception {
        when(request.getReader()).thenThrow(IOException.class);

        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> responseContent.readJson(request, response));
    }


    @Test
    @DisplayName("readJson should not throw exception when request is passed")
    void readJsonShouldNotThrowExceptionWhenRequestIsPassed() throws Exception {
        final Client client = new Client("Teste");
        String clientJson = new Gson().toJson(client);

        StringBuilder sb = new StringBuilder(clientJson);
        sb.append("]");
        sb.insert(0, "[");

        Reader reader = new StringReader(sb.toString());
        when(request.getReader()).thenReturn(new BufferedReader(reader));

        assertDoesNotThrow(() -> responseContent.readJson(request, response));
    }


}