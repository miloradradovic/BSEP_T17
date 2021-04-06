package bsep.hospital.api;

import bsep.hospital.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveCertificate(@RequestBody byte[] encodedCer) {
        try {
            certificateService.saveCertificate(encodedCer);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (CertificateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


}
