package bsep.hospital.service;

import bsep.hospital.logging.LogModel;
import bsep.hospital.logging.LogParser;
import bsep.hospital.repository.LogRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogParser logParser;

    private static Logger logger = LogManager.getLogger(LogService.class);

    private int rowNumberAppLogs = 0;
    private int rowNumberKeycloakLogs = 0;

    public LogService() {

    }

    public void save(List<LogModel> logModels) {
        logger.info("Saving logs.");
        for (LogModel logModel: logModels) {
            logRepository.save(logModel);
        }
        logger.info("Finished saving logs.");
    }

    public List<LogModel> findAll() {
        logger.info("Getting all logs from database.");
        return logRepository.findAll();
    }

    @Scheduled(fixedRate = 5000)
    public void scheduledTest() {
        logger.info("Parsing logs from files.");
        List<LogModel> parsedAppLogs = logParser.parseAppLogs(rowNumberAppLogs + 1);
        rowNumberAppLogs = rowNumberAppLogs + parsedAppLogs.size();
        List<LogModel> parsedKeyCloakLogs = logParser.parseKeycloakLogs(rowNumberKeycloakLogs + 1);
        rowNumberKeycloakLogs = rowNumberKeycloakLogs + parsedKeyCloakLogs.size();
        parsedAppLogs.addAll(parsedKeyCloakLogs);
        logger.info("Successfully parsed logs from files.");
        save(parsedAppLogs);
    }
}
