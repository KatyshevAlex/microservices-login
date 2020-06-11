package com.microservices.login.service.interfaces;

import com.microservices.login.data.User;
import com.microservices.login.data.requestDTO.LoginRequest;

import javax.servlet.http.HttpServletRequest;

public interface ILoginService {
    String login(LoginRequest loginRequest, HttpServletRequest request);

    boolean logout(String token);

    User getUserByToken(String token);

    String createNewToken(String token);
}
