package bsep.hospital.service;

import bsep.hospital.keystore.KeyStoreWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Service
public class CertificateService {

    @Autowired
    KeyStoreWriter keyStoreWriter;

    public void saveCertificate(byte[] encodedCer, String alias) throws CertificateException {

        CertificateFactory factory = CertificateFactory.getInstance("X.509");


        X509Certificate createdCertificate = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(encodedCer));

        keyStoreWriter.write(alias, createdCertificate);
        keyStoreWriter.saveKeyStore();
    }
}
