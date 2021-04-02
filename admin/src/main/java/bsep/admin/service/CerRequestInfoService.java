package bsep.admin.service;

import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.keystore.KeyStoreReader;
import bsep.admin.model.CerRequestInfo;
import bsep.admin.repository.CerRequestInfoRepository;
import bsep.admin.utils.CryptingUtil;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class CerRequestInfoService implements ServiceInterface<CerRequestInfo> {

    @Autowired
    CerRequestInfoRepository cerRequestInfoRepository;

    @Autowired
    CryptingUtil cryptingUtil;

    @Autowired
    EmailService emailService;

    @Autowired
    KeyStoreReader keyStoreReader;


    @Override
    public List<CerRequestInfo> findAll() {
        return cerRequestInfoRepository.findAll();
    }

    @Override
    public Page<CerRequestInfo> findAll(Pageable pageable) {
        return cerRequestInfoRepository.findAll(pageable);
    }

    @Override
    public CerRequestInfo findOne(int id) {
        return cerRequestInfoRepository.findById(id).orElse(null);
    }

    @Override
    public CerRequestInfo saveOne(CerRequestInfo entity) {
        CerRequestInfo cerRequestInfo = cerRequestInfoRepository.findByUserId(entity.getUserId());

        return cerRequestInfo == null ? cerRequestInfoRepository.save(entity) : null;

    }

    @Override
    public boolean delete(int id) {
        CerRequestInfo cerRequestInfo = findOne(id);
        if (cerRequestInfo != null) {
            cerRequestInfoRepository.delete(cerRequestInfo);
            return true;
        }
        return false;

    }

    @Override
    public CerRequestInfo update(CerRequestInfo entity) {
        return null;
    }

    public boolean createCertificateRequest(byte[] encryptedCSR) throws IOException, CertificateNotFoundException {

        JcaPKCS10CertificationRequest p10Object = new JcaPKCS10CertificationRequest(encryptedCSR);
        X500Name x500Name = p10Object.getSubject();
        CerRequestInfo cerRequestInfo = generateAndSaveCertificateRequest(x500Name); // returns saved certificate request

        emailService.sendEmail(cerRequestInfo.getEmail(), generateCryptedData(cerRequestInfo.getEmail()));

        return true;
    }

    private String generateCryptedData(String email) throws CertificateNotFoundException {

        // Public key of super administrator is needed for encrypting.
        PublicKey publicKey = keyStoreReader.readCertificate("super.admin@admin.com").getPublicKey();
        // PrivateKey privateKey = keyStoreReader.readIssuerFromStore("super.admin@admin.com").getPrivateKey();
        byte[] encrypted = cryptingUtil.encrypt(email + "trimusketara", publicKey);
        return Hex.encodeHexString(encrypted);
        // return Arrays.toString(Hex.encode(encrypted));
        // return Base64.getEncoder().encodeToString(encrypted);
    }

    private CerRequestInfo generateAndSaveCertificateRequest(X500Name x500Name) {
        CerRequestInfo cerRequestInfo = new CerRequestInfo(getRDNValue(x500Name, BCStyle.CN),
                                                            getRDNValue(x500Name, BCStyle.SURNAME),
                                                            getRDNValue(x500Name, BCStyle.GIVENNAME),
                                                            getRDNValue(x500Name, BCStyle.O),
                                                            getRDNValue(x500Name, BCStyle.OU),
                                                            getRDNValue(x500Name, BCStyle.C),
                                                            Integer.parseInt(getRDNValue(x500Name, BCStyle.UID)),
                                                            getRDNValue(x500Name, BCStyle.E),
                                                            false);
        return saveOne(cerRequestInfo);
    }

    private String getRDNValue(X500Name x500Name, ASN1ObjectIdentifier bcStyle) {
        return x500Name.getRDNs(bcStyle)[0].getFirst().getValue().toString();
    }


    public boolean verifyCertificateRequest(String encrypted) {

        return false;
    }
}
