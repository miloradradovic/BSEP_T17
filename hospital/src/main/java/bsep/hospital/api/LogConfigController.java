package bsep.hospital.api;

import bsep.hospital.model.LogConfig;
import bsep.hospital.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@CrossOrigin(origins = "https://localhost:4205")
@RestController
@RequestMapping(value = "/log-config", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogConfigController {

    @Autowired
    private LogService logService;

    private static Logger logger = LogManager.getLogger(LogConfigController.class);

    @RequestMapping(value = "/send-log-config", method = RequestMethod.POST)
    public ResponseEntity<?> sendLogConfig(@RequestBody LogConfig logConfig) {
        logger.info("Attempting to create new log config.");
        try{
            logService.createNewConfig(logConfig);
            logger.info("Successfully created new log config.");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to create new log config.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
