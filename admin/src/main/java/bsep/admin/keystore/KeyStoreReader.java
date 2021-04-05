package bsep.admin.keystore;

import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.model.Issuer;
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

    public Issuer readIssuerFromStore(String alias) throws CertificateNotFoundException {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            keyStore.load(in, password.toCharArray());

            Certificate cert;
            if (keyStore.isKeyEntry(alias)) {
                cert = keyStore.getCertificate(alias);
            } else {
                throw new CertificateNotFoundException();
            }

            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, password.toCharArray());

            X500Name issuerName = new JcaX509CertificateHolder((X509Certificate) cert).getSubject();

            return new Issuer(privateKey, issuerName);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException
                | IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public Certificate[] readCertificateChain(String alias) {
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
            keyStore.load(in, password.toCharArray());

            if (keyStore.isKeyEntry(alias)) {
                return keyStore.getCertificateChain(alias);
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
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
                return (PrivateKey) ks.getKey(alias, password.toCharArray());
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | NoSuchProviderException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;
    }


}
