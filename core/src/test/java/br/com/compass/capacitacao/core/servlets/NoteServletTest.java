package br.com.compass.capacitacao.core.servlets;

import br.com.compass.capacitacao.core.service.NoteService;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class NoteServletTest {

    @Mock
    private SlingHttpServletRequest requestMock;

    @Mock
    private SlingHttpServletResponse responseMock;

    @InjectMocks
    private NoteServlet noteServlet;

    @Mock
    private NoteService noteService;

    @Test
    void doGetShouldCallNoteServiceDoGet() throws Exception {
        assertDoesNotThrow(() -> noteServlet.doGet(requestMock, responseMock));
    }


}