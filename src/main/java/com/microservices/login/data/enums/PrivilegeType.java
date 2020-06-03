package com.microservices.login.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum PrivilegeType {
    READ,
    WRITE,
    DELETE
}