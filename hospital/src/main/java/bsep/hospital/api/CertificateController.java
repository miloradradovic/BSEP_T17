package bsep.hospital.api;

import bsep.hospital.model.Person;
import bsep.hospital.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.cert.CertificateException;

@RestController
@RequestMapping(value = "/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveCertificate(@RequestBody byte[] encodedCer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person loggedIn = (Person) authentication.getPrincipal();
        try {
            certificateService.saveCertificate(encodedCer, loggedIn.getEmail());

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (CertificateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


}
