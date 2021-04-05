package bsep.hospital.service;

import bsep.hospital.keystore.KeyStoreReader;
import bsep.hospital.keystore.KeyStoreWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

@Service
public class CertificateService {

    @Autowired
    KeyStoreWriter keyStoreWriter;

    @Autowired
    KeyStoreReader keyStoreReader;

    public void saveCertificate(byte[] encodedCer, String email) throws CertificateException {

        CertificateFactory factory = CertificateFactory.getInstance("X.509");


        X509Certificate createdCertificate = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(encodedCer));

        String alias = generateAlias(email);

        keyStoreWriter.write(alias, createdCertificate);
        keyStoreWriter.saveKeyStore();
    }

    public boolean checkCertificate(String email) throws CertificateException {

        String alias = getLastAlias(email);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>("");
        ResponseEntity<?> responseEntity = restTemplate.exchange("http://localhost:8080/certificate/isValid/" + alias, HttpMethod.POST, request, ResponseEntity.class);
        return responseEntity.getStatusCode() == HttpStatus.OK;

    }

    private String generateAlias(String email) {

        int lastAliasNumber = 0;
        Enumeration<String> aliases = keyStoreReader.getAllAliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (alias.contains(email) && alias.contains("|")) {
                int number = Integer.parseInt(alias.split("\\|")[1]);
                lastAliasNumber = Math.max(number, lastAliasNumber);
            }

        }
        if (lastAliasNumber != 0) {
            email += lastAliasNumber + 1;
        }


        return email;
    }

    private String getLastAlias(String email) {

        int lastAliasNumber = 0;
        Enumeration<String> aliases = keyStoreReader.getAllAliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (alias.contains(email) && alias.contains("|")) {
                int number = Integer.parseInt(alias.split("\\|")[1]);
                lastAliasNumber = Math.max(number, lastAliasNumber);
            }

        }
        if (lastAliasNumber != 0) {
            email += lastAliasNumber;
        }


        return email;
    }
}
