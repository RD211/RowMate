package nl.tudelft.sem.project.authentication;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.shared.Username;
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

    @PostMapping("/reset_password_with_previous")
    @Headers("Content-Type: application/json")
    void resetPasswordWithPrevious(ResetPasswordModel resetPasswordModel) throws FeignException;

    @PostMapping("/get_email_reset_password_token")
    @Headers("Content-Type: application/json")
    String getEmailResetPasswordToken(Username username) throws FeignException;

    @PostMapping("/reset_password_email?token={token}")
    @Headers("Content-Type: application/json")
    void emailResetPassword(@PathVariable(value = "token") String token,
                            Password newPassword) throws FeignException;
}