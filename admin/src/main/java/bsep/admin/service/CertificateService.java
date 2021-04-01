package bsep.admin.service;

import bsep.admin.dto.CertificateCreationDTO;
import bsep.admin.dto.ExtendedKeyUsageDTO;
import bsep.admin.dto.KeyUsageDTO;
import bsep.admin.dto.RevokeCertificateDTO;
import bsep.admin.enums.RevocationReason;
import bsep.admin.exceptions.AliasAlreadyExistsException;
import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.exceptions.InvalidIssuerException;
import bsep.admin.exceptions.IssuerNotCAException;
import bsep.admin.keystore.KeyStoreReader;
import bsep.admin.keystore.KeyStoreWriter;
import bsep.admin.model.CerRequestInfo;
import bsep.admin.model.Issuer;
import bsep.admin.model.SubjectData;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.*;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

@Service
public class CertificateService {

    @Autowired
    CerRequestInfoService cerRequestInfoService;

    @Autowired
    private KeyStoreWriter keyStoreWriter;

    @Autowired
    private KeyStoreReader keyStoreReader;

    @PostConstruct
    private void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public boolean createAdminCertificate(CertificateCreationDTO certificateCreationDTO, String issuerAlias) throws CertificateNotFoundException, OperatorCreationException, IssuerNotCAException, InvalidIssuerException, AliasAlreadyExistsException, CertificateException, CRLException, IOException {

        Certificate[] issuerCertificateChain = keyStoreReader.readCertificateChain(issuerAlias);

        X509Certificate issuer = (X509Certificate) issuerCertificateChain[0];
        if (!isCertificateValid(issuerCertificateChain))
            throw new InvalidIssuerException();

        try {
            if (issuer.getBasicConstraints() == -1 || !issuer.getKeyUsage()[5]) {
                // Sertifikat nije CA
                // https://stackoverflow.com/questions/12092457/how-to-check-if-x509certificate-is-ca-certificate
                throw new IssuerNotCAException();
            }
        } catch (NullPointerException ignored) {
        }

        String alias = cerRequestInfoService.findOne(certificateCreationDTO.getSubjectID()).getEmail();

        Certificate certInfo = keyStoreReader.readCertificate(alias);
        if (certInfo != null) {
            if (!isRevoked(certInfo))
                throw new AliasAlreadyExistsException();
        }

        return generateAdminCertificate(certificateCreationDTO, issuerCertificateChain, alias, issuerAlias);

    }

    private boolean generateAdminCertificate(CertificateCreationDTO certificateCreationDTO, Certificate[] issuerCertificateChain, String alias, String issuerAlias) throws CertificateNotFoundException, OperatorCreationException, CertificateException {
        KeyPair keyPair = generateKeyPair();
        SubjectData subjectData = generateSubjectData(certificateCreationDTO.getSubjectID());
        assert subjectData != null;
        assert keyPair != null;

        subjectData.setPublicKey(keyPair.getPublic());

        //email
        Issuer issuer = keyStoreReader.readIssuerFromStore(issuerAlias);


        X500Name issuerName = issuer.getX500name();
        PrivateKey privateKey = issuer.getPrivateKey();

        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        builder = builder.setProvider("BC");


        ContentSigner contentSigner = builder.build(privateKey);


        Date startDate = java.util.Date.from(subjectData.getStartDate().atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = java.util.Date.from(subjectData.getEndDate().atZone(ZoneId.systemDefault()).toInstant());


        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerName, new BigInteger(subjectData.getSerialNumber()),
                startDate, endDate, subjectData.getX500name(), keyPair.getPublic());

        KeyUsageDTO keyUsageDTO = certificateCreationDTO.getKeyUsageDTO();
        KeyUsage k = new KeyUsage(keyUsageDTO.getcRLSign() |
                keyUsageDTO.getDataEncipherment() |
                keyUsageDTO.getDecipherOnly() |
                keyUsageDTO.getDigitalSignature() |
                keyUsageDTO.getEncipherOnly() |
                keyUsageDTO.getKeyAgreement() |
                keyUsageDTO.getKeyCertSign() | // --> ima samo CA
                keyUsageDTO.getKeyEncipherment() |
                keyUsageDTO.getNonRepudiation());

        try {
            certGen.addExtension(Extension.keyUsage, false, k);
        } catch (CertIOException e) {
            e.printStackTrace();
        }

        ExtendedKeyUsageDTO extendedKeyUsageDTO = certificateCreationDTO.getExtendedKeyUsageDTO();
        ExtendedKeyUsage eku = new ExtendedKeyUsage(extendedKeyUsageDTO.makeKeyPurposeIdArray());

        try {
            certGen.addExtension(Extension.extendedKeyUsage, false, eku);
        } catch (CertIOException e) {
            e.printStackTrace();
        }

        X509CertificateHolder certHolder = certGen.build(contentSigner);

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");

        X509Certificate createdCertificate = certConverter.getCertificate(certHolder);


        Certificate[] newCertificateChain = ArrayUtils.insert(0, issuerCertificateChain, createdCertificate);
        keyStoreWriter.write(alias, keyPair.getPrivate(), newCertificateChain);
        keyStoreWriter.saveKeyStore();
        return true;
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);

            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SubjectData generateSubjectData(int subjectID) {
        CerRequestInfo cerRequestInfo = cerRequestInfoService.findOne(subjectID);

        if (cerRequestInfo == null)
            return null;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Datumi od kad do kad vazi sertifikat
        LocalDateTime startDate = LocalDateTime.now();
        startDate.format(dtf);

        LocalDateTime endDate = startDate.plusYears(1);
        endDate.format(dtf);

        Calendar curCal = new GregorianCalendar(TimeZone.getDefault());
        curCal.setTimeInMillis(System.currentTimeMillis());

        String serialNumber = curCal.getTimeInMillis() + "";

        // klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, cerRequestInfo.getCommonName());
        builder.addRDN(BCStyle.SURNAME, cerRequestInfo.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, cerRequestInfo.getGivenName());
        builder.addRDN(BCStyle.O, cerRequestInfo.getOrganization());
        builder.addRDN(BCStyle.OU, cerRequestInfo.getOrganizationUnit());
        builder.addRDN(BCStyle.C, cerRequestInfo.getCountry());

        // UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, String.valueOf(cerRequestInfo.getUserId()));

        // Kreiraju se podaci za sertifikat, sto ukljucuje:
        // - javni kljuc koji se vezuje za sertifikat
        // - podatke o vlasniku
        // - serijski broj sertifikata
        // - od kada do kada vazi sertifikat
        return new SubjectData(builder.build(), serialNumber, startDate, endDate);

    }

    public boolean isCertificateValid(Certificate[] chain) throws CertificateException, CRLException, IOException {

        if (chain == null) {
            return false;
        }

        X509Certificate cert;
        for (int i = 0; i < chain.length; i++) {
            cert = (X509Certificate) chain[i];

            if (isRevoked(cert)) {
                return false;
            }

            Date now = new Date();

            if (now.after(cert.getNotAfter()) || now.before(cert.getNotBefore())) {
                return false;
            }

            try {
                if (i == chain.length - 1) {
                    return isSelfSigned(cert);
                }
                X509Certificate issuer = (X509Certificate) chain[i + 1];
                cert.verify(issuer.getPublicKey());
            } catch (SignatureException | InvalidKeyException e) {
                return false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static boolean isSelfSigned(X509Certificate cert) {
        try {
            cert.verify(cert.getPublicKey());
            return true;
        } catch (SignatureException | InvalidKeyException e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRevoked(Certificate cer) throws IOException, CertificateException, CRLException {

        CertificateFactory factory = CertificateFactory.getInstance("X.509");

        File file = new File("src/main/resources/adminCRLs.crl");

        byte[] bytes = Files.readAllBytes(file.toPath());
        X509CRL crl = (X509CRL) factory.generateCRL(new ByteArrayInputStream(bytes));

        return crl.isRevoked(cer);
    }

    public void revokeCertificate(RevokeCertificateDTO revokeCertificateDTO, String issuerAlias) throws IOException, CertificateException, CRLException, CertificateNotFoundException, OperatorCreationException {

        File file = new File("src/main/resources/adminCRLs.crl");

        byte[] bytes = Files.readAllBytes(file.toPath());


        X509CRLHolder holder = new X509CRLHolder(bytes);
        X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(holder);

        crlBuilder.addCRLEntry(BigInteger.valueOf(Long.parseLong(revokeCertificateDTO.getSerialNumber()))/*The serial number of the revoked certificate*/, new Date() /*Revocation time*/, RevocationReason.valueOf(revokeCertificateDTO.getRevocationReason()).ordinal() /*Reason for cancellation*/);



        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SHA256WithRSA");
        contentSignerBuilder.setProvider("BC");


        Issuer issuer = keyStoreReader.readIssuerFromStore(issuerAlias);

        X509CRLHolder crlHolder = crlBuilder.build(contentSignerBuilder.build(issuer.getPrivateKey()));
        JcaX509CRLConverter converter = new JcaX509CRLConverter();
        converter.setProvider("BC");

        X509CRL crl = converter.getCRL(crlHolder);

        bytes = crl.getEncoded();


        OutputStream os = new FileOutputStream("src/main/resources/adminCRLs.crl");
        os.write(bytes);
        os.close();
    }
}
