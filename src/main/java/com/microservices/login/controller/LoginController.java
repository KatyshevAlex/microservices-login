package com.microservices.login.controller;

import com.microservices.login.data.AuthResponse;
import com.microservices.login.data.User;
import com.microservices.login.data.requestDTO.LoginRequest;
import com.microservices.login.service.interfaces.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {
    @Autowired
    private ILoginService loginService;
    private final String authHeader = "Authorization-jwt";

    @CrossOrigin("*")
    @PostMapping("/signin")
    @ResponseBody
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest){
        String token = loginService.login(loginRequest, httpServletRequest);
        HttpHeaders headers = makeHeaders(token);

        return new ResponseEntity<>(new AuthResponse(token), headers, HttpStatus.CREATED);
    }


    @CrossOrigin("*")
    @PostMapping("/signout")
    @ResponseBody
    public ResponseEntity<AuthResponse> logout (@RequestHeader(value=authHeader) String token) {
        HttpHeaders headers = new HttpHeaders();
        if (loginService.logout(token)) {
            headers.remove(authHeader);
            return new ResponseEntity<>(new AuthResponse("logged out"), headers, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new AuthResponse("Logout Failed"), headers, HttpStatus.NOT_MODIFIED);
    }


    @PostMapping("/get-user-by-token")
    public User isValidToken (@RequestBody AuthResponse auth) {
        return loginService.getUserByToken(auth.getAccessToken());
    }

    @PostMapping("/signin/token")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<AuthResponse> createNewToken (@RequestHeader(value=authHeader) String token) {
        String newToken = loginService.createNewToken(token);
        HttpHeaders headers = makeHeaders(newToken);
        return new ResponseEntity<>(new AuthResponse(newToken), headers, HttpStatus.CREATED);
    }


    private HttpHeaders makeHeaders(String token){
        HttpHeaders headers = new HttpHeaders();

        List<String> headerList = Arrays.asList(
                "Content-Type",
                " Accept",
                "X-Requested-With",
                "X-Requested-With",
                authHeader);

        List<String> exposeList = Arrays.asList(
                authHeader
        );

        headers.setAccessControlAllowHeaders(headerList);
        headers.setAccessControlExposeHeaders(exposeList);

        headers.set(authHeader, token);
        return headers;
    }
}
