package com.microservices.login.data;


import com.microservices.login.data.enums.PrivilegeType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class Privilege {

    public Privilege(PrivilegeType pt){
        this.privilegeType = pt;
    }

    private Long id;
    private PrivilegeType privilegeType;
    private Collection<Role> roles;
}