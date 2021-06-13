package bsep.admin.api;

import bsep.admin.dto.CerRequestInfoDTO;
import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.mappers.CerRequestInfoMapper;
import bsep.admin.model.CerRevocationRequest;
import bsep.admin.service.CerRequestInfoService;
import bsep.admin.service.CerRevocationRequestService;
import bsep.admin.service.CertificateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "https://localhost:4200")
@RestController
@RequestMapping(value = "/certificate-request", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateRequestController {

    @Autowired
    CerRequestInfoService cerRequestInfoService;

    @Autowired
    CerRevocationRequestService cerRevocationRequestService;

    @Autowired
    CertificateService certificateService;

    private final CerRequestInfoMapper cerRequestInfoMapper;

    private static Logger logger = LogManager.getLogger(CertificateController.class);

    public CertificateRequestController() {
        cerRequestInfoMapper = new CerRequestInfoMapper();
    }

    @RequestMapping(value = "/send-certificate-request", method = RequestMethod.POST)
    public ResponseEntity<?> sendCertificateRequest(@RequestBody byte[] encryptedCSR) {
        try {
            logger.info("Attempting to create certificate request.");
            boolean success = cerRequestInfoService.createCertificateRequest(encryptedCSR);

            if (success) {
                logger.info("Successfully created certificate request.");
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                logger.error("Failed to create certificate request.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (IOException | CertificateNotFoundException e) {
            logger.error("Failed to create certificate request.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CerRequestInfoDTO>> getCertificateRequests() {

        logger.info("Attempting to get certificate requests.");
        List<CerRequestInfoDTO> reqs = cerRequestInfoMapper.toDTOList(cerRequestInfoService.findAllVerified());
        logger.info("Successfully retrieved all certificate requests.");
        return new ResponseEntity<>(reqs, HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeCertificateRequest(@PathVariable @Positive Integer id) {

        logger.info("Attempting to remove certificate request with id " + id.toString());
        if (cerRequestInfoService.delete(id)) {
            logger.info("Successfully removed certificate request with id " + id.toString());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.error("Failed to remove certificate request with id " + id.toString());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/send-certificate-revocation-request/{email}", method = RequestMethod.POST)
    public ResponseEntity<?> saveCertificateRevocationRequest(@PathVariable String email) {
        try {
            logger.info("Attempting to verify certificate by email.");
            boolean success = certificateService.checkCertificateByEmail(email);

            if (success) {
                logger.info("Successfully verify certificate by email.");
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                logger.error("Failed to verify certificate by email.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            logger.error("Failed to verify certificate by email.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/check-certificate-valid/{email}", method = RequestMethod.GET)
    public ResponseEntity<?> checkCertificateValid(@PathVariable String email) {
        try {
            logger.info("Attempting to create certificate revocation request.");
            CerRevocationRequest success = cerRevocationRequestService.saveOne(new CerRevocationRequest("Admin with email: " + email + " want's to revoke certificate."));

            if (success != null) {
                logger.info("Successfully created certificate revocation request.");
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                logger.error("Failed to create certificate revocation request.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            logger.error("Failed to create certificate revocation request.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
