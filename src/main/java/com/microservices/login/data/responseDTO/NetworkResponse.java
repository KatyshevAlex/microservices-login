package com.microservices.login.data.responseDTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class NetworkResponse implements Serializable {
    private String ipAddress;
    private String macAddress;
}
