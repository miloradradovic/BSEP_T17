package bsep.hospital.service;

import bsep.hospital.keystore.KeyStoreReader;
import bsep.hospital.keystore.KeyStoreWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateEncodingException;
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

    private static Logger logger = LogManager.getLogger(CertificateService.class);

    public void saveCertificate(byte[] encodedCer){

        CertificateFactory factory = null;
        logger.info("Attempting to generate certificate factory.");
        try {
            factory = CertificateFactory.getInstance("X.509");
            logger.info("Successfully generated certificate factory.");
        } catch (CertificateException certificateException) {
            logger.error("Failed to generate certificate factory");
        }


        X509Certificate createdCertificate = null;
        logger.info("Attempting to generate certificate using factory.");
        try {
            createdCertificate = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(encodedCer));
            logger.info("Successfully generated certificate using factory.");
        } catch (CertificateException certificateException) {
            logger.error("Failed to generate certificate using factory.");
        }

        JcaX509CertificateHolder certHolder = null;
        logger.info("Attempting to generate certificate holder.");
        try {
            certHolder = new JcaX509CertificateHolder(createdCertificate);
            logger.info("Successfully generated certificate holder.");
        } catch (CertificateEncodingException e) {
            logger.error("Failed to generate certificate holder.");
        }

        X500Name name = certHolder.getSubject();

        String alias = generateAlias(IETFUtils.valueToString(name.getRDNs(BCStyle.E)[0].getFirst().getValue()));

        keyStoreWriter.write(alias, createdCertificate);
        keyStoreWriter.saveKeyStore();
    }

    public boolean revokeCertificate(AccessToken accessToken) {

        logger.info("Sending request to super admin to revoke certificate.");

        String email = accessToken.getEmail();

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>("");
        ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8084/certificate-request/send-certificate-revocation-request/" + email , HttpMethod.POST, request, ResponseEntity.class);
        return responseEntity.getStatusCode() == HttpStatus.OK;

    }

    public boolean checkCertificateValidation(AccessToken accessToken) {

        logger.info("Sending request to super admin to verify certificate.");

        String email = accessToken.getEmail();

        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = new HttpEntity<>("");
            ResponseEntity<?> responseEntity = restTemplate.exchange("https://localhost:8084/certificate-request/check-certificate-valid/" + email , HttpMethod.GET, request, ResponseEntity.class);
            logger.info("Successfully sent request to super admin to verify certificate.");
            return responseEntity.getStatusCode() == HttpStatus.OK;
        } catch (Exception e) {
            logger.error("Failed to send request to super admin to verify certificate.");
            return false;
        }


    }

    private String generateAlias(String email) {

        logger.info("Attempting to generate alias.");
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

        logger.info("Generated alias.");
        return email;
    }

    private String getLastAlias(String email) {

        logger.info("Attempting to get the last alias.");
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

        logger.info("Retrieved the last alias.");
        return email;
    }
}
