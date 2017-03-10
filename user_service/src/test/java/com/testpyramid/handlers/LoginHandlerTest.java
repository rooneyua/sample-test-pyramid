package com.testpyramid.handlers;

import com.google.gson.Gson;
import com.testpyramid.HttpResult;
import com.testpyramid.persistence.UserRepository;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import spark.Request;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoginHandlerTest {
    private final Gson gson = new Gson();
    private UserRepository mockUserRepository = mock(UserRepository.class);
    private Map<String, String> defaultResultSet = new HashMap<>();

    @Before
    public void setUp() throws Exception {
        defaultResultSet.put("name", "tom");
        defaultResultSet.put("active", "false");
    }

    @Test
    public void getsParametersFromRequestBody() throws Exception {
        String email = "mail@example.com";
        String password = "password1";
        Request mockRequest = mockRequest(email, password);

        LoginHandler handler = new LoginHandler(mockUserRepository);
        handler.handle(mockRequest);

        verify(mockUserRepository, times(1)).findByEmailAndPassword(email, password);
    }

    @Test
    public void returnsUserDetailsIfUserFound() throws Exception {
        String email = "email";
        String password = "password";
        Request mockRequest = mockRequest(email, password);
        when(mockUserRepository.findByEmailAndPassword(email, password))
                .thenReturn(createResultMap("your name", "true"));

        LoginHandler handler = new LoginHandler(mockUserRepository);
        HttpResult<String> result = handler.handle(mockRequest);

        assertTrue(result.isSuccess());
        assertEquals("your name", result.getValue());
    }

    @Test
    public void returnsUnauthorizedIfUserNotFound() throws Exception {
        when(mockUserRepository.findByEmailAndPassword(any(), any()))
                .thenReturn(null);

        LoginHandler handler = new LoginHandler(mockUserRepository);
        HttpResult<String> result = handler.handle(mockRequest("", ""));

        assertFalse(result.isSuccess());
        assertEquals(HttpStatus.UNAUTHORIZED_401, result.getStatusCode());
        assertEquals("", result.getErrorMessage());
    }

    @Test
    public void returnsUnauthorizedIfUserIsNotActive() throws Exception {
        String email = "email";
        String password = "password";

        when(mockUserRepository.findByEmailAndPassword(email, password))
                .thenReturn(createResultMap(email, "false"));

        LoginHandler handler = new LoginHandler(mockUserRepository);
        HttpResult<String> result = handler.handle(mockRequest(email, password));

        assertFalse(result.isSuccess());
        assertEquals(HttpStatus.UNAUTHORIZED_401, result.getStatusCode());
        assertEquals("", result.getErrorMessage());
    }

    private Request mockRequest(String email, String password) {
        Request mockRequest = mock(Request.class);
        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("email", email);
        requestMap.put("password", password);
        String requestJson = gson.toJson(requestMap);

        when(mockRequest.body()).thenReturn(requestJson);

        return mockRequest;
    }

    private Map<String, String> createResultMap(String name, String active) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("name", name);
        resultMap.put("active", active);
        return resultMap;
    }
}