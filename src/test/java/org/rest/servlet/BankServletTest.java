package org.rest.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rest.exception.NotFoundException;
import org.rest.model.Bank;
import org.rest.service.BankService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import static org.mockito.Mockito.verify;

@ExtendWith(
        MockitoExtension.class
)
class BankServletTest {
    @InjectMocks
    private static BankServlet bankServlet;
    @Mock
    private static BankService mockBankService;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
        bankServlet = new BankServlet(mockBankService);

    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockBankService);
    }

    @Test
    void doGet_FindAll() throws IOException {
        Mockito.doReturn("bank/all").when(mockRequest).getPathInfo();
        bankServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockBankService).findAll();
    }

    @Test
    void doGet_FindById() throws IOException, NotFoundException {
        Mockito.when(mockRequest.getPathInfo()).thenReturn("user/3");
        bankServlet.doGet(mockRequest, mockResponse);
        verify(mockBankService).findById(Mockito.anyLong());
    }

    @Test
    void doGetFindById_ThrowsNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("user/200").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockBankService).findById(200L);
        bankServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGet_FindById_BadRequest() throws IOException {
        Mockito.doReturn("user/name").when(mockRequest).getPathInfo();
        bankServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDeleteById() throws IOException, NotFoundException {
        Mockito.doReturn("user/3").when(mockRequest).getPathInfo();
        Mockito.doReturn(true).when(mockBankService).delete(Mockito.anyLong());
        bankServlet.doDelete(mockRequest, mockResponse);
        Mockito.verify(mockBankService).delete(Mockito.anyLong());
    }

    @Test
    void doDelete_ThrowsNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("city/200").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockBankService).delete(100L);
        bankServlet.doDelete(mockRequest, mockResponse);
        verify(mockBankService).delete(Mockito.anyLong());
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String expectedName = "New bank";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"name\":\"" + expectedName + "\"}",
                null).when(mockBufferedReader).readLine();
        bankServlet.doPost(mockRequest, mockResponse);
        ArgumentCaptor<Bank> argumentCaptorBank = ArgumentCaptor.forClass(Bank.class);
        verify(mockBankService).save(argumentCaptorBank.capture());
        Bank result = argumentCaptorBank.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPost_BadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":1}", null).when(mockBufferedReader).readLine();
        bankServlet.doPost(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPut() throws IOException, NotFoundException {
        String expectedName = "New bank name";
        Mockito.doReturn("bank/").when(mockRequest).getPathInfo();
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();

        Mockito.doReturn(
                "{\"id\": 3,\"name\": \"" + expectedName + "\"}",
                null).when(mockBufferedReader).readLine();
        bankServlet.doPut(mockRequest, mockResponse);
        ArgumentCaptor<Bank> argumentCaptorBank = ArgumentCaptor.forClass(Bank.class);
        Mockito.verify(mockBankService).update(argumentCaptorBank.capture());
        Bank result = argumentCaptorBank.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }


    @Test
    void doPutBadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{Bad json:1}", null).when(mockBufferedReader).readLine();
        bankServlet.doPut(mockRequest, mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
