package bsep.admin.api;

import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.service.AuthorityService;
import bsep.admin.service.CerRequestInfoService;
import org.apache.commons.codec.DecoderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://localhost:4200")
@RestController
@RequestMapping(value = "/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    /*@Autowired
    private AuthenticationManager authenticationManager;*/

    @Autowired
    AuthorityService authorityService;

    @Autowired
    CerRequestInfoService cerRequestInfoService;


    public AuthenticationController() {
    }

    // Prvi endpoint koji pogadja korisnik kada se loguje.
    // Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
    /*@PostMapping("/log-in")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody UserLoginDTO authenticationRequest, HttpServletResponse response) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));

        // Ubaci korisnika u trenutni security kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        Admin person = (Admin) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(person.getEmail(), person.getId(), person.getAuthorities().get(0).getAuthority()); // prijavljujemo se na sistem sa email adresom


        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenStateDTO(jwt));
    }*/

    @RequestMapping(value = "/verify-certificate-request/{encrypted}", method = RequestMethod.GET)
    public ResponseEntity<?> verifyCertificateRequest(@PathVariable String encrypted) throws DecoderException, CertificateNotFoundException {

        boolean success = cerRequestInfoService.verifyCertificateRequest(encrypted);
        if (success) {
            return new ResponseEntity<>("Successfully verified!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

        /*
    // U slucaju isteka vazenja JWT tokena, endpoint koji se poziva da se token osvezi
    @PostMapping(value = "/refresh")
    public ResponseEntity<UserTokenStateDTO> refreshAuthenticationToken(HttpServletRequest request) {

        String token = tokenUtils.getToken(request);
        String username = this.tokenUtils.getUsernameFromToken(token);
        Person person = (Person) this.userDetailsService.loadUserByUsername(username);

        if (this.tokenUtils.canTokenBeRefreshed(token, person.getLastPasswordResetDate())) {
            String refreshedToken = tokenUtils.refreshToken(token);
            int expiresIn = tokenUtils.getExpiredIn();

            return ResponseEntity.ok(new UserTokenStateDTO(refreshedToken));
        } else {
            UserTokenStateDTO userTokenState = new UserTokenStateDTO();
            return ResponseEntity.badRequest().body(userTokenState);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') || hasRole('ROLE_REGISTERED')")
    public void updatedLoggedIn(String username, String password){

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    */
}
