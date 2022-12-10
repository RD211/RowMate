package nl.tudelft.sem.project.users;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.users.models.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(url = "http://localhost:8084/api/users", name = "usersClient")
public interface UsersClient {
    @PostMapping("/add_user")
    @Headers("Content-Type: application/json")
    UserDTO addUser(UserDTO owner) throws FeignException;

    @GetMapping("/get_user_by_id?userId={userId}")
    @Headers("Content-Type: application/json")
    UserDTO getUserById(@PathVariable(value = "userId") UUID userId) throws FeignException;

    @PutMapping("/change_gender")
    @Headers("Content-Type: application/json")
    UserDTO changeGenderOfUser(ChangeGenderUserModel changeGenderUserModel) throws FeignException;

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
}