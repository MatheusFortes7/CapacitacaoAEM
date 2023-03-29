package br.com.compass.capacitacao.core.servlets;

import br.com.compass.capacitacao.core.service.NoteService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.apache.sling.api.servlets.HttpConstants.METHOD_GET;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_METHODS;
import static org.apache.sling.api.servlets.ServletResolverConstants.SLING_SERVLET_RESOURCE_TYPES;

@Component(
        immediate = true,
        service = Servlet.class,
        property = {
                SLING_SERVLET_METHODS + "=" + METHOD_GET,
                SLING_SERVLET_RESOURCE_TYPES + "=" + "capacitacao/note",
        })
public class NoteServlet extends SlingAllMethodsServlet {



    @Reference
    private NoteService noteService;

    @Override
    protected void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException, ServletException {
        noteService.doGet(request, response);
    }
}
