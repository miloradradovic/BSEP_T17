package bsep.hospital.keystore;


import bsep.hospital.initialization.Initialize;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Enumeration;

@Service
public class KeyStoreReader {

    @Value("${keystore.path}")
    private String path;

    @Value("${keystore.password}")
    private String password;


    private KeyStore keyStore;

    private static Logger logger = LogManager.getLogger(KeyStoreReader.class);



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


    public Certificate readCertificate(String alias) {
        try {
            logger.info("Attempting to find certificate by alias " + alias);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            keyStore.load(in, password.toCharArray());
            return keyStore.isKeyEntry(alias) ? keyStore.getCertificate(alias) : null;


        } catch (KeyStoreException keyStoreException) {
            logger.error("Couldn't find certificate for alias " + alias + " because something is wrong with keystore.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Couldn't find certificate for alias " + alias + " because keystore algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Couldn't find certificate for alias " + alias + " because something is wrong with certificate.");
        } catch (IOException ioException) {
            logger.error("Couldn't find certificate for alias " + alias + " because path to keystore is invalid.");
        }
        return null;
    }

    public Enumeration<String> getAllAliases() {
        try {
            logger.info("Attempting to retrieve all aliases from keystore.");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            keyStore.load(in, password.toCharArray());
            return keyStore.aliases();
        } catch (KeyStoreException keyStoreException) {
            logger.error("Couldn't retrieve aliases from keystore because something is wrong with keystore.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Couldn't retrieve aliases from keystore because keystore algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Couldn't retrieve aliases from keystore because something is wrong with certificate.");
        } catch (IOException ioException) {
            logger.error("Couldn't retrieve aliases from keystore because path to keystore is invalid.");
        }
        return null;
    }


    public PrivateKey readPrivateKey(String alias) {
        try {
            logger.info("Attempting to read private key.");
            //kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            //ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            ks.load(in, password.toCharArray());

            if (ks.isKeyEntry(alias)) {
                logger.info("Attempting to retrieve private key from keystore.");
                PrivateKey pk = (PrivateKey) ks.getKey(alias, password.toCharArray());
                return pk;
            }
        } catch (KeyStoreException keyStoreException) {
            logger.error("Failed to retrieve private key from keystore because something is wrong with keystore.");
        } catch (NoSuchProviderException noSuchProviderException) {
            logger.error("Failed to retrieve private key from keystore because provider is not provided.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Failed to retrieve private key from keystore because keystore algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Failed to retrieve private key from keystore because something is wrong with certificate.");
        } catch (IOException ioException) {
            logger.error("Failed to retrieve private key from keystore because path to keystore is invalid.");
        } catch (UnrecoverableKeyException unrecoverableKeyException) {
            logger.error("Failed to retrieve private key from keystore because something key is unrecoverable.");
        }
        return null;
    }


}
