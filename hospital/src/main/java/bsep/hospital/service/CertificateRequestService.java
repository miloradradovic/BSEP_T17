package bsep.hospital.service;

import bsep.hospital.dto.CertificateRequestDTO;
import bsep.hospital.model.Person;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.*;

@Service
public class CertificateRequestService {

    @PostConstruct
    private void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public boolean sendCertificateRequest(CertificateRequestDTO certificateRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Person loggedIn = (Person) authentication.getPrincipal();
        if (!loggedIn.getEmail().equals(certificateRequestDTO.getEmail())){ // lose poslat email sa fronta
            return false;
        }
        certificateRequestDTO.setUserId(loggedIn.getId());
        certificateRequestDTO.setEmail(loggedIn.getEmail());
        byte[] csr = generateCSR(certificateRequestDTO);
        if (csr != null){
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<byte[]> request = new HttpEntity<>(csr);
            ResponseEntity responseEntity = restTemplate.exchange("http://localhost:8080/certificate-request/send-certificate-request", HttpMethod.POST, request, ResponseEntity.class);
            return responseEntity.getStatusCode() == HttpStatus.OK;
        }
        return false;
    }

    private byte[] generateCSR(CertificateRequestDTO certificateRequestDTO) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048, new SecureRandom());
            KeyPair keypair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keypair.getPublic();
            PrivateKey privateKey = keypair.getPrivate();
            String commonName = certificateRequestDTO.getEmail().split("@")[1];
            X500NameBuilder x500NameBuilder = new X500NameBuilder(BCStyle.INSTANCE);
            x500NameBuilder.addRDN(BCStyle.CN, commonName);
            x500NameBuilder.addRDN(BCStyle.SURNAME, certificateRequestDTO.getSurname());
            x500NameBuilder.addRDN(BCStyle.GIVENNAME, certificateRequestDTO.getGivenName());
            x500NameBuilder.addRDN(BCStyle.O, certificateRequestDTO.getOrganization());
            x500NameBuilder.addRDN(BCStyle.OU, certificateRequestDTO.getOrganizationUnit());
            x500NameBuilder.addRDN(BCStyle.C, certificateRequestDTO.getCountry());
            x500NameBuilder.addRDN(BCStyle.E, certificateRequestDTO.getEmail());
            x500NameBuilder.addRDN(BCStyle.UID, String.valueOf(certificateRequestDTO.getUserId()));
            X500Name x500Name = x500NameBuilder.build();
            byte[] encoded = publicKey.getEncoded();
            SubjectPublicKeyInfo subjectPublicKeyInfo = new SubjectPublicKeyInfo(
                    ASN1Sequence.getInstance(encoded));
            PKCS10CertificationRequestBuilder pkcs10Builder = new PKCS10CertificationRequestBuilder(x500Name, subjectPublicKeyInfo);
            JcaContentSignerBuilder jcaContentSignerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            jcaContentSignerBuilder = jcaContentSignerBuilder.setProvider("BC");


            ContentSigner contentSigner = jcaContentSignerBuilder.build(privateKey);
            PKCS10CertificationRequest certificationRequest = pkcs10Builder.build(contentSigner);
            return certificationRequest.getEncoded();
        } catch (NoSuchAlgorithmException | OperatorCreationException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
