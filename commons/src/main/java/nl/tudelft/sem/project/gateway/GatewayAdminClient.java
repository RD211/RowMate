package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "http://localhost:8087/api/admin", name = "gatewayAdminClient")
public interface GatewayAdminClient {
    @DeleteMapping("/delete_user_by_username")
    @Headers("Content-Type: application/json")
    void deleteUserByUsername(@RequestHeader("Authorization") String bearerToken, String username) throws FeignException;

    @DeleteMapping("/delete_user_by_email")
    @Headers("Content-Type: application/json")
    void deleteUserByEmail(@RequestHeader("Authorization") String bearerToken,String email) throws FeignException;
}