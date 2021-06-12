package bsep.admin.keystore;

import bsep.admin.api.CertificateController;
import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.model.Issuer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
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
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Service
public class KeyStoreReader {

    @Value("${keystore.path}")
    private String path;

    @Value("${keystore.password}")
    private String password;

    private static Logger logger = LogManager.getLogger(KeyStoreReader.class);


    private KeyStore keyStore;

    @PostConstruct
    private void init() {
        logger.info("Attempting to initialize keystore using bouncy castle provider.");
        Security.addProvider(new BouncyCastleProvider());
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException keyStoreException) {
            logger.error("Failed to initialize keystore because something is wrong with keystore.");
        } catch (NoSuchProviderException noSuchProviderException) {
            logger.error("Failed to initialize keystore because provider is not provided.");
        }
    }

    public Issuer readIssuerFromStore(String alias) throws CertificateNotFoundException {
        try {
            logger.info("Attempting to read issuer from store. Alias: " + alias);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            keyStore.load(in, password.toCharArray());

            Certificate cert;
            logger.info("Attempting to get certificate by alias.");
            if (keyStore.isKeyEntry(alias)) {
                cert = keyStore.getCertificate(alias);
                logger.info("Successfully got certificate by alias.");
            } else {
                throw new CertificateNotFoundException();
            }

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());

            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();

            logger.info("Successfully returned issuer.");
            return new Issuer(privateKey, issuerName);
        } catch (KeyStoreException keyStoreException) {
            logger.error("Couldn't get the issuer because something is wrong with keystore.");
        } catch (CertificateNotFoundException certificateNotFoundException) {
            logger.error("Couldn't get the issuer because certificate is not found.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Couldn't get the issuer because keystore algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Couldn't get the issuer because something is wrong with certificate.");
        } catch (UnrecoverableKeyException unrecoverableKeyException) {
            logger.error("Couldn't get the issuer because key is unrecoverable.");
        } catch (IOException ioException) {
            logger.error("Couldn't get the issuer because path to keystore is invalid.");
        }
        return null;
    }

    public Certificate readCertificate(String alias) {
        try {
            logger.info("Attempting to get certificate by alias " + alias);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            keyStore.load(in, password.toCharArray());
            logger.info("Successfully retrieved certificate by alias " + alias);
            return keyStore.isKeyEntry(alias) ? keyStore.getCertificate(alias) : null;


        } catch (KeyStoreException keyStoreException) {
            logger.error("Couldn't get certificate by alias because something is wrong with keystore.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Couldn't get certificate by alias because keystore algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Couldn't get certificate by alias because something is wrong with certificate.");
        } catch (IOException ioException) {
            logger.error("Couldn't get certificate by alias because keystore path is invalid.");
        }
        return null;
    }

    public Certificate[] readCertificateChain(String alias) {
        try {
            logger.info("Attempting to read certificate chain by alias " + alias);
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            keyStore.load(in, password.toCharArray());

            if (keyStore.isKeyEntry(alias)) {
                logger.info("Successfully retrieved certificate chain by alias " + alias);
                return keyStore.getCertificateChain(alias);
            }
        } catch (KeyStoreException keyStoreException) {
            logger.error("Couldn't retrieve certificate chain by alias because something is wrong with keystore.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Couldn't retrieve certificate chain by alias because keystore algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Couldn't retrieve certificate chain by alias because something is wrong with certificate.");
        } catch (IOException ioException) {
            logger.error("Couldn't retrieve certificate chain by alias because keystore path is invalid.");
        }
        return null;
    }

    public Enumeration<String> getAllAliases() {
        try {
            logger.info("Attempting to get all aliases.");
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            keyStore.load(in, password.toCharArray());
            logger.info("Successfully retrieved all aliases.");
            return keyStore.aliases();
        } catch (KeyStoreException keyStoreException) {
            logger.error("Couldn't retrieve all aliases because something is wrong with keystore.");
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            logger.error("Couldn't retrieve all aliases because keystore algorithm is invalid.");
        } catch (CertificateException certificateException) {
            logger.error("Couldn't retrieve all aliases because something is wrong with certificate.");
        } catch (IOException ioException) {
            logger.error("Couldn't retrieve all aliases because keystore path is invalid.");
        }
        return null;
    }

    public PrivateKey readPrivateKey(String alias) {
        try {
            //kreiramo instancu KeyStore
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            //ucitavamo podatke
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            ks.load(in, password.toCharArray());

            if (ks.isKeyEntry(alias)) {
                return (PrivateKey) ks.getKey(alias, password.toCharArray());
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | NoSuchProviderException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
