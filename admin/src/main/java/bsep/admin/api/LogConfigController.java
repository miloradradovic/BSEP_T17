package bsep.admin.api;

import bsep.admin.exceptions.CertificateNotFoundException;
import bsep.admin.model.LogConfig;
import bsep.admin.service.LogConfigService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@CrossOrigin(origins = "https://localhost:4200")
@RestController
@RequestMapping(value = "/log-config", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogConfigController {

    @Autowired
    private LogConfigService logConfigService;

    private static Logger logger = LogManager.getLogger(LogConfigController.class);

    // @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    @RequestMapping(value = "/send-log-config", method = RequestMethod.POST)
    public ResponseEntity<?> sendLogConfig(@RequestBody @Valid LogConfig logConfig) {
        logger.info("Received log config, attempting to save it.");
        if (logConfigService.sendLogConfig(logConfig)) {
            logger.info("Successfully saved log config.");
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            logger.error("Failed to save log config.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}
