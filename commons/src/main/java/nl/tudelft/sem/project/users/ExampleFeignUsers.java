package nl.tudelft.sem.project.users;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface ExampleFeignUsers {
    @RequestLine("POST /add_user")
    @Headers("Content-Type: application/json")
    UserDTO addUser(UserDTO owner);

    @RequestLine("GET /get_user/{username}")
    void getUserByUsername(@Param("username") String username);
}