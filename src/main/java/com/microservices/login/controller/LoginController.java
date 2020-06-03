package com.microservices.login.controller;

import com.microservices.login.data.AuthResponse;
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
public class LoginController {
    @Autowired
    private ILoginService loginService;

    @GetMapping("/test")
    public String test(){
        return "login-service works";
    }

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
    public ResponseEntity<AuthResponse> logout (@RequestHeader(value="Authorization") String token) {
        HttpHeaders headers = new HttpHeaders();
        if (loginService.logout(token)) {
            headers.remove("Authorization");
            return new ResponseEntity<>(new AuthResponse("logged out"), headers, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new AuthResponse("Logout Failed"), headers, HttpStatus.NOT_MODIFIED);
    }


    /**
     * @param token
     * @return boolean.
     * if request reach here it means it is a valid token.
     */
    @PostMapping("/valid/token")
    @ResponseBody
    public Boolean isValidToken (@RequestHeader(value="Authorization") String token) {
        return true;
    }

    @PostMapping("/signin/token")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<AuthResponse> createNewToken (@RequestHeader(value="Authorization") String token) {
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
                "Authorization");

        List<String> exposeList = Arrays.asList(
                "Authorization"
        );

        headers.setAccessControlAllowHeaders(headerList);
        headers.setAccessControlExposeHeaders(exposeList);

        headers.set("Authorization", token);
        return headers;
    }
}
