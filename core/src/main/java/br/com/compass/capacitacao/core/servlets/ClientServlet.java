package br.com.compass.capacitacao.core.servlets;


import br.com.compass.capacitacao.core.service.ClientService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.apache.sling.api.servlets.HttpConstants.*;
import static org.apache.sling.api.servlets.ServletResolverConstants.*;

@Component(
        immediate = true,
        service = Servlet.class,
        property = {
            SLING_SERVLET_METHODS + "=" + METHOD_GET,
            SLING_SERVLET_METHODS + "=" + METHOD_POST,
            SLING_SERVLET_METHODS + "=" + METHOD_DELETE,
            SLING_SERVLET_METHODS + "=" + METHOD_PUT,
            SLING_SERVLET_RESOURCE_TYPES + "=" + "capacitacao/client"
        })
public class ClientServlet extends SlingAllMethodsServlet {

    @Reference
    private ClientService clientService;

    @Override
    public void doGet(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException, ServletException {
       clientService.doGet(request, response);
    }

    @Override
    protected void doPost(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException, ServletException {
        clientService.doPost(request, response);
    }

    @Override
    protected void doDelete(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException, ServletException {
        clientService.doDelete(request, response);
    }

    @Override
    protected void doPut(final SlingHttpServletRequest request, final SlingHttpServletResponse response) throws IOException, ServletException {
        clientService.doPut(request, response);
    }
}
