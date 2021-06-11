package bsep.hospital.service;

import bsep.hospital.model.Person;
import bsep.hospital.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private PersonRepository personRepository;

    public Person loadUserByEmail(String email) {
        return personRepository.findByEmail(email);
    }

}

