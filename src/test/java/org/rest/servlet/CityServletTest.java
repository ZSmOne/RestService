package org.rest.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.rest.exception.NotFoundException;
import org.rest.model.City;
import org.rest.service.CityService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CityServletTest {

    @InjectMocks
    private static CityServlet cityServlet;
    @Mock
    private static CityService mockCityService;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private BufferedReader mockBufferedReader;

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
        cityServlet = new CityServlet(mockCityService);
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockCityService);
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.doReturn("city/all").when(mockRequest).getPathInfo();
        cityServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockCityService).findAll();

    }

    @Test
    void doGetById() throws IOException, NotFoundException {
        Mockito.when(mockRequest.getPathInfo()).thenReturn("city/3");
        cityServlet.doGet(mockRequest, mockResponse);
        verify(mockCityService).findById(Mockito.anyLong());
    }
    @Test
    void doGet_ThrowsNotFoundException() throws IOException, NotFoundException {
        Mockito.doReturn("city/200").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockCityService).findById(200L);
        cityServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
    @Test
    void doGetBadRequest() throws IOException {
        Mockito.doReturn("city/Moscow").when(mockRequest).getPathInfo();
        cityServlet.doGet(mockRequest, mockResponse);
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doDelete() throws IOException, NotFoundException {
        Mockito.doReturn("city/3").when(mockRequest).getPathInfo();
        Mockito.doReturn(true).when(mockCityService).delete(Mockito.anyLong());
        cityServlet.doDelete(mockRequest, mockResponse);
        Mockito.verify(mockCityService).delete(Mockito.anyLong());
    }

    @Test
    void doDeleteNotFound() throws IOException, NotFoundException {
        Mockito.doReturn("city/200").when(mockRequest).getPathInfo();
        Mockito.doThrow(new NotFoundException("not found.")).when(mockCityService).delete(100L);
        cityServlet.doDelete(mockRequest, mockResponse);
        verify(mockCityService).delete(Mockito.anyLong());
    }

    @Test
    void doPost() throws IOException {
        String expectedName = "New city";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"name\":\"" + expectedName + "\"}",
                null
        ).when(mockBufferedReader).readLine();

        cityServlet.doPost(mockRequest, mockResponse);
        ArgumentCaptor<City> argumentCaptorCity = ArgumentCaptor.forClass(City.class);
        verify(mockCityService).save(argumentCaptorCity.capture());
        City result = argumentCaptorCity.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPostBadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\":1}",
                null).when(mockBufferedReader).readLine();
        cityServlet.doPost(mockRequest, mockResponse);
        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void doPut() throws IOException, NotFoundException {
        String expectedName = "Update city Moscow";
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn("{\"id\": 2,\"name\": \"" + expectedName + "\"}",
                null).when(mockBufferedReader).readLine();
        cityServlet.doPut(mockRequest, mockResponse);
        ArgumentCaptor<City> argumentCaptorCity = ArgumentCaptor.forClass(City.class);
        verify(mockCityService).update(argumentCaptorCity.capture());
        City result = argumentCaptorCity.getValue();
        Assertions.assertEquals(expectedName, result.getName());
    }

    @Test
    void doPutBadRequest() throws IOException {
        Mockito.doReturn(mockBufferedReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{Bad json:1}",
                null).when(mockBufferedReader).readLine();
        cityServlet.doPut(mockRequest, mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}
