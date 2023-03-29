package br.com.compass.capacitacao.core.service;

import br.com.compass.capacitacao.core.dao.NoteDao;
import br.com.compass.capacitacao.core.models.Note;
import br.com.compass.capacitacao.core.utils.ResponseContent;
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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class NoteServiceImplTest {
    @Mock
    private SlingHttpServletRequest request;
    @Mock
    private SlingHttpServletResponse response;

    @InjectMocks
    private NoteService noteService = new NoteServiceImpl();

    @Mock
    private NoteDao noteDaoMock;

    SlingHttpServletResponse realResponse;
    SlingHttpServletRequest realRequest;

    @Mock
    ResponseContent responseContent;

    @BeforeEach
    void setUp(AemContext context) {
        MockitoAnnotations.openMocks(this);

        realResponse = context.response();
        realRequest = context.request();
    }

    @Test
    @DisplayName("doGet should not throw exception when all notes are returned")
    void doGetShouldNotThrowExceptionWhenAllNotesAreReturned() throws IOException {

        when(noteDaoMock.getAllNotes()).thenReturn(getAllNotes());
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> noteService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when note size equal 0")
    void doGetShouldNotThrowexceptionWhenNoteSizeEqual0() throws IOException {
        when(noteDaoMock.getAllNotes()).thenReturn(new ArrayList<>());


        assertDoesNotThrow(() -> noteService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when id is a number")
    void doGetShouldNotThrowExceptionWhenIdIsANumber() throws IOException {
        when(request.getParameter("id")).thenReturn("1");
        when(noteDaoMock.getNoteByClientId(1)).thenReturn(getAllNotes());
        PrintWriter writer = realResponse.getWriter();
        Assertions.assertDoesNotThrow(() -> when(response.getWriter()).thenReturn(writer));

        assertDoesNotThrow(() -> noteService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when id is not a number")
    void doGetShouldNotThrowExceptionWhenIdIsNotANumber() throws IOException {
        when(request.getParameter("id")).thenReturn("a");

        assertDoesNotThrow(() -> noteService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when no notes are found for id")
    void doGetShouldNotThrowExceptionWhenNoNotesAreFoundForId(){
        when(request.getParameter("id")).thenReturn("1");
        when(noteDaoMock.getNoteByClientId(1)).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> noteService.doGet(request, response));
    }

    @Test
    @DisplayName("doGet should not throw exception when get all notes is wrong")
    void doGetShouldNotThrowExceptionWhenGetAllNotesIsWrong(){
        when(noteDaoMock.getAllNotes()).thenReturn(null);
        assertDoesNotThrow(() -> noteService.doGet(request, response));
    }

    List<Note> getAllNotes() {
        Note note = new Note(1,1,1,2561);
        Note note2 = new Note(2,2,2,2562);
        Note note3 = new Note(3,3,3,2563);
        Note note4 = new Note(4,4,4,2564);
        List<Note> notes = new ArrayList<>();
        notes.add(note);
        notes.add(note2);
        notes.add(note3);
        notes.add(note4);
        return notes;
    }
}