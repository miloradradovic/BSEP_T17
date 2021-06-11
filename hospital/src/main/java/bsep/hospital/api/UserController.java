package bsep.hospital.api;

import bsep.hospital.dto.CertificateRequestDTO;
import bsep.hospital.dto.PersonDTO;
import bsep.hospital.model.Person;
import bsep.hospital.service.CertificateRequestService;
import bsep.hospital.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Arrays;

@CrossOrigin(origins = "https://localhost:4205")
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    UserService userService;

    private static Logger logger = LogManager.getLogger(UserController.class);



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getUserByEmail(@RequestParam @Email() @Pattern(regexp = "[^;]+") String email) {
       // Person person = userService.loadUserByEmail(email);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal kcp = (KeycloakPrincipal) authentication.getPrincipal();

        KeycloakSecurityContext session = kcp.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();

        if (accessToken != null) {
            logger.info("User with the email " + accessToken.getEmail() + " is trying to retrieve user data.");
            PersonDTO dto = new PersonDTO(accessToken.getEmail(), accessToken.getName(), accessToken.getFamilyName());
            logger.info("User with the email " + accessToken.getEmail() + " successfully retrieved user data.");
            return new ResponseEntity<>(dto,HttpStatus.OK);
        } else {
            logger.warn("Currently logged user couldn't retrieve user data because access token is null.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
