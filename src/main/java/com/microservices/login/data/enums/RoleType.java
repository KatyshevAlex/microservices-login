package com.microservices.login.data.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum RoleType {
    ROLE_USER("USER", //ROLE_ will be added by Spring
            1,
            Arrays.asList(
                    PrivilegeType.READ)),

    ROLE_PREMIUM_USER("PREMIUM_USER", //ROLE_ will be added by Spring
            2,
            Arrays.asList(
                    PrivilegeType.READ)),

    ROLE_ADMIN("ADMIN", //ROLE_ will be added by Spring
            3,
            Arrays.asList(
                    PrivilegeType.READ,
                    PrivilegeType.WRITE)),

    ROLE_APPLICATION("APPLICATION", //ROLE_ will be added by Spring
            4,
            Arrays.asList(
                    PrivilegeType.READ,
                    PrivilegeType.WRITE,
                    PrivilegeType.DELETE)),

    ROLE_NO_ROLE("NO_ROLE", //ROLE_ will be added by Spring
            0,
            Arrays.asList()) ;

    private final String role;
    private final int priority;
    private final List<PrivilegeType> privilegesType;

    public String[] getThisAndHigherPriorities(){
        return Arrays.stream(RoleType.values()).filter(a-> a.getPriority() >= this.priority).map(RoleType::getRole).toArray(String[]::new);
    }

    public String[] getAllRoles(){
        return Arrays.stream(RoleType.values()).map(RoleType::getRole).toArray(String[]::new);
    }

}
