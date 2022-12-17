package nl.tudelft.sem.project.users;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.shared.Username;
import nl.tudelft.sem.project.users.models.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(url = "http://localhost:8084/api/users", name = "usersClient")
public interface UsersClient {
    @PostMapping("/add_user")
    @Headers("Content-Type: application/json")
    UserDTO addUser(UserDTO owner) throws FeignException;

    @GetMapping("/get_user_by_username?username={username}")
    @Headers("Content-Type: application/json")
    UserDTO getUserByUsername(@PathVariable(value = "username") Username username) throws FeignException;

    @PutMapping("/change_gender")
    @Headers("Content-Type: application/json")
    UserDTO changeGenderOfUser(ChangeGenderUserModel changeGenderUserModel) throws FeignException;

    @PutMapping("/change_organization")
    @Headers("Content-Type: application/json")
    UserDTO changeOrganizationOfUser(ChangeOrganizationUserModel changeOrganizationUserModel) throws FeignException;

    @PutMapping("/change_amateur")
    @Headers("Content-Type: application/json")
    UserDTO changeAmateurOfUser(ChangeAmateurUserModel changeAmateurUserModel) throws FeignException;

    @PostMapping("/add_availability")
    @Headers("Content-Type: application/json")
    UserDTO addAvailabilityToUser(AddAvailabilityUserModel addAvailabilityUserModel) throws FeignException;

    @DeleteMapping("/remove_availability")
    @Headers("Content-Type: application/json")
    UserDTO removeAvailabilityOfUser(RemoveAvailabilityUserModel removeAvailabilityUserModel) throws FeignException;

    @PostMapping("/add_role")
    @Headers("Content-Type: application/json")
    UserDTO addRoleToUser(AddRoleUserModel addRoleUserModel) throws FeignException;


    @DeleteMapping("/remove_role")
    @Headers("Content-Type: application/json")
    UserDTO removeRoleFromUser(RemoveRoleUserModel removeRoleUserModel) throws FeignException;

    @PostMapping("/add_certificate")
    @Headers("Content-Type: application/json")
    UserDTO addCertificateToUser(AddCertificateUserModel addCertificateUserModel) throws FeignException;


    @DeleteMapping("/remove_certificate")
    @Headers("Content-Type: application/json")
    UserDTO removeCertificateFromUser(RemoveCertificateUserModel removeCertificateUserModel) throws FeignException;
}