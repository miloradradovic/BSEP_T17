package bsep.admin.api;

import bsep.admin.dto.CerRequestInfoDTO;
import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.mappers.CerRequestInfoMapper;
import bsep.admin.service.CerRequestInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/certificate-request", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateRequestController {

    @Autowired
    CerRequestInfoService cerRequestInfoService;

    private final CerRequestInfoMapper cerRequestInfoMapper;

    public CertificateRequestController() {
        cerRequestInfoMapper = new CerRequestInfoMapper();
    }

    @RequestMapping(value = "/send-certificate-request", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendCertificateRequest(@RequestBody byte[] encryptedCSR) {

        try {
            boolean success = cerRequestInfoService.createCertificateRequest(encryptedCSR);

            if (success)
                return new ResponseEntity<>(HttpStatus.OK);

        } catch (IOException | CertificateNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CerRequestInfoDTO>> getCertificateRequests() {

        List<CerRequestInfoDTO> reqs = cerRequestInfoMapper.toDTOList(cerRequestInfoService.findAllVerified());
        return new ResponseEntity<>(reqs, HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeCertificateRequest(@PathVariable Integer id) {

        if (cerRequestInfoService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
