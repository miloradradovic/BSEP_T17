package bsep.hospital.initialization;


import bsep.hospital.api.CertificateRequestController;
import bsep.hospital.keystore.KeyStoreWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Initialize {

    private static Logger logger = LogManager.getLogger(Initialize.class);

    @Autowired
    KeyStoreWriter keyStoreWriter;


    @PostConstruct
    public void init() {
        logger.info("Executing operation Initialize");
        createKeyStore();

    }

    public void createKeyStore() {
        logger.info("Attempting to create keystore.");
        logger.info("Checking if keystore already exists.");
        if (!keyStoreWriter.loadKeyStore())
        {
            logger.info("Keystore doesn't exist. Attempting to create it.");
            keyStoreWriter.createKeyStore();
            keyStoreWriter.saveKeyStore();
        } else {
            logger.info("Keystore already exists.");
        }

    }
}
