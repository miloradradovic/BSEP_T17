package bsep.admin.service;

import bsep.admin.model.Admin;
import bsep.admin.repository.AdminRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    private static Logger logger = LogManager.getLogger(CustomUserDetailsService.class);

    // Funkcija koja na osnovu username-a iz baze vraca objekat User-a
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // ako se ne radi nasledjivanje, paziti gde sve treba da se proveri email
        logger.info("Attempting to load user by email " + email);
        Admin person = adminRepository.findByEmail(email);
        if (person == null) {
            logger.error("No user found with that email " + email);
            throw new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
        } else {
            logger.info("Successfully found user by email " + email);
            return person;
        }
    }
}
