package com.microservices.login.feignInterface;


import com.microservices.login.data.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "dao-service")
@Service
@RequestMapping("/dao")
public interface UserInterface {

    @RequestMapping(method = RequestMethod.POST, value = "/find-user")
    User searchUser(@RequestBody User user);

    @RequestMapping(method = RequestMethod.PUT, value = "/update-user")
    void updateUser(@RequestBody User user);
}
