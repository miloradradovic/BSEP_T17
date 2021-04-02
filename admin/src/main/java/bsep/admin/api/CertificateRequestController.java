package bsep.admin.api;

import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.service.CerRequestInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/certificate-request", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateRequestController {
    //kontroler za pregled zahteva
    //kontroler za prihvatanje zahteva
    //kontroler za odbijanje zahteva

    @Autowired
    CerRequestInfoService cerRequestInfoService;

    @PostMapping("/send-certificate-request")
    public ResponseEntity<?> sendCertificateRequest(@RequestBody byte[] encryptedCSR) throws IOException, CertificateNotFoundException {

        boolean success = cerRequestInfoService.createCertificateRequest(encryptedCSR);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
