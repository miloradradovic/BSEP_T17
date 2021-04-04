package bsep.hospital.service;

import bsep.hospital.keystore.KeyStoreReader;
import bsep.hospital.keystore.KeyStoreWriter;
import org.bouncycastle.asn1.ocsp.CertID;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Service
public class CertificateService {

    @Autowired
    KeyStoreWriter keyStoreWriter;

    @Autowired
    KeyStoreReader keyStoreReader;

    public void saveCertificate(byte[] encodedCer, String alias) throws CertificateException {

        CertificateFactory factory = CertificateFactory.getInstance("X.509");


        X509Certificate createdCertificate = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(encodedCer));

        keyStoreWriter.write(alias, createdCertificate);
        keyStoreWriter.saveKeyStore();
    }

    public boolean checkCertificate(String alias) throws CertificateException {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>("");
        ResponseEntity<?> responseEntity = restTemplate.exchange("http://localhost:8080/certificate/isValid/" + alias, HttpMethod.POST, request, ResponseEntity.class);
        return responseEntity.getStatusCode() == HttpStatus.OK;

    }
}
