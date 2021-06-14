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

@CrossOrigin(origins = "https://localhost:4205")
@RestController
@RequestMapping(value = "/log-report", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportController {

    @Autowired
    private LogService logService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Report> getLogReport(@RequestBody ReportParams reportParams) {
        Report report = logService.getReport(reportParams.getFrom(), reportParams.getTo());
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
