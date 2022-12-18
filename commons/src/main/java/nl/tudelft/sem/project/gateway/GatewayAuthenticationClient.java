package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.authentication.Password;
import nl.tudelft.sem.project.authentication.ResetPasswordModel;
import nl.tudelft.sem.project.shared.Username;
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

    @PostMapping("/reset_password_with_previous")
    @Headers("Content-Type: application/json")
    void resetPasswordWithPrevious(ResetPasswordModel resetPasswordModel) throws FeignException;

    @PostMapping("/reset_password_with_email")
    @Headers("Content-Type: application/json")
    void resetPasswordWithEmail(Username username) throws FeignException;

    @PostMapping("/reset_password?token={token}")
    @Headers("Content-Type: application/json")
    void resetPassword(@PathVariable(value = "token") String token,
                            String newPassword) throws FeignException;
}