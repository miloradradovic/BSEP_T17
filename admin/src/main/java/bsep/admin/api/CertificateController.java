package bsep.admin.api;

import bsep.admin.dto.CertificateCreationDTO;
import bsep.admin.dto.CertificateInfoDTO;
import bsep.admin.dto.RevokeCertificateDTO;
import bsep.admin.exceptions.AliasAlreadyExistsException;
import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.exceptions.InvalidIssuerException;
import bsep.admin.exceptions.IssuerNotCAException;
import bsep.admin.model.Admin;
import bsep.admin.service.CerRequestInfoService;
import bsep.admin.service.CertificateService;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;

@RestController
@RequestMapping(value = "/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @Autowired
    CerRequestInfoService cerRequestInfoService;

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCertificate(@Valid @RequestBody CertificateCreationDTO certificateCreationDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin loggedIn = (Admin) authentication.getPrincipal();

        try {
            certificateService.createAdminCertificate(certificateCreationDTO, loggedIn.getEmail());
            cerRequestInfoService.delete(certificateCreationDTO.getSubjectID());
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (OperatorCreationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IssuerNotCAException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (InvalidIssuerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (AliasAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (CertificateException | CRLException | IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateInfoDTO> getAllCertificates() {

        CertificateInfoDTO certificateInfoDTO = certificateService.getCertificates();
        return new ResponseEntity<>(certificateInfoDTO, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> revokeCertificate(@Valid @RequestBody RevokeCertificateDTO revokeCertificateDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Admin loggedIn = (Admin) authentication.getPrincipal();

        try {
            certificateService.revokeCertificate(revokeCertificateDTO, loggedIn.getEmail());
        } catch (IOException | CRLException | CertificateException | OperatorCreationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (CertificateNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

}
