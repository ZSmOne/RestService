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
import org.rest.model.User;
import org.rest.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServletTest {
    @InjectMocks
    private static UserServlet userServlet;
    @Mock
    private static UserService mockUserService;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
        userServlet = new UserServlet(mockUserService);

    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockUserService);
    }

    @Test
    void doGet_FindAll() throws IOException {
        Mockito.doReturn("user/all").when(mockRequest).getPathInfo();
        userServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockUserService).findAll();
    }

    @Test
    void doGet_FindById() throws IOException, NotFoundException {
        Mockito.when(mockRequest.getPathInfo()).thenReturn("user/3");
        userServlet.doGet(mockRequest, mockResponse);
        verify(mockUserService).findById(Mockito.anyLong());
    }

    @Test
    void doGetFindById_ThrowsNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("user/200").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockUserService).findById(200L);
        userServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGet_FindById_BadRequest() throws IOException {
        Mockito.doReturn("user/name").when(mockRequest).getPathInfo();
        userServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDeleteById() throws IOException, NotFoundException {
        Mockito.doReturn("user/3").when(mockRequest).getPathInfo();
        Mockito.doReturn(true).when(mockUserService).delete(Mockito.anyLong());
        userServlet.doDelete(mockRequest, mockResponse);
        Mockito.verify(mockUserService).delete(Mockito.anyLong());
    }

    @Test
    void doDelete_ThrowsNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("city/200").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockUserService).delete(100L);
        userServlet.doDelete(mockRequest, mockResponse);
        verify(mockUserService).delete(Mockito.anyLong());
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPost() throws IOException {
        String expectedName = "Firstname Lastname";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"name\":\"" + expectedName + "\"}",
                null).when(mockBufferedReader).readLine();
        userServlet.doPost(mockRequest, mockResponse);
        ArgumentCaptor<User> argumentCaptorUser = ArgumentCaptor.forClass(User.class);
        verify(mockUserService).save(argumentCaptorUser.capture());
        User result = argumentCaptorUser.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPost_BadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":1}", null).when(mockBufferedReader).readLine();
        userServlet.doPost(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPut() throws IOException, NotFoundException {
        String expectedName = "New username";
        //Mockito.doReturn("bank/").when(mockRequest).getPathInfo();

        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\": 3," + "\"name\": \"" + expectedName + "\""+ ", \"city\":{\"id\":3}}",
                null).when(mockBufferedReader).readLine();
        userServlet.doPut(mockRequest, mockResponse);
        ArgumentCaptor<User> argumentCaptorUser = ArgumentCaptor.forClass(User.class);
        Mockito.verify(mockUserService).update(argumentCaptorUser.capture());
        User result = argumentCaptorUser.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }


    @Test
    void doPutBadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{Bad json:1}", null).when(mockBufferedReader).readLine();
        userServlet.doPut(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}