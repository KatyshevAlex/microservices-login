# microservices-login
This service asks a JWT token from DAO-service by passing log/pass

This service recives log/pass on one of its controllers, then crypt password and sends it to DAO-service by @FeignClient

If log/pass were correct, it return JWT.

There are several interesting things inside: 

--custom annotation with useing aspects 

--email RegEx 

--@FeignClient

--custom exceptions

--it works as Eureka client
