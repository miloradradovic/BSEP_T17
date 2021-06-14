package bsep.admin.api;

import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.model.LogConfig;
import bsep.admin.service.LogConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin(origins = "https://localhost:4200")
@RestController
@RequestMapping(value = "/log-config", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogConfigController {

    @Autowired
    private LogConfigService logConfigService;

    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @RequestMapping(value = "/send-log-config", method = RequestMethod.POST)
    public ResponseEntity<?> sendLogConfig(@RequestBody LogConfig logConfig) {
        if (logConfigService.sendLogConfig(logConfig)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
