package bsep.admin.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/certificate-request", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateRequestController {
    //kontroler za pregled zahteva
    //kontroler za prihvatanje zahteva
    //kontroler za odbijanje zahteva
}
