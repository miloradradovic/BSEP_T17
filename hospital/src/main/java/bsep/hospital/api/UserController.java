package bsep.hospital.api;

import bsep.hospital.dto.CertificateRequestDTO;
import bsep.hospital.dto.PersonDTO;
import bsep.hospital.model.Person;
import bsep.hospital.service.CertificateRequestService;
import bsep.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    UserService userService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        Person person = userService.loadUserByEmail(email);
        if (person != null) {
            PersonDTO dto = new PersonDTO(person.getEmail(), person.getName(), person.getSurname());
            return new ResponseEntity<>(dto,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
