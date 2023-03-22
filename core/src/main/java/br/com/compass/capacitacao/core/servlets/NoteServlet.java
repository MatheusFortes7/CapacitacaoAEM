package br.com.compass.capacitacao.core.servlets;

import br.com.compass.capacitacao.core.service.NoteService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.apache.sling.api.servlets.HttpConstants.METHOD_GET;
import static org.apache.sling.api.servlets.ServletResolverConstants.*;

@Component(
        immediate = true,
        service = Servlet.class,
        property = {
                SLING_SERVLET_METHODS + "=" + METHOD_GET,
                SLING_SERVLET_RESOURCE_TYPES + "=" + "capacitacao/note",
                SLING_SERVLET_EXTENSIONS + "=" + "json"
        })
public class NoteServlet extends SlingAllMethodsServlet {

    @Reference
    private NoteService noteService;

    @Activate
    public NoteServlet(@Reference NoteService noteService) {
        this.noteService = noteService;
    }

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException, ServletException {
        noteService.doGet(request, response);
    }
}
