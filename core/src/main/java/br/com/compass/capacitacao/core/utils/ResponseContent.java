package br.com.compass.capacitacao.core.utils;

import br.com.compass.capacitacao.core.models.ErrorMessage;
import br.com.compass.capacitacao.core.models.SucessMessage;
import com.google.gson.Gson;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;

import java.io.IOException;

@Component(immediate = true, service = ResponseContent.class)
public class ResponseContent {

    public void FinalMesage(int status, String message, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);
        if(status == 200) {
            response.getWriter().write(strToJson(new SucessMessage(message)));
        } else if (status == 400){
            response.getWriter().write(strToJson(new ErrorMessage(message)));
        } else {
            response.getWriter().write(strToJson(new ErrorMessage(message)));
        }
    }

    public void getRequest(int status, SlingHttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(200);
    }

    public String strToJson(Object obj) {
        return new Gson().toJson(obj);
    }
}
