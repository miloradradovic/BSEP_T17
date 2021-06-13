package bsep.admin.keystore;

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
            logger.error("Failed to initialize keystore because something is wrong with keystore");
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
            logger.error("Failed to create keystore because path is invalid.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Failed to create keystore because algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Failed to create keystore because something is wrong with certificate.");
        }
    }

    public boolean loadKeyStore() {
        try {
            logger.info("Attempting to load keystore.");
            keyStore.load(new FileInputStream(path), password.toCharArray());
            logger.info("Successfully loaded keystore.");
            return true;
        } catch (IOException ioException) {
            logger.error("Failed to load keystore because path is invalid.");
            return false;
        } catch (CertificateException certificateException) {
            logger.error("Failed to load keystore because something is wrong with certificate.");
            return false;
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Failed to load keystore because algorithm is invalid.");
            return false;
        }
    }

    public void saveKeyStore() {
        try {
            logger.info("Attempting to save keystore.");
            keyStore.store(new FileOutputStream(path), password.toCharArray());
            logger.info("Successfully saved keystore.");
        } catch (KeyStoreException keyStoreException) {
            logger.error("Failed to save keystore because something is wrong with keystore.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Failed to save keystore because algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Failed to save keystore because something is wrong with certificate.");
        } catch (IOException ioException) {
            logger.error("Failed to save keystore because path is invalid.");
        }
    }

    public void write(String alias, PrivateKey privateKey, Certificate[] certificateChain) {
        try {
            logger.info("Attempting to write to keystore.");
            keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), certificateChain);
            logger.info("Successfully wrote to keystore.");
        } catch (KeyStoreException e) {
            logger.error("Failed to write to keystore because something is wrong with keystore.");
        }
    }

    public void writeRootCA(String alias, PrivateKey privateKey, Certificate certificate) {
        try {
            logger.info("Attempting to write root ca.");
            keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), new Certificate[]{certificate});
            logger.info("Successfully wrote root ca.");
        } catch (KeyStoreException e) {
            logger.error("Failed to write root ca because something is wrong with keystore.");
        }
    }


}
