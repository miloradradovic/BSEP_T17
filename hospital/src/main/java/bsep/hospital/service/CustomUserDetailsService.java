package bsep.hospital.service;

import bsep.hospital.model.Person;
import bsep.hospital.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    private static Logger logger = LogManager.getLogger(CustomUserDetailsService.class);

    // Funkcija koja na osnovu email-a iz baze vraca objekat User-a
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // ako se ne radi nasledjivanje, paziti gde sve treba da se proveri email
            logger.info("Attempting to load user by email " + email);
            Person person = personRepository.findByEmail(email);
            if (person == null) {
                logger.error("Failed to find person with email " + email);
                throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
            } else {
                logger.info("Successfully found person with email " + email);
                return person;
        }
    }
}
