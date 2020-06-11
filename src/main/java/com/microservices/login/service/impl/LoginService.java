package com.microservices.login.service.impl;


import com.microservices.login.annotation.LogExecutionTime;
import com.microservices.login.constants.PatternConstants.EmailConstants;
import com.microservices.login.data.User;
import com.microservices.login.data.requestDTO.LoginRequest;
import com.microservices.login.exceptions.UnauthorisedException;
import com.microservices.login.feignInterface.UserInterface;
import com.microservices.login.security.JwtTokenProvider;
import com.microservices.login.service.interfaces.ILoginService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional("transactionManager")
@Slf4j
public class LoginService implements ILoginService {


    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserInterface userInterface;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    @LogExecutionTime
    public String login(LoginRequest loginRequest, HttpServletRequest request) {
        User user = fetchUserDetails.apply(loginRequest);

        validateUser.accept(user);
        validateUserStatus.accept(user);
        validatePassword.accept(loginRequest, user);

        String jwtToken = jwtTokenProvider.createToken(loginRequest.getLogin(), request);

        return jwtToken;
    }

    private Function<LoginRequest, User> fetchUserDetails = (loginRequest) -> {
        Pattern pattern = Pattern.compile(EmailConstants.EMAIL_PATTERN);
        Matcher mailFinder = pattern.matcher(loginRequest.getLogin());

        User user = new User();
        if(mailFinder.find()){
            user.setEmail(loginRequest.getLogin());
        } else {
            user.setLogin(loginRequest.getLogin());
        }

        return userInterface.searchUser(user);
    };

    private Consumer<User> validateUser = (user) -> {
        if (Objects.isNull(user))
            throw new UnauthorisedException("User with given login or email doesn't exits.", "User entity returned null");
        log.info(":::: USER IS VALID ::::");
    };

    private Consumer<User> validateUserStatus = (user) -> {
        if (!user.isEnabled())
            throw new UnauthorisedException("The user is in disabled state.", "The user is disabled");
        log.info(":::: USER STATUS IS ENABLED ::::");
    };

    private BiConsumer<LoginRequest, User> validatePassword = (loginRequest, user) -> {
        log.info(":::: ADMIN PASSWORD VALIDATION ::::");

        if (passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())) {
            user.setLoginAttempt(0);
            user.setLastSeen(LocalDateTime.now());
            userInterface.updateUser(user);
        } else {
            user.setLoginAttempt(user.getLoginAttempt() + 1);
            if (user.getLoginAttempt() >= 3) {
                user.setEnabled(false);
                userInterface.updateUser(user);

                log.debug("USER WAS BLOCKED DUE TO MULTIPLE WRONG ATTEMPTS...");
                throw new UnauthorisedException("User was blocked. Please contact your system administrator.", "User was blocked");
            }

            log.debug("INCORRECT PASSWORD...");
            throw new UnauthorisedException("Incorrect password.Forgot Password?", "Password didn't match with the original one.");
        }

        log.info(":::: USER PASSWORD VALIDATED ::::");
    };


    @Override
    public boolean logout(String token) {
        return false;
    }

    @Override
    public User getUserByToken(String token) {
        if((null != token) && jwtTokenProvider.validateToken(token)){
            User request = new User();
            request.setLogin(jwtTokenProvider.getUsername(token));
            User response = userInterface.searchUser(request);
            return response;
        }
        return null;
    }

    @Override
    public String createNewToken(String token) {
        return null;
    }
}
