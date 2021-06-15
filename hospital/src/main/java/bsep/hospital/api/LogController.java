package bsep.hospital.api;

import bsep.hospital.dto.FilterParamsDTO;
import bsep.hospital.dto.LogDTO;
import bsep.hospital.logging.LogModel;
import bsep.hospital.logging.LogSource;
import bsep.hospital.logging.LogType;
import bsep.hospital.model.FilterParams;
import bsep.hospital.model.LogConfig;
import bsep.hospital.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "https://localhost:4205")
@RestController
@RequestMapping(value = "/logs", produces = MediaType.APPLICATION_JSON_VALUE)
public class LogController {

    @Autowired
    LogService logService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // pretrage od do tip regex

    @RequestMapping(value = "/get-logs", method = RequestMethod.GET)
    public ResponseEntity<?> getLogs() {
        List<LogModel> logs = logService.findAll();
        List<LogDTO> logDTOS = new ArrayList<LogDTO>();
        for(LogModel log: logs){
            logDTOS.add(new LogDTO(log.getLevel(), log.getMessage(), log.getLogTime().format(formatter), log.getLogSource(), log.getIp(), log.isAlarm()));
        }
        return new ResponseEntity<>(logDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/get-logs/alarm", method = RequestMethod.GET)
    public ResponseEntity<?> getAlarmedLogs() {
        List<LogModel> logs = logService.findAllByAlarm();
        List<LogDTO> logDTOS = new ArrayList<LogDTO>();
        for(LogModel log: logs){
            logDTOS.add(new LogDTO(log.getLevel(), log.getMessage(), log.getLogTime().format(formatter), log.getLogSource(), log.getIp(), log.isAlarm()));
        }
        return new ResponseEntity<>(logDTOS, HttpStatus.OK);
    }

    @RequestMapping(value = "/filter-logs", method = RequestMethod.POST)
    public ResponseEntity<?> filterLogs(@RequestBody FilterParamsDTO filterParamsDTO) {
        LocalDateTime ldcFrom = null;
        LocalDateTime ldcTo = null;

        if(filterParamsDTO.getDateFrom() != null) {
            ldcFrom = filterParamsDTO.getDateFrom().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        if(filterParamsDTO.getDateTo() != null) {
            ldcTo = filterParamsDTO.getDateTo().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        FilterParams filterParams = new FilterParams(filterParamsDTO.getLogType(), filterParamsDTO.getLogSource(), ldcFrom, ldcTo, "");

        List<LogModel> logs = logService.filterLogs(filterParams);
        List<LogDTO> logDTOS = new ArrayList<LogDTO>();
        for(LogModel log: logs){
            logDTOS.add(new LogDTO(log.getLevel(), log.getMessage(), log.getLogTime().format(formatter), log.getLogSource(), log.getIp(), log.isAlarm()));
        }
        return new ResponseEntity<>(logDTOS, HttpStatus.OK);
    }
}
