package bsep.hospital.keystore;


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

    @PostConstruct
    private void init() {
        Security.addProvider(new BouncyCastleProvider());
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }


    public Certificate readCertificate(String alias) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            keyStore.load(in, password.toCharArray());

            return keyStore.isKeyEntry(alias) ? keyStore.getCertificate(alias) : null;


        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException
                | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Enumeration<String> getAllAliases() {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            keyStore.load(in, password.toCharArray());

            return keyStore.aliases();
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
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
                PrivateKey pk = (PrivateKey) ks.getKey(alias, password.toCharArray());
                return pk;
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | CertificateException | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


}
