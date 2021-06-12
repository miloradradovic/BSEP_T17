package bsep.hospital.api;

import bsep.hospital.model.LogConfig;
import bsep.hospital.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "https://localhost:4205")
@RestController
@RequestMapping(value = "/log-config", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogConfigController {

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/send-log-config", method = RequestMethod.POST)
    public ResponseEntity<?> sendLogConfig(@RequestBody LogConfig logConfig) {
        logService.saveNewConfig(logConfig);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
