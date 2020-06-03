package com.microservices.login.security;

import com.microservices.login.configs.JwtProperties;
import com.microservices.login.data.responseDTO.NetworkResponse;
import com.microservices.login.utils.NetworkUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Autowired
    JwtProperties jwtProperties;

    String encodedKey;

    @PostConstruct
    protected void init() {
        encodedKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    public String createToken(String username, HttpServletRequest request) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getValidityInMilliseconds());
        NetworkResponse network = NetworkUtils.getDeviceAddresses.apply(request);

        return Jwts.builder()
                .setSubject(username)
                .claim("mac", network.getMacAddress())
                .claim("ip", network.getIpAddress())
                .setIssuer(jwtProperties.getIssuer())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, encodedKey)
                .compact();
    }



}
