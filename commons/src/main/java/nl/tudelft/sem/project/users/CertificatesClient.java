package nl.tudelft.sem.project.users;

import feign.FeignException;
import feign.Headers;
import nl.tudelft.sem.project.users.models.ChangeCertificateNameModel;
import nl.tudelft.sem.project.users.models.ChangeCertificateSupersededModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@FeignClient(url = "http://localhost:8084/api/certificates", name = "certificatesClient")
public interface CertificatesClient {
    @PostMapping("/add_certificate")
    @Headers("Content-Type: application/json")
    CertificateDTO addCertificate(CertificateDTO cartificate) throws FeignException;

    @GetMapping("/get_certificate_by_id?id={id}")
    @Headers("Content-Type: application/json")
    CertificateDTO getCertificateById(@PathVariable(value = "id") UUID id) throws FeignException;

    @GetMapping("/get_certificate_by_name?certificateName={certificateName}")
    @Headers("Content-Type: application/json")
    CertificateDTO getCertificateByName(@PathVariable(value = "certificateName") CertificateName certificateName) throws FeignException;


    @PutMapping("/change_certificate_name")
    @Headers("Content-Type: application/json")
    CertificateDTO changeCertificateName(ChangeCertificateNameModel changeCertificateNameModel);

    @PutMapping("/change_certificate_superseded")
    @Headers("Content-Type: application/json")
    CertificateDTO changeCertificateSuperseded(ChangeCertificateSupersededModel changeCertificateSupersededModel);

    @GetMapping("")
    @Headers("Content-Type: application/json")
    List<CertificateDTO> getAllAvailableCertificates();

    @GetMapping("/get_certificate_chain_by_id?id={id}")
    @Headers("Content-Type: application/json")
    List<CertificateDTO> getCertificateChain(@PathVariable(value = "id") UUID id) throws FeignException;

}