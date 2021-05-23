package bsep.admin.initialization;


import bsep.admin.dto.ExtendedKeyUsageDTO;
import bsep.admin.dto.KeyUsageDTO;
import bsep.admin.keystore.KeyStoreReader;
import bsep.admin.keystore.KeyStoreWriter;
import bsep.admin.model.SubjectData;
import bsep.admin.service.CertificateService;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.*;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.logging.Logger;

@Component
public class Initialize {

    private static final Logger LOG = Logger.getLogger(Initialize.class.getName());

    @Autowired
    KeyStoreReader keyStoreReader;

    @Autowired
    KeyStoreWriter keyStoreWriter;

    @Autowired
    CertificateService certificateService;


    @PostConstruct
    public void init() {
        LOG.info("Executing operation Initialize");
        createCerAndCrlForRootCA();

    }

    public void createCerAndCrlForRootCA() {
        try {
            if (!keyStoreWriter.loadKeyStore())
                createCer();
        } catch (OperatorCreationException | CertificateException | CRLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void createCer() throws OperatorCreationException, CertificateException, CRLException, IOException {
        keyStoreWriter.createKeyStore();

        KeyPair keyPair = generateKeyPair();
        SubjectData subjectData = generateSubjectDataPredefined();

        assert keyPair != null;

        subjectData.setPublicKey(keyPair.getPublic());

        certificateService.writePrivateKeyToPEM(keyPair.getPrivate());


        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        builder = builder.setProvider("BC");


        ContentSigner contentSigner = builder.build(keyPair.getPrivate());


        Date startDate = java.util.Date.from(subjectData.getStartDate().atZone(ZoneId.systemDefault()).toInstant());
        Date endDate = java.util.Date.from(subjectData.getEndDate().atZone(ZoneId.systemDefault()).toInstant());

        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(subjectData.getX500name(), new BigInteger(subjectData.getSerialNumber()),
                startDate, endDate, subjectData.getX500name(), keyPair.getPublic());

        KeyUsageDTO keyUsageDTO = new KeyUsageDTO(true, true, true, true, true, true, true, true, true);
        KeyUsage k = new KeyUsage(keyUsageDTO.getcRLSign() |
                keyUsageDTO.getDataEncipherment() |
                keyUsageDTO.getDecipherOnly() |
                keyUsageDTO.getDigitalSignature() |
                keyUsageDTO.getEncipherOnly() |
                keyUsageDTO.getKeyAgreement() |
                keyUsageDTO.getKeyCertSign() |
                keyUsageDTO.getKeyEncipherment() |
                keyUsageDTO.getNonRepudiation());

        try {
            certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
            certGen.addExtension(Extension.keyUsage, false, k);
        } catch (CertIOException e) {
            e.printStackTrace();
        }

        ExtendedKeyUsageDTO extendedKeyUsageDTO = new ExtendedKeyUsageDTO(true, true, true, true, true, true);
        ExtendedKeyUsage eku = new ExtendedKeyUsage(extendedKeyUsageDTO.makeKeyPurposeIdArray());

        try {
            certGen.addExtension(Extension.extendedKeyUsage, false, eku);

            GeneralNames subjectAltName = new GeneralNames(new GeneralName(GeneralName.dNSName, "localhost"));

            certGen.addExtension(X509Extensions.SubjectAlternativeName, false, subjectAltName);
        } catch (CertIOException e) {
            e.printStackTrace();
        }

        X509CertificateHolder certHolder = certGen.build(contentSigner);

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");

        X509Certificate createdCertificate = certConverter.getCertificate(certHolder);

        keyStoreWriter.writeRootCA("super.admin@localhost.com", keyPair.getPrivate(), createdCertificate);
        keyStoreWriter.saveKeyStore();

        createCRL(keyPair.getPrivate(), subjectData.getX500name());
        //writeCertificateToPEM(createdCertificate);
        //writePrivateKeyToPEM(keyPair.getPrivate());
    }

    private void createCRL(PrivateKey pk, X500Name issuerName) throws CRLException, IOException, OperatorCreationException {
        X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(issuerName, new Date());
        crlBuilder.setNextUpdate(new Date(System.currentTimeMillis() + 86400 * 1000));

        JcaContentSignerBuilder contentSignerBuilder = new JcaContentSignerBuilder("SHA256WithRSA");
        contentSignerBuilder.setProvider("BC");

        //issuer pk
        X509CRLHolder crlHolder = crlBuilder.build(contentSignerBuilder.build(pk));
        JcaX509CRLConverter converter = new JcaX509CRLConverter();
        converter.setProvider("BC");

        X509CRL crl = converter.getCRL(crlHolder);

        byte[] bytes = crl.getEncoded();


        OutputStream os = new FileOutputStream("src/main/resources/adminCRLs.crl");
        os.write(bytes);
        os.close();
    }

    private SubjectData generateSubjectDataPredefined() {

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
        builder.addRDN(BCStyle.CN, "localhost.com");
        builder.addRDN(BCStyle.SURNAME, "Martin");
        builder.addRDN(BCStyle.GIVENNAME, "Neil");
        builder.addRDN(BCStyle.O, "FTN");
        builder.addRDN(BCStyle.OU, "SIIT");
        builder.addRDN(BCStyle.C, "RS");
        builder.addRDN(BCStyle.E, "super.admin@localhost.com");

        // UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, String.valueOf(1));

        // Kreiraju se podaci za sertifikat, sto ukljucuje:
        // - javni kljuc koji se vezuje za sertifikat
        // - podatke o vlasniku
        // - serijski broj sertifikata
        // - od kada do kada vazi sertifikat
        return new SubjectData(builder.build(), serialNumber, startDate, endDate);

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

    /*
    public void writeCertificateToPEM(X509Certificate certificate) throws IOException {
        JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(new File("admin.crt")));
        pemWriter.writeObject(certificate);
        pemWriter.flush();
        pemWriter.close();
    }

    public void writePrivateKeyToPEM(PrivateKey privateKey) throws IOException {
        PemObject pemFile = new PemObject("PRIVATE KEY", privateKey.getEncoded());

        JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(new File("admin.key")));
        pemWriter.writeObject(pemFile);
        pemWriter.flush();
        pemWriter.close();
    }
   */
}
