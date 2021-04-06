package bsep.hospital.api;

import bsep.hospital.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

    @Autowired
    CertificateService certificateService;
/*
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> saveCertificate(@RequestBody byte[] encodedCer) {
        try {
            certificateService.saveCertificate(encodedCer);

            return new ResponseEntity<>(HttpStatus.OK);

        } catch (CertificateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }*/


}
