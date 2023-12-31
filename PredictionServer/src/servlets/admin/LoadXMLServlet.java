package servlets.admin;

import com.google.gson.Gson;
import dto.StatusDTO;
import engine.Engine;
import http.url.Client;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SessionUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

import static http.url.URLconst.LOAD_XML_SRC;
import static utils.ServletUtils.getEngine;

@WebServlet(name="loadXMLServlet", urlPatterns = LOAD_XML_SRC)
public class LoadXMLServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        ServletContext servletContext = getServletContext();
        if (SessionUtils.getTypeOfClient(request) != Client.ADMIN) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println(gson.toJson(new StatusDTO(false, "unauthorized client type")));
            return;
        }
        Engine engine = getEngine(servletContext);
        String xmlPath = gson.fromJson(request.getReader(), String.class);
        try {
            engine.loadXML(xmlPath);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(gson.toJson(new StatusDTO(true, "XML file loaded successfully.")));
        } catch (FileNotFoundException | IllegalArgumentException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println(gson.toJson(new StatusDTO(false, e.getMessage())));
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println(gson.toJson(new StatusDTO(false, e.getMessage())));
        }
    }
}
