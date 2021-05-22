package bsep.hospital.api;

import bsep.hospital.dto.CertificateRequestDTO;
import bsep.hospital.dto.PersonDTO;
import bsep.hospital.model.Person;
import bsep.hospital.service.CertificateRequestService;
import bsep.hospital.service.UserService;
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

import java.util.ArrayList;
import java.util.Arrays;


@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    UserService userService;


    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
       // Person person = userService.loadUserByEmail(email);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal kcp = (KeycloakPrincipal) authentication.getPrincipal();

        KeycloakSecurityContext session = kcp.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        if (accessToken != null) {
            PersonDTO dto = new PersonDTO(accessToken.getEmail(), accessToken.getName(), accessToken.getFamilyName());
            return new ResponseEntity<>(dto,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
