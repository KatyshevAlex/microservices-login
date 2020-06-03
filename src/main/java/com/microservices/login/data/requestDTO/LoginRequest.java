package com.microservices.login.data.requestDTO;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
public class LoginRequest implements Serializable {
    private String login;
    private String password;
}