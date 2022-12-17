package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(url = "http://localhost:8087/api/authentication", name = "gatewayAuthenticationClient")
public interface GatewayAuthenticationClient {
    @PostMapping("/register")
    @Headers("Content-Type: application/json")
    String register(CreateUserModel createUserModel) throws FeignException;

    @PostMapping("/authenticate")
    @Headers("Content-Type: application/json")
    String authenticate(AuthenticateUserModel authenticateUserModel) throws FeignException;
}