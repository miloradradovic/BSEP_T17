package bsep.hospital.api;

import bsep.hospital.model.LogConfig;
import bsep.hospital.model.Report;
import bsep.hospital.model.ReportParams;
import bsep.hospital.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZoneId;

@CrossOrigin(origins = "https://localhost:4205")
@RestController
@RequestMapping(value = "/log-report", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportController {

    @Autowired
    private LogService logService;

    private static Logger logger = LogManager.getLogger(ReportController.class);

    // @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Report> getLogReport(@RequestBody ReportParams reportParams) {
        logger.info("Attempting to get report.");
        try {
            Report report = logService.getReport(reportParams.getFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    reportParams.getTo().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            logger.info("Successfully generated and retrieved the report.");
            return new ResponseEntity<>(report, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to generate and retrieve the report.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
