package bsep.admin.service;

import bsep.admin.dto.*;
import bsep.admin.enums.RevocationReason;
import bsep.admin.exceptions.*;
import bsep.admin.keystore.KeyStoreReader;
import bsep.admin.keystore.KeyStoreWriter;
import bsep.admin.model.CerRequestInfo;
import bsep.admin.model.Issuer;
import bsep.admin.model.SubjectData;
import org.apache.commons.lang3.ArrayUtils;
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
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
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

    @PostConstruct
    private void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public void createAdminCertificate(CertificateCreationDTO certificateCreationDTO, String issuerAlias) throws CertificateNotFoundException, OperatorCreationException, IssuerNotCAException, InvalidIssuerException, AliasAlreadyExistsException, CertificateException, CRLException, IOException, CertificateSendFailException {

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

        //String alias = cerRequestInfoService.findOne(certificateCreationDTO.getSubjectID()).getEmail();
        String alias = getLastAlias(cerRequestInfoService.findOne(certificateCreationDTO.getSubjectID()).getEmail());


        Certificate certInfo = keyStoreReader.readCertificate(alias);
        if (certInfo != null) {
            if (!isRevoked(certInfo))
                throw new AliasAlreadyExistsException();
        }

        String generatedAlias = generateAlias(cerRequestInfoService.findOne(certificateCreationDTO.getSubjectID()).getEmail());
        generateAdminCertificate(certificateCreationDTO, issuerCertificateChain, generatedAlias, issuerAlias);

    }

    private void generateAdminCertificate(CertificateCreationDTO certificateCreationDTO, Certificate[] issuerCertificateChain, String alias, String issuerAlias) throws CertificateNotFoundException, OperatorCreationException, CertificateException, CertificateSendFailException {
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
            e.printStackTrace();
        }

        ExtendedKeyUsageDTO extendedKeyUsageDTO = certificateCreationDTO.getExtendedKeyUsageDTO();
        ExtendedKeyUsage eku = new ExtendedKeyUsage(extendedKeyUsageDTO.makeKeyPurposeIdArray());

        try {
            certGen.addExtension(Extension.extendedKeyUsage, true, eku);


            GeneralNames subjectAltName = new GeneralNames(new GeneralName(GeneralName.dNSName, IETFUtils.valueToString(subjectData.getX500name().getRDNs(BCStyle.CN)[0].getFirst().getValue())));
            certGen.addExtension(X509Extensions.SubjectAlternativeName, false, subjectAltName);
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

        if (!sendCertificateToAdmin(createdCertificate)) {
            throw new CertificateSendFailException();
        }


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

    public boolean sendCertificateToAdmin(Certificate cer) throws CertificateEncodingException {
        byte[] bytes = cer.getEncoded();
        if (bytes != null) {
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<byte[]> request = new HttpEntity<>(bytes);
            ResponseEntity<?> responseEntity = restTemplate.exchange("http://localhost:8085/certificate", HttpMethod.POST, request, ResponseEntity.class);
            return responseEntity.getStatusCode() == HttpStatus.OK;
        }

        return false;

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

    public String getRevocationReason(Certificate cer) throws IOException, CertificateException, CRLException {

        CertificateFactory factory = CertificateFactory.getInstance("X.509");

        File file = new File("src/main/resources/adminCRLs.crl");

        byte[] bytes = Files.readAllBytes(file.toPath());
        X509CRL crl = (X509CRL) factory.generateCRL(new ByteArrayInputStream(bytes));

        X509CRLEntry c = crl.getRevokedCertificate((X509Certificate) cer);

        CRLReason reason = c.getRevocationReason();

        return reason != null ? c.getRevocationReason().toString() : "UNSPECIFIED";
    }

    public void revokeCertificate(RevokeCertificateDTO revokeCertificateDTO, String issuerAlias) throws IOException, CRLException, CertificateNotFoundException, OperatorCreationException, CertificateEncodingException {

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
    }

    public CertificateInfoDTO getCertificates() {

        List<Certificate> certificates = new ArrayList<>();

        Enumeration<String> aliases = keyStoreReader.getAllAliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            Certificate cer = keyStoreReader.readCertificate(alias);
            certificates.add(cer);
        }

        try {
            return makeTree(certificates);
        } catch (CertificateException | CRLException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CertificateInfoDTO makeTree(List<Certificate> certificates) throws CertificateException, CRLException, IOException {
        CertificateInfoDTO tree = findRoot(certificates);
        certificates.remove(Integer.parseInt(tree.getEmail().split("\\|")[1]));
        tree.setEmail(tree.getEmail().split("\\|")[0]);

        tree = findIntermediate(certificates, tree, tree.getEmail());
        tree = findEnd(certificates, tree);

        return tree;
    }

    public CertificateInfoDTO findRoot(List<Certificate> certificates) throws CertificateException, CRLException, IOException {
        CertificateInfoDTO tree = new CertificateInfoDTO();
        for (Certificate cer : certificates) {
            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) cer);
            X500Name i = certHolder.getIssuer();
            X500Name subject = certHolder.getSubject();

            if (IETFUtils.valueToString(i.getRDNs(BCStyle.E)[0].getFirst().getValue()).equals(IETFUtils.valueToString(subject.getRDNs(BCStyle.E)[0].getFirst().getValue()))) {
                tree.setEmail(IETFUtils.valueToString(subject.getRDNs(BCStyle.E)[0].getFirst().getValue()));
                tree.setCommonName(IETFUtils.valueToString(subject.getRDNs(BCStyle.CN)[0].getFirst().getValue()));
                tree.setFullName(IETFUtils.valueToString(subject.getRDNs(BCStyle.GIVENNAME)[0].getFirst().getValue()) + " " + IETFUtils.valueToString(subject.getRDNs(BCStyle.SURNAME)[0].getFirst().getValue()));
                tree.setEmail(tree.getEmail() + "|" + certificates.indexOf(cer));

                if (((X509Certificate) cer).getBasicConstraints() != -1 || ((X509Certificate) cer).getKeyUsage()[5])
                    tree.setCA(true);

                if (isRevoked(cer)) {
                    tree.setRevoked(true);
                    tree.setRevocationReason(getRevocationReason(cer));
                }
            }
        }

        return tree;

    }

    public CertificateInfoDTO findIntermediate(List<Certificate> certificates, CertificateInfoDTO parent, String issuerEmail) throws CertificateException, CRLException, IOException {

        for (Certificate cer : certificates) {
            JcaX509CertificateHolder certHolder = new JcaX509CertificateHolder((X509Certificate) cer);
            X500Name i = certHolder.getIssuer();
            X500Name subject = certHolder.getSubject();

            if (IETFUtils.valueToString(i.getRDNs(BCStyle.E)[0].getFirst().getValue()).equals(issuerEmail)) {
                CertificateInfoDTO tree = new CertificateInfoDTO();
                tree.setEmail(IETFUtils.valueToString(subject.getRDNs(BCStyle.E)[0].getFirst().getValue()));
                tree.setCommonName(IETFUtils.valueToString(subject.getRDNs(BCStyle.CN)[0].getFirst().getValue()));
                tree.setFullName(IETFUtils.valueToString(subject.getRDNs(BCStyle.GIVENNAME)[0].getFirst().getValue()) + " " + IETFUtils.valueToString(subject.getRDNs(BCStyle.SURNAME)[0].getFirst().getValue()));
                tree.setEmail(tree.getEmail());

                if (((X509Certificate) cer).getBasicConstraints() != -1 || ((X509Certificate) cer).getKeyUsage()[5])
                    tree.setCA(true);

                if (isRevoked(cer)) {
                    tree.setRevoked(true);
                    tree.setRevocationReason(getRevocationReason(cer));
                }

                parent.getChildren().add(tree);
            }
        }
        return parent;
    }

    public CertificateInfoDTO findEnd(List<Certificate> certificates, CertificateInfoDTO parent) throws CertificateException, CRLException, IOException {

        for (CertificateInfoDTO child : parent.getChildren()) {
            for (Certificate cer : certificates) {
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

                    if (isRevoked(cer)) {
                        tree.setRevoked(true);
                        tree.setRevocationReason(getRevocationReason(cer));
                    }

                    child.getChildren().add(tree);
                }
            }
        }

        return parent;
    }

    public boolean checkCertificate(String alias) throws CertificateException, CRLException, IOException {
        Certificate[] chain = keyStoreReader.readCertificateChain(alias);
        return isCertificateValid(chain);

    }
    /*
    public void generate() {

        try {
            createAdminCertificate(new CertificateCreationDTO(1, new KeyUsageDTO(), new ExtendedKeyUsageDTO()), "super.admin@admin.com");

            Certificate cer = keyStoreReader.readCertificate("fred.billy@hospital.com");
            PrivateKey pk = keyStoreReader.readPrivateKey("fred.billy@hospital.com");
            writeCertificateToPEM((X509Certificate) cer);
            writePrivateKeyToPEM(pk);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateNotFoundException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (IssuerNotCAException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (AliasAlreadyExistsException e) {
            e.printStackTrace();
        } catch (CRLException e) {
            e.printStackTrace();
        } catch (InvalidIssuerException e) {
            e.printStackTrace();
        }
    }



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
