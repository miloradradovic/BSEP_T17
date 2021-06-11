package bsep.hospital.service;

import bsep.hospital.logging.LogModel;
import bsep.hospital.logging.LogParser;
import bsep.hospital.repository.LogRepository;
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

    private int rowNumberAppLogs = 0;
    private int rowNumberKeycloakLogs = 0;

    public LogService() {

    }

    public void save(List<LogModel> logModels) {
        for (LogModel logModel: logModels) {
            logRepository.save(logModel);
        }
    }

    public List<LogModel> findAll() {
        return logRepository.findAll();
    }

    @Scheduled(fixedRate = 5000)
    public void scheduledTest() {
        List<LogModel> parsedAppLogs = logParser.parseAppLogs(rowNumberAppLogs + 1);
        rowNumberAppLogs = rowNumberAppLogs + parsedAppLogs.size();
        List<LogModel> parsedKeyCloakLogs = logParser.parseKeycloakLogs(rowNumberKeycloakLogs + 1);
        rowNumberKeycloakLogs = rowNumberKeycloakLogs + parsedKeyCloakLogs.size();
        parsedAppLogs.addAll(parsedKeyCloakLogs);
        save(parsedAppLogs);
    }
}
