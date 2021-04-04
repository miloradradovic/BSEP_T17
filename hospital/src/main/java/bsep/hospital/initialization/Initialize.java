package bsep.hospital.initialization;


import bsep.hospital.keystore.KeyStoreWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class Initialize {

    private static final Logger LOG = Logger.getLogger(bsep.hospital.initialization.Initialize.class.getName());

    @Autowired
    KeyStoreWriter keyStoreWriter;


    @PostConstruct
    public void init() {
        LOG.info("Executing operation Initialize");
        createKeyStore();

    }

    public void createKeyStore() {
        if (!keyStoreWriter.loadKeyStore())
        {
            keyStoreWriter.createKeyStore();
            keyStoreWriter.saveKeyStore();
        }

    }
}
