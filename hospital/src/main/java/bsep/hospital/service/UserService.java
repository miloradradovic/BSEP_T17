package bsep.hospital.service;

import bsep.hospital.model.Person;
import bsep.hospital.repository.PersonRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PersonRepository personRepository;

    private static Logger logger = LogManager.getLogger(UserService.class);

    public Person loadUserByEmail(String email) {
        logger.info("Finding user by email " + email);
        return personRepository.findByEmail(email);
    }

}

