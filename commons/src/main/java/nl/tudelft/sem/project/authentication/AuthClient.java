package nl.tudelft.sem.project.authentication;

import feign.FeignException;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "http://localhost:8081/api", name = "authClient")
public interface AuthClient {
    @PostMapping("/authenticate")
    @Headers("Content-Type: application/json")
    Token authenticate(AppUserModel appUserModel) throws FeignException;

    @PostMapping("/register")
    @Headers("Content-Type: application/json")
    void register(AppUserModel appUserModel) throws FeignException;
}