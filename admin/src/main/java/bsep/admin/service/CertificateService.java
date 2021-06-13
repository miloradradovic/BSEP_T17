package bsep.admin.service;

import bsep.admin.dto.*;
import bsep.admin.enums.RevocationReason;
import bsep.admin.exceptions.*;
import bsep.admin.keystore.KeyStoreReader;
import bsep.admin.keystore.KeyStoreWriter;
import bsep.admin.model.CerRequestInfo;
import bsep.admin.model.Issuer;
import bsep.admin.model.SubjectData;
import bsep.admin.utils.Pair;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.*;
import org.bouncycastle.cert.jcajce.JcaX509CRLConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.io.pem.PemObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.CRLReason;
import java.security.cert.Certificate;
import java.security.cert.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CertificateService {

    @Autowired
    CerRequestInfoService cerRequestInfoService;

    @Autowired
    private KeyStoreWriter keyStoreWriter;

    @Autowired
    private KeyStoreReader keyStoreReader;

    @Autowired
    private EmailService emailService;

    private static Logger logger = LogManager.getLogger(CertificateService.class);

    @PostConstruct
    private void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public void createAdminCertificate(CertificateCreationDTO certificateCreationDTO, String issuerAlias) throws CertificateNotFoundException, OperatorCreationException, IssuerNotCAException, InvalidIssuerException, AliasAlreadyExistsException, CertificateException, CRLException, IOException, CertificateSendFailException, MessagingException {

        logger.info("Attempting to create admin certificate, issuer alias " + issuerAlias);
        Certificate[] issuerCertificateChain = keyStoreReader.readCertificateChain(issuerAlias);
        Certificate cc = keyStoreReader.readCertificate(issuerAlias);

        X509Certificate issuer = (X509Certificate) issuerCertificateChain[0];
        if (!isCertificateValid(issuerCertificateChain)) {
            logger.error("Issuer alias is invalid " + issuerAlias);
            throw new InvalidIssuerException();
        }
        try {
            if (issuer.getBasicConstraints() == -1 || !issuer.getKeyUsage()[5]) {
                // Sertifikat nije CA
                // https://stackoverflow.com/questions/12092457/how-to-check-if-x509certificate-is-ca-certificate
                logger.error("Issuer is not CA.");
                throw new IssuerNotCAException();
            }
        } catch (NullPointerException ignored) {
        }

        //String alias = cerRequestInfoService.findOne(certificateCreationDTO.getSubjectID()).getEmail();
        String alias = getLastAlias(cerRequestInfoService.findOne(certificateCreationDTO.getSubjectID()).getEmail());


        Certificate certInfo = keyStoreReader.readCertificate(alias);
        if (certInfo != null) {
            if (!isRevoked(certInfo)) {
                logger.error("Alias " + alias + " already exists.");
                throw new AliasAlreadyExistsException();
            }
        }

        String generatedAlias = generateAlias(cerRequestInfoService.findOne(certificateCreationDTO.getSubjectID()).getEmail());
        generateAdminCertificate(certificateCreationDTO, issuerCertificateChain, generatedAlias, issuerAlias);

    }

    private void generateAdminCertificate(CertificateCreationDTO certificateCreationDTO, Certificate[] issuerCertificateChain, String alias, String issuerAlias) throws CertificateNotFoundException, OperatorCreationException, CertificateException, CertificateSendFailException, MessagingException, IOException {
        logger.info("Attempting to generate admin certificate.");
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
            certGen.addExtension(Extension.keyUsage, true, k);
            if (keyUsageDTO.isKeyCertSign())
                certGen.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
        } catch (CertIOException e) {
            logger.error("Failed to add extensions to certificate.");
            e.printStackTrace();
        }

        ExtendedKeyUsageDTO extendedKeyUsageDTO = certificateCreationDTO.getExtendedKeyUsageDTO();
        ExtendedKeyUsage eku = new ExtendedKeyUsage(extendedKeyUsageDTO.makeKeyPurposeIdArray());

        try {
            certGen.addExtension(Extension.extendedKeyUsage, true, eku);


            GeneralNames subjectAltName = new GeneralNames(new GeneralName(GeneralName.dNSName, IETFUtils.valueToString(subjectData.getX500name().getRDNs(BCStyle.CN)[0].getFirst().getValue())));
            certGen.addExtension(X509Extensions.SubjectAlternativeName, false, subjectAltName);
        } catch (CertIOException e) {
            logger.error("Failed to add extensions to certificate.");
            e.printStackTrace();
        }


        X509CertificateHolder certHolder = certGen.build(contentSigner);

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");

        X509Certificate createdCertificate = certConverter.getCertificate(certHolder);


        Certificate[] newCertificateChain = ArrayUtils.insert(0, issuerCertificateChain, createdCertificate);
        keyStoreWriter.write(alias, keyPair.getPrivate(), newCertificateChain);
        keyStoreWriter.saveKeyStore();

        String email = IETFUtils.valueToString(subjectData.getX500name().getRDNs(BCStyle.E)[0].getFirst().getValue());
        String fullName = IETFUtils.valueToString(subjectData.getX500name().getRDNs(BCStyle.SURNAME)[0].getFirst().getValue()) + " " + IETFUtils.valueToString(subjectData.getX500name().getRDNs(BCStyle.GIVENNAME)[0].getFirst().getValue());
        logger.info("Created certificate. Sending it via email.");
        if (!sendCertificateToAdmin(email, fullName, createdCertificate)) {
            logger.error("Failed to send certificate via email.");
            throw new CertificateSendFailException();
        }


    }

    private String generateAlias(String email) {

        logger.info("Attempting to generate alias for email " + email);
        int lastAliasNumber = -1;
        Enumeration<String> aliases = keyStoreReader.getAllAliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (alias.contains(email) && alias.contains("|")) {
                int number = Integer.parseInt(alias.split("\\|")[1]);
                lastAliasNumber = Math.max(number, lastAliasNumber);
            }

        }

        lastAliasNumber += 1;
        email += "|" + lastAliasNumber;

        logger.info("Successfully generated alias for email " + email);
        return email;
    }

    private String getLastAlias(String email) {

        logger.info("Getting the last alias for email " + email);
        int lastAliasNumber = -1;
        Enumeration<String> aliases = keyStoreReader.getAllAliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            if (alias.contains(email) && alias.contains("|")) {
                int number = Integer.parseInt(alias.split("\\|")[1]);
                lastAliasNumber = Math.max(number, lastAliasNumber);
            }

        }

        email += "|" + lastAliasNumber;

        logger.info("Successfully retrieved the last alias for email.");
        return email;
    }

    public boolean sendCertificateToAdmin(String email, String fullName, X509Certificate cer) throws MessagingException, IOException {
        logger.info("Attempting to send certificate via email.");
        writeCertificateToPEM(cer);

        File file = new File("src/main/resources/sending.crt");
        Path path = file.toPath();
        byte[] bytes = Files.readAllBytes(path);
        emailService.sendCertificate(email, fullName, bytes);
        return true;
    }

    private KeyPair generateKeyPair() {
        try {
            logger.info("Attempting to generate keypair.");
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            logger.info("Successfully generated keypair.");
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            logger.error("Failed to generate keypair.");
            e.printStackTrace();
        }
        return null;
    }

    private SubjectData generateSubjectData(int subjectID) {
        logger.info("Attempting to generate subject data for subject id " + subjectID);
        CerRequestInfo cerRequestInfo = cerRequestInfoService.findOne(subjectID);

        if (cerRequestInfo == null) {
            logger.error("Certificate request with id " + subjectID + " is not found.");
            return null;
        }
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
        builder.addRDN(BCStyle.CN, cerRequestInfo.getCommonName()); //domain za koji se izdaje i koji treba da protect-uje - Fully Qualified Domain Name
        builder.addRDN(BCStyle.SURNAME, cerRequestInfo.getSurname());
        builder.addRDN(BCStyle.GIVENNAME, cerRequestInfo.getGivenName());
        builder.addRDN(BCStyle.O, cerRequestInfo.getOrganization());
        builder.addRDN(BCStyle.OU, cerRequestInfo.getOrganizationUnit());
        builder.addRDN(BCStyle.C, cerRequestInfo.getCountry());
        builder.addRDN(BCStyle.E, cerRequestInfo.getEmail());

        // UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, String.valueOf(cerRequestInfo.getUserId()));

        // Kreiraju se podaci za sertifikat, sto ukljucuje:
        // - javni kljuc koji se vezuje za sertifikat
        // - podatke o vlasniku
        // - serijski broj sertifikata
        // - od kada do kada vazi sertifikat
        logger.info("Successfully generated subject data for subject id " + subjectID);
        return new SubjectData(builder.build(), serialNumber, startDate, endDate);

    }

    public boolean isCertificateValid(Certificate[] chain) throws CertificateException, CRLException, IOException {

        logger.info("Attempting to check if certificate is valid.");
        if (chain == null) {
            logger.error("Chain is null.");
            return false;
        }

        X509Certificate cert;
        for (int i = 0; i < chain.length; i++) {
            cert = (X509Certificate) chain[i];

            if (isRevoked(cert)) {
                logger.error("Certificate is revoked.");
                return false;
            }

            Date now = new Date();

            if (now.after(cert.getNotAfter()) || now.before(cert.getNotBefore())) {
                logger.error("Date of certificate is invalid.");
                return false;
            }

            try {
                if (i == chain.length - 1) {
                    logger.info("Successfully checked certificate validity.");
                    return isSelfSigned(cert);
                }
                X509Certificate issuer = (X509Certificate) chain[i + 1];
                cert.verify(issuer.getPublicKey());
            } catch (SignatureException | InvalidKeyException e) {
                logger.error("Certificate signature is invalid.");
                return false;
            } catch (Exception e) {
                logger.error("Something went wrong.");
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    public static boolean isSelfSigned(X509Certificate cert) {
        try {
            logger.info("Checking if certificate is self signed.");
            cert.verify(cert.getPublicKey());
            logger.info("Successfully checked if certificate is self signed.");
            return true;
        } catch (SignatureException | InvalidKeyException e) {
            logger.error("Failed to check if certificate is self signed because something is wrong with signature.");
            return false;
        } catch (Exception e) {
            logger.error("Something went wrong.");
            throw new RuntimeException(e);
        }
    }

    public boolean isRevoked(Certificate cer) throws IOException, CertificateException, CRLException {

        logger.info("Attempting to check if certificate is revoked.");
        CertificateFactory factory = CertificateFactory.getInstance("X.509");

        File file = new File("src/main/resources/adminCRLs.crl");

        byte[] bytes = Files.readAllBytes(file.toPath());
        X509CRL crl = (X509CRL) factory.generateCRL(new ByteArrayInputStream(bytes));
        logger.info("Successfully checked if certificate is revoked.");
        return crl.isRevoked(cer);
    }

    public String getRevocationReason(Certificate cer) throws IOException, CertificateException, CRLException {

        logger.info("Attempting to get revocation reason for certificate.");
        CertificateFactory factory = CertificateFactory.getInstance("X.509");

        File file = new File("src/main/resources/adminCRLs.crl");

        byte[] bytes = Files.readAllBytes(file.toPath());
        X509CRL crl = (X509CRL) factory.generateCRL(new ByteArrayInputStream(bytes));

        X509CRLEntry c = crl.getRevokedCertificate((X509Certificate) cer);

        CRLReason reason = c.getRevocationReason();

        logger.info("Successfully retrieved revocation reason for certificate.");
        return reason != null ? c.getRevocationReason().toString() : "UNSPECIFIED";
    }

    public void revokeCertificate(RevokeCertificateDTO revokeCertificateDTO, String issuerAlias) throws IOException, CRLException, CertificateNotFoundException, OperatorCreationException, CertificateEncodingException {

        logger.info("Attempting to revoke certificate.");
        File file = new File("src/main/resources/adminCRLs.crl");

        byte[] bytes = Files.readAllBytes(file.toPath());


        X509CRLHolder holder = new X509CRLHolder(bytes);
        X509v2CRLBuilder crlBuilder = new X509v2CRLBuilder(holder);

        Certificate cer = keyStoreReader.readCertificate(revokeCertificateDTO.getSubjectAlias());
        JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) cer);

        crlBuilder.addCRLEntry(certHolder.getSerialNumber()/*The serial number of the revoked certificate*/, new Date() /*Revocation time*/, RevocationReason.valueOf(revokeCertificateDTO.getRevocationReason()).ordinal() /*Reason for cancellation*/);


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
        logger.info("Successfully revoked certificate.");
    }

    public CertificateInfoDTO getCertificates() {

        logger.info("Attempting to get all certificates from keystore.");
        List<Pair<String, Certificate>> certificates = new ArrayList<>();

        Enumeration<String> aliases = keyStoreReader.getAllAliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate cer = keyStoreReader.readCertificate(alias);
            certificates.add(new Pair<>(alias, cer));
        }

        try {
            return makeTree(certificates);
        } catch (CertificateException | CRLException | IOException e) {
            logger.error("Failed to get all certificates from keystore.");
            e.printStackTrace();
        }
        return null;
    }

    public CertificateInfoDTO makeTree(List<Pair<String, Certificate>> certificates) throws CertificateException, CRLException, IOException {
        logger.info("Attempting to generate tree-like structure of certificates.");
        CertificateInfoDTO tree = findRoot(certificates);
        certificates.remove(Integer.parseInt(tree.getEmail().split("\\|")[1]));
        tree.setEmail(tree.getEmail().split("\\|")[0]);

        tree = findIntermediate(certificates, tree, tree.getEmail());
        tree = findEnd(certificates, tree);
        logger.info("Successfully generated tree-like structure of certificates");
        return tree;
    }

    public CertificateInfoDTO findRoot(List<Pair<String, Certificate>> certificates) throws CertificateException, CRLException, IOException {
        logger.info("Attempting to find the root certificate.");
        CertificateInfoDTO tree = new CertificateInfoDTO();
        for (Pair pair : certificates) {
            Certificate cer = (Certificate) pair.getR();
            String alias = (String) pair.getL();

            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) cer);
            X500Name i = certHolder.getIssuer();
            X500Name subject = certHolder.getSubject();

            if (IETFUtils.valueToString(i.getRDNs(BCStyle.E)[0].getFirst().getValue()).equals(IETFUtils.valueToString(subject.getRDNs(BCStyle.E)[0].getFirst().getValue()))) {
                tree.setEmail(IETFUtils.valueToString(subject.getRDNs(BCStyle.E)[0].getFirst().getValue()));
                tree.setCommonName(IETFUtils.valueToString(subject.getRDNs(BCStyle.CN)[0].getFirst().getValue()));
                tree.setFullName(IETFUtils.valueToString(subject.getRDNs(BCStyle.GIVENNAME)[0].getFirst().getValue()) + " " + IETFUtils.valueToString(subject.getRDNs(BCStyle.SURNAME)[0].getFirst().getValue()));
                tree.setEmail(tree.getEmail() + "|" + certificates.indexOf(pair));
                tree.setAlias(alias);

                if (((X509Certificate) cer).getBasicConstraints() != -1 || ((X509Certificate) cer).getKeyUsage()[5])
                    tree.setCA(true);

                if (isRevoked(cer)) {
                    tree.setRevoked(true);
                    tree.setRevocationReason(getRevocationReason(cer));
                }
            }
        }
        logger.info("Successfully retrieved root certificate.");
        return tree;

    }

    public CertificateInfoDTO findIntermediate(List<Pair<String, Certificate>> certificates, CertificateInfoDTO parent, String issuerEmail) throws CertificateException, CRLException, IOException {

        logger.info("Attempting to find intermediate certificates.");
        for (Pair pair : certificates) {
            Certificate cer = (Certificate) pair.getR();
            String alias = (String) pair.getL();

            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) cer);
            X500Name i = certHolder.getIssuer();
            X500Name subject = certHolder.getSubject();

            if (IETFUtils.valueToString(i.getRDNs(BCStyle.E)[0].getFirst().getValue()).equals(issuerEmail)) {
                CertificateInfoDTO tree = new CertificateInfoDTO();
                tree.setEmail(IETFUtils.valueToString(subject.getRDNs(BCStyle.E)[0].getFirst().getValue()));
                tree.setCommonName(IETFUtils.valueToString(subject.getRDNs(BCStyle.CN)[0].getFirst().getValue()));
                tree.setFullName(IETFUtils.valueToString(subject.getRDNs(BCStyle.GIVENNAME)[0].getFirst().getValue()) + " " + IETFUtils.valueToString(subject.getRDNs(BCStyle.SURNAME)[0].getFirst().getValue()));
                tree.setEmail(tree.getEmail());
                tree.setAlias(alias);

                if (((X509Certificate) cer).getBasicConstraints() != -1 || ((X509Certificate) cer).getKeyUsage()[5])
                    tree.setCA(true);

                if (isRevoked(cer)) {
                    tree.setRevoked(true);
                    tree.setRevocationReason(getRevocationReason(cer));
                }

                parent.getChildren().add(tree);
            }
        }
        logger.info("Successfully retrieved intermediate certificates.");
        return parent;
    }

    public CertificateInfoDTO findEnd(List<Pair<String, Certificate>> certificates, CertificateInfoDTO parent) throws CertificateException, CRLException, IOException {

        logger.info("Attempting to retrieve the end certificate.");
        for (CertificateInfoDTO child : parent.getChildren()) {
            for (Pair pair : certificates) {
                Certificate cer = (Certificate) pair.getR();
                String alias = (String) pair.getL();

                JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) cer);
                X500Name i = certHolder.getIssuer();
                X500Name subject = certHolder.getSubject();

                if (IETFUtils.valueToString(i.getRDNs(BCStyle.E)[0].getFirst().getValue()).equals(child.getEmail())) {
                    CertificateInfoDTO tree = new CertificateInfoDTO();
                    tree.setEmail(IETFUtils.valueToString(subject.getRDNs(BCStyle.E)[0].getFirst().getValue()));
                    tree.setCommonName(IETFUtils.valueToString(subject.getRDNs(BCStyle.CN)[0].getFirst().getValue()));
                    tree.setFullName(IETFUtils.valueToString(subject.getRDNs(BCStyle.GIVENNAME)[0].getFirst().getValue()) + " " + IETFUtils.valueToString(subject.getRDNs(BCStyle.SURNAME)[0].getFirst().getValue()));
                    tree.setEmail(tree.getEmail());
                    tree.setCA(false);
                    tree.setAlias(alias);

                    if (isRevoked(cer)) {
                        tree.setRevoked(true);
                        tree.setRevocationReason(getRevocationReason(cer));
                    }

                    child.getChildren().add(tree);
                }
            }
        }
        logger.info("Successfully retrieved end certificate.");
        return parent;
    }

    public boolean checkCertificate(String alias) throws CertificateException, CRLException, IOException {
        logger.info("Attempting to check certificate for alias " + alias);
        Certificate[] chain = keyStoreReader.readCertificateChain(alias);
        logger.info("Successfully checked certificate for alias " + alias);
        return isCertificateValid(chain);

    }

    public void writeCertificateToPEM(X509Certificate certificate) throws IOException {
        logger.info("Attempting to write certificate to pem.");
        JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(new File("src/main/resources/sending.crt")));
        pemWriter.writeObject(certificate);
        pemWriter.flush();
        pemWriter.close();
        logger.info("Successfully wrote certificate to pem.");
    }


    public void writePrivateKeyToPEM(PrivateKey privateKey) throws IOException {
        logger.info("Attempting to write private key to pem.");
        PemObject pemFile = new PemObject("PRIVATE KEY", privateKey.getEncoded());

        JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(new File("localhost.key")));
        pemWriter.writeObject(pemFile);
        pemWriter.flush();
        pemWriter.close();
        logger.info("Successfully wrote private key to pem.");
    }

    public boolean checkCertificateByEmail(String email) throws CertificateException, CRLException, IOException {
        String alias = getLastAlias(email);
        if (alias != null) {
            logger.info("Attempting to check certificate for alias " + alias);
            Certificate[] chain = keyStoreReader.readCertificateChain(alias);
            logger.info("Successfully checked certificate for alias " + alias);
            return isCertificateValid(chain);
        }

        return false;
    }


}
