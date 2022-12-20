package nl.tudelft.sem.project.gateway;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.users.CertificateDTO;
import nl.tudelft.sem.project.users.CertificateName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(url = "http://localhost:8087/api/certificates", name = "gatewayCertificatesClient")
public interface GatewayCertificatesClient {
    @GetMapping("/get_certificate_by_id?id={id}")
    @Headers("Content-Type: application/json")
    CertificateDTO getCertificateById(@RequestHeader("Authorization") String bearerToken, @PathVariable(value = "id") UUID id) throws FeignException;

    @GetMapping("/get_certificate_by_name?certificateName={certificateName}")
    @Headers("Content-Type: application/json")
    CertificateDTO getCertificateByName(@RequestHeader("Authorization") String bearerToken, @PathVariable(value = "certificateName") CertificateName certificateName) throws FeignException;

    @GetMapping("")
    @Headers("Content-Type: application/json")
    List<CertificateDTO> getAllAvailableCertificates(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/get_certificate_chain_by_id?id={id}")
    @Headers("Content-Type: application/json")
    List<CertificateDTO> getCertificateChain(@RequestHeader("Authorization") String bearerToken, @PathVariable(value = "id") UUID id) throws FeignException;
}
