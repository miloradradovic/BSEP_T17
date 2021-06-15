package bsep.hospital.api;

import bsep.hospital.service.CertificateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "https://localhost:4205")
@RestController
@RequestMapping(value = "/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    private static Logger logger = LogManager.getLogger(DeviceController.class);

    // @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<?> saveCertificate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal kcp = (KeycloakPrincipal) authentication.getPrincipal();

        KeycloakSecurityContext session = kcp.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        logger.info("User with the email " + accessToken.getEmail() + " is attempting to ask for certificate revocation.");

        if (certificateService.revokeCertificate(accessToken)) {
            logger.info("Successfully sent request for certificate revocation.");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.info("Failed to sent request for certificate revocation.");
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }


}
