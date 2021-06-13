package bsep.hospital.keystore;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

@Service
public class KeyStoreWriter {
    @Value("${keystore.path}")
    private String path;

    @Value("${keystore.password}")
    private String password;


    private KeyStore keyStore;

    private static Logger logger = LogManager.getLogger(KeyStoreWriter.class);

    @PostConstruct
    private void init() {
        Security.addProvider(new BouncyCastleProvider());
        try {
            logger.info("Attempting to initialize keystore using bouncy castle provider.");
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException keyStoreException) {
            logger.error("Failed to initialize keystore because something is wrong with keystore.");
        } catch (NoSuchProviderException noSuchProviderException) {
            logger.error("Failed to initialize keystore because provider is not provided.");
        }
    }

    public void createKeyStore() {
        try {
            logger.info("Attempting to create keystore.");
            keyStore.load(null, password.toCharArray());
            logger.info("Successfully created keystore.");
        } catch (IOException ioException) {
            logger.error("Couldn't create keystore because path to keystore is invalid.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Couldn't create keystore because keystore algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Couldn't create keystore because something is wrong with certificate.");
        }
    }

    public boolean loadKeyStore() {
        try {
            logger.info("Attempting to load keystore.");
            keyStore.load(new FileInputStream(path), password.toCharArray());
            logger.info("Successfully loaded keystore.");
            return true;
        } catch (IOException ioException) {
            logger.error("Couldn't load keystore because path to keystore is invalid.");
            return false;
        } catch (CertificateException certificateException) {
            logger.error("Couldn't load keystore because something is wrong with certificate.");
            return false;
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Couldn't load keystore because keystore algorithm is invalid.");
            return false;
        }
    }

    public void saveKeyStore() {
        try {
            logger.info("Attempting to save keystore.");
            keyStore.store(new FileOutputStream(path), password.toCharArray());
            logger.info("Successfully saved keystore.");
        } catch (KeyStoreException keyStoreException) {
            logger.error("Couldn't save keystore because something is wrong with keystore.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Couldn't save keystore because keystore algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Couldn't save keystore because something is wrong with certificate.");
        } catch (IOException ioException) {
            logger.error("Couldn't save keystore because path to keystore is invalid.");
        }
    }


    public void write(String alias, Certificate certificate) {
        try {
            logger.info("Attempting to write alias-certificate in keystore");
            keyStore.setCertificateEntry(alias,certificate);
            logger.info("Successfully wrote alias-certificate in keystore");
        } catch (KeyStoreException e) {
            logger.error("Couldn't write alias-certificate because something is wrong with keystore");
        }
    }


}
