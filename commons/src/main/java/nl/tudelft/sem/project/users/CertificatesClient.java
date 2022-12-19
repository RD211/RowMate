package nl.tudelft.sem.project.users;

import feign.FeignException;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@FeignClient(url = "http://localhost:8084/api/certificates", name = "certificatesClient")
public interface CertificatesClient {
    @PostMapping("/add_certificate")
    @Headers("Content-Type: application/json")
    CertificateDTO addCertificate(CertificateDTO cartificate) throws FeignException;

    @GetMapping("/get_certificate_by_id?id={id}")
    @Headers("Content-Type: application/json")
    CertificateDTO getCertificateById(@PathVariable(value = "id") UUID id) throws FeignException;

    @GetMapping("/get_certificate_by_name?certificateName={name}")
    @Headers("Content-Type: application/json")
    CertificateDTO getCertificateByName(@PathVariable(value = "name") CertificateName name) throws FeignException;

//    @PutMapping("/change_gender")
//    @Headers("Content-Type: application/json")
//    UserDTO changeGenderOfUser(ChangeGenderUserModel changeGenderUserModel) throws FeignException;
//
//    @PutMapping("/change_organization")
//    @Headers("Content-Type: application/json")
//    UserDTO changeOrganizationOfUser(ChangeOrganizationUserModel changeOrganizationUserModel) throws FeignException;
//
//    @PutMapping("/change_amateur")
//    @Headers("Content-Type: application/json")
//    UserDTO changeAmateurOfUser(ChangeAmateurUserModel changeAmateurUserModel) throws FeignException;
//
//    @PostMapping("/add_availability")
//    @Headers("Content-Type: application/json")
//    UserDTO addAvailabilityToUser(AddAvailabilityUserModel addAvailabilityUserModel) throws FeignException;
//
//    @DeleteMapping("/remove_availability")
//    @Headers("Content-Type: application/json")
//    UserDTO removeAvailabilityOfUser(RemoveAvailabilityUserModel removeAvailabilityUserModel) throws FeignException;
//
//    @PostMapping("/add_role")
//    @Headers("Content-Type: application/json")
//    UserDTO addRoleToUser(AddRoleUserModel addRoleUserModel) throws FeignException;
//
//
//    @DeleteMapping("/remove_role")
//    @Headers("Content-Type: application/json")
//    UserDTO removeRoleFromUser(RemoveRoleUserModel removeRoleUserModel) throws FeignException;
//
//    @PostMapping("/add_certificate")
//    @Headers("Content-Type: application/json")
//    UserDTO addCertificateToUser(AddCertificateUserModel addCertificateUserModel) throws FeignException;
//
//
//    @DeleteMapping("/remove_certificate")
//    @Headers("Content-Type: application/json")
//    UserDTO removeCertificateFromUser(RemoveCertificateUserModel removeCertificateUserModel) throws FeignException;
//
//    @DeleteMapping("/delete_user_by_username")
//    @Headers("Content-Type: application/json")
//    void deleteUserByUsername(Username username) throws FeignException;
//
//    @DeleteMapping("/delete_user_by_email")
//    @Headers("Content-Type: application/json")
//    void deleteUserByEmail(UserEmail email) throws FeignException;
}