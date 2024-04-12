package org.rest.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.rest.exception.NotFoundException;
import org.rest.service.BankService;
import org.rest.service.impl.BankServiceImpl;
import org.rest.servlet.dto.BankIncomingDto;
import org.rest.servlet.dto.BankOutGoingDto;
import org.rest.servlet.dto.BankUpdateDto;
import org.rest.servlet.mapper.BankDtoMapper;
import org.rest.servlet.mapper.impl.BankDtoMapperImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/bank/*"})
public class BankServlet extends HttpServlet {
    private final transient BankService bankService;
    private final transient BankDtoMapper bankDtoMapper = BankDtoMapperImpl.getInstance();
    private final ObjectMapper objectMapper;

    public BankServlet() {
        this.bankService = BankServiceImpl.getInstance();
        this.objectMapper = new ObjectMapper();
    }
    public BankServlet(BankService bankService){
        this.bankService = bankService;
        this.objectMapper = new ObjectMapper();
    }

    private static void setJsonHeader(HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);

        String responseAnswer = "";
        try {
            String[] pathPart = req.getPathInfo().split("/");
            if ("all".equals(pathPart[1])) {
                List<BankOutGoingDto> bankDtoList = bankDtoMapper.map(bankService.findAll()) ;
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(bankDtoList);
            } else {
                Long bankId = Long.parseLong(pathPart[1]);
                BankOutGoingDto bankDto = bankDtoMapper.map(bankService.findById(bankId));
                resp.setStatus(HttpServletResponse.SC_OK);
                responseAnswer = objectMapper.writeValueAsString(bankDto);
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

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String responseAnswer = "";
        try {
            String[] pathPart = req.getPathInfo().split("/");
            Long bankId = Long.parseLong(pathPart[1]);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            if (req.getPathInfo().contains("/deleteUser/")) {
                if ("deleteUser".equals(pathPart[2])) {
                    Long userId = Long.parseLong(pathPart[3]);
                    bankService.deleteUserToBank(userId, bankId);
                }
            } else {
                bankService.delete(bankId);
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Bad request. ";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer = null;
        Optional<BankIncomingDto> bankResponse;
        try {
            bankResponse = Optional.ofNullable(objectMapper.readValue(json, BankIncomingDto.class));
            BankIncomingDto bankIncomingDto = bankResponse.orElseThrow(IllegalArgumentException::new);
            responseAnswer = objectMapper.writeValueAsString(bankService.save(bankDtoMapper.map(bankIncomingDto)));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Incorrect bank Object.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setJsonHeader(resp);
        String json = getJson(req);

        String responseAnswer = "";
        BankUpdateDto bankResponse;
        try {
            if (req.getPathInfo().contains("/addUser/")) {
                String[] pathPart = req.getPathInfo().split("/");
                if (pathPart.length > 3 && "addUser".equals(pathPart[2])) {
                    Long bankId = Long.parseLong(pathPart[1]);
                    resp.setStatus(HttpServletResponse.SC_OK);
                    Long userId = Long.parseLong(pathPart[3]);
                    bankService.addUserToBank(userId, bankId);
                }
            } else {
                bankResponse = objectMapper.readValue(json, BankUpdateDto.class);
                if (bankResponse == null)
                    throw new IllegalArgumentException();
                BankUpdateDto bankUpdateDto = bankResponse;
                bankService.update(bankDtoMapper.map(bankUpdateDto));
            }
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            responseAnswer = e.getMessage();
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            responseAnswer = "Incorrect bank Object.";
        }
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(responseAnswer);
        printWriter.flush();
    }
}