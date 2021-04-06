package bsep.hospital.service;

import bsep.hospital.model.Authority;
import bsep.hospital.model.Person;
import bsep.hospital.repository.AuthorityRepository;
import bsep.hospital.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private PersonRepository personRepository;

    public Person loadUserByEmail(String email){
        return personRepository.findByEmail(email);
    }

}

