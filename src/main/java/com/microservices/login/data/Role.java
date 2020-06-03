package com.microservices.login.data;


import com.microservices.login.data.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class Role {

    public Role(RoleType rt){
        this.roleType = rt;
    }

    private Long id;
    private RoleType roleType;
    private Collection<User> users;
    private Collection<Privilege> privileges;
}
