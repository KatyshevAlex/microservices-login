package com.microservices.login.data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.microservices.login.data.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;

@Data
@NoArgsConstructor
public class Role {

    public Role(RoleType rt){
        this.roleType = rt;
    }

    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @JsonIgnoreProperties("roles")
    private Collection<User> users;

    @JsonIgnoreProperties("roles")
    private Collection<Privilege> privileges;
}
