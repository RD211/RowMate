package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.enums.BoatRole;
import nl.tudelft.sem.project.enums.Gender;
import nl.tudelft.sem.project.shared.DateInterval;
import nl.tudelft.sem.project.shared.Organization;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.UserDTO;
import nl.tudelft.sem.project.utils.Existing;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.validation.Valid;

@FeignClient(url = "http://localhost:8087/api/user", name = "gatewayUserClient")
public interface GatewayUserClient {
    @GetMapping("/get_details")
    @Headers("Content-Type: application/json")
    UserDTO getDetailsOfUser(@RequestHeader("Authorization") String bearerToken) throws FeignException;

    @PostMapping("/change_gender")
    @Headers("Content-Type: application/json")
    UserDTO changeGender(@RequestHeader("Authorization") String bearerToken, Gender gender) throws FeignException;

    @PostMapping("/change_organization")
    @Headers("Content-Type: application/json")
    UserDTO changeOrganization(@RequestHeader("Authorization") String bearerToken, Organization organization) throws FeignException;

    @PostMapping("/change_amateur")
    @Headers("Content-Type: application/json")
    UserDTO changeAmateur(@RequestHeader("Authorization") String bearerToken, boolean isAmateur) throws FeignException;


    @PostMapping("/add_availability")
    @Headers("Content-Type: application/json")
    UserDTO addAvailability(@RequestHeader("Authorization") String bearerToken, DateInterval dateInterval) throws FeignException;

    @PostMapping("/remove_availability")
    @Headers("Content-Type: application/json")
    UserDTO removeAvailability(@RequestHeader("Authorization") String bearerToken, DateInterval dateInterval) throws FeignException;

    @PostMapping("/add_role")
    @Headers("Content-Type: application/json")
    UserDTO addRole(@RequestHeader("Authorization") String bearerToken, BoatRole role) throws FeignException;

    @PostMapping("/remove_role")
    @Headers("Content-Type: application/json")
    UserDTO removeRole(@RequestHeader("Authorization") String bearerToken, BoatRole role) throws FeignException;

    @PostMapping("/add_certificate")
    @Headers("Content-Type: application/json")
    UserDTO addCertificate(@RequestHeader("Authorization") String bearerToken, CertificateDTO certificateDTO) throws FeignException;

    @PostMapping("/remove_certificate")
    @Headers("Content-Type: application/json")
    UserDTO removeCertificate(@RequestHeader("Authorization") String bearerToken, CertificateDTO certificateDTO) throws FeignException;

    @GetMapping("/has_certificate")
    @Headers("Content-Type: application/json")
    boolean hasCertificate(@RequestHeader("Authorization") String bearerToken, CertificateDTO certificateDTO) throws FeignException;
}