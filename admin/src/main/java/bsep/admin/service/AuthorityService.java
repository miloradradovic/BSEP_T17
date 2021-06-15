package bsep.admin.service;

import bsep.admin.api.AuthenticationController;
import bsep.admin.model.Authority;
import bsep.admin.repository.AuthorityRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    private static Logger logger = LogManager.getLogger(AuthorityService.class);

    public List<Authority> findById(Long id) {
        logger.info("Attempting to find authorities by id.");
        List<Authority> auths = new ArrayList<>();
        Authority auth = this.authorityRepository.findById(id).orElse(null);
        if(auth != null)
            auths.add(auth);
        logger.info("Found authorities by id.");
        return auths;
    }

}