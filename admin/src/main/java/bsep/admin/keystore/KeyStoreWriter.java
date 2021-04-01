package bsep.admin.keystore;

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

    @PostConstruct
    private void init() {
        Security.addProvider(new BouncyCastleProvider());
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException | NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void createKeyStore() {
        try {
            keyStore.load(null, password.toCharArray());
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }
    }

    public boolean loadKeyStore() {
        try {
            keyStore.load(new FileInputStream(path), password.toCharArray());
            return true;
        } catch (IOException | CertificateException | NoSuchAlgorithmException e) {
            return false;
        }
    }

    public void saveKeyStore() {
        try {
            keyStore.store(new FileOutputStream(path), password.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String alias, PrivateKey privateKey, Certificate[] certificateChain) {
        try {
            keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), certificateChain);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    public void writeRootCA(String alias, PrivateKey privateKey, Certificate certificate) {
        try {
            keyStore.setKeyEntry(alias, privateKey, password.toCharArray(), new Certificate[]{certificate});
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }


}
