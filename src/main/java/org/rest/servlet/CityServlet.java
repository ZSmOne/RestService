package org.rest.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.rest.exception.NotFoundException;
import org.rest.service.CityService;
import org.rest.service.impl.CityServiceImpl;
import org.rest.servlet.dto.CityIncomingDto;
import org.rest.servlet.dto.CityOutGoingDto;
import org.rest.servlet.dto.CityUpdateDto;
import org.rest.servlet.mapper.CityDtoMapper;
import org.rest.servlet.mapper.impl.CityDtoMapperImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(urlPatterns = {"/city/*"})
public class CityServlet extends HttpServlet {
    private final transient CityService cityService;
    private final transient CityDtoMapper cityDtoMapper = CityDtoMapperImpl.getInstance();
    private final transient ObjectMapper objectMapper;
    public CityServlet(CityService cityService) {
        this.cityService = cityService;
        this.objectMapper = new ObjectMapper();
    }
    public CityServlet() {
        this.cityService = CityServiceImpl.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    private static String getJson(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader postData = req.getReader();
        String line;
        while ((line = postData.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private static void setJsonHeader(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String response = null;
        try {
            String[] pathPart = req.getPathInfo().split("/");
            if ("all".equals(pathPart[1])) {
                //List<CityOutGoingDto> cityDtoList = cityDtoMapper.map(cityService.findAll());
                resp.setStatus(HttpServletResponse.SC_OK);
                //response = objectMapper.writeValueAsString(cityDtoList);
                response = "{\"gete\":\"test city\"}";
            } else {
                Long cityId = Long.parseLong(pathPart[1]);
                CityOutGoingDto cityDto = cityDtoMapper.map(cityService.findById(cityId));
                resp.setStatus(HttpServletResponse.SC_OK);
                response = objectMapper.writeValueAsString(cityDto);
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Bad request.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String response = null;
        CityIncomingDto cityResponse;
        try {
            cityResponse = objectMapper.readValue(json, CityIncomingDto.class);
            if (cityResponse == null)
                throw new IllegalArgumentException();
            CityIncomingDto cityIncomingDto = cityResponse;
            response = objectMapper.writeValueAsString(cityService.save(cityDtoMapper.map(cityIncomingDto)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response = "Incorrect city Object.";
        }

        PrintWriter printWriter = resp.getWriter();
        printWriter.write(response);
        printWriter.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer = "";
        CityUpdateDto cityResponse;
        try {
            cityResponse = objectMapper.readValue(json, CityUpdateDto.class);
            if (cityResponse == null)
                throw new IllegalArgumentException();
            cityService.update(cityDtoMapper.map(cityResponse));
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Incorrect city Object.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String responseAnswer = "";
        try {
            String[] pathPart = req.getPathInfo().split("/");
            Long cityId = Long.parseLong(pathPart[1]);
            if (cityService.delete(cityId)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Bad request.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }
}


