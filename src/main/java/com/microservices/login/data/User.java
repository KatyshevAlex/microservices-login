package com.microservices.login.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean enabled;
    private int loginAttempt;
    private boolean tokenExpired;
    private LocalDateTime lastSeen;

    private Collection<Role> roles;
}