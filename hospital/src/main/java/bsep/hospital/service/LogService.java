package bsep.hospital.service;

import bsep.hospital.logging.IPAddress;
import bsep.hospital.logging.LogModel;
import bsep.hospital.logging.LogParser;
import bsep.hospital.logging.LogType;
import bsep.hospital.model.FilterParams;
import bsep.hospital.model.LogConfig;
import bsep.hospital.model.Report;
import bsep.hospital.repository.LogRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LogParser logParser;

    @Autowired
    KieSession kieSession;

    private static Logger logger = LogManager.getLogger(LogService.class);

    private int rowNumberAppLogs = 0;
    private int rowNumberKeycloakLogs = 0;

    @Value("${configuration_json_path}")
    private String pathToConfigJson;


    public LogService() {

    }

    public void save(List<LogModel> logModels) {
        logger.info("Saving logs.");
        for (LogModel logModel : logModels) {
            logRepository.save(logModel);
        }
        logger.info("Finished saving logs.");
    }

    public List<LogModel> findAll() {
        logger.info("Getting all logs from database.");
        return logRepository.findAll();
    }

    public List<LogModel> findAllByAlarm() {
        logger.info("Getting all alarmed logs from database.");
        return logRepository.findAllByAlarm(true);
    }

    @Scheduled(fixedRate = 5000)
    @Async
    public void logParsing() {
        logger.info("Parsing app and keycloak logs from files.");
        Pair<List<LogModel>, Integer> parsed = logParser.parseAppLogs(rowNumberAppLogs);
        List<LogModel> parsedAppLogs = parsed.getValue0();
        rowNumberAppLogs = parsed.getValue1();

        Pair<List<LogModel>, Integer> parsed2 = logParser.parseKeycloakLogs(rowNumberKeycloakLogs);
        List<LogModel> parsedKeyCloakLogs = parsed2.getValue0();
        rowNumberKeycloakLogs = parsed2.getValue1();

        parsedAppLogs.addAll(parsedKeyCloakLogs);
        logger.info("Successfully parsed app and keycloak logs from files.");


        for (LogModel log : parsedAppLogs) {
            kieSession.insert(log);
        }

        kieSession.getAgenda().getAgendaGroup("admin-rules").setFocus();
        logger.info("Firing rules for logs.");
        kieSession.fireAllRules();

        Collection<LogModel> newEvents = (Collection<LogModel>) kieSession.getObjects(new ClassObjectFilter(LogModel.class));

        ArrayList<LogModel> newParsedAppLogs = new ArrayList<>(newEvents);
        Collection<FactHandle> handlers = kieSession.getFactHandles();
        for (FactHandle handle : handlers) {
            Object obj = kieSession.getObject(handle);

            if (obj.getClass() != IPAddress.class)
                kieSession.delete(handle);
        }
        save(newParsedAppLogs);
        rewriteIPs();
    }

    @Scheduled(fixedRate = 5000)
    @Async
    public void simulateLogs() {
        try {
            logger.info("Parsing simulate logs");
            List<LogConfig> configs = getLogModelsFromConfig();
            for (LogConfig logConfig : configs) {
                readSimulatorLogs(logConfig);
                Thread.sleep(logConfig.getDuration() * 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readSimulatorLogs(LogConfig logConfig) {

        List<LogModel> parsedLogs = null;
        Pair<List<LogModel>, Integer> parsed = logParser.parseSimulatorLogs(logConfig);
        parsedLogs = parsed.getValue0();


        for (LogModel log : parsedLogs) {
            kieSession.insert(log);
        }

        kieSession.getAgenda().getAgendaGroup("admin-rules").setFocus();
        logger.info("Firing all rules for simulated logs.");
        kieSession.fireAllRules();

        Collection<LogModel> newEvents = (Collection<LogModel>) kieSession.getObjects(new ClassObjectFilter(LogModel.class));

        ArrayList<LogModel> newParsedLogs = new ArrayList<>(newEvents);
        Collection<FactHandle> handlers = kieSession.getFactHandles();
        for (FactHandle handle : handlers) {
            Object obj = kieSession.getObject(handle);

            if (obj.getClass() != IPAddress.class)
                kieSession.delete(handle);
        }

        logConfig.setCurrentRow(parsed.getValue1());
        saveConfig(logConfig);
        save(newParsedLogs);
        rewriteIPs();
        logger.info("Finished parsing simulator logs.");
    }

    private void saveConfig(LogConfig logConfig) {
        logger.info("Attempting to save log config.");
        try {
            List<LogConfig> configs = getLogModelsFromConfig();
            boolean found = false;
            logger.info("Checking if config with the same path already exists.");
            for (LogConfig logConfig1 : configs) {
                if (logConfig1.getPath().equals(logConfig.getPath())) {
                    logger.info("Overriding the existing log config.");
                    found = true;
                    logConfig1.setRegexp(logConfig.getRegexp());
                    logConfig1.setDuration(logConfig.getDuration());
                    logConfig1.setCurrentRow(logConfig.getCurrentRow());
                }
            }
            if (!found) {
                configs.add(logConfig);
            }

            writeToConfigFile(configs);

        } catch (Exception e) {
            logger.error("Failed to save log config.");
        }
    }

    private List<LogConfig> getLogModelsFromConfig() {
        try {
            logger.info("Getting logs from config json file.");
            Gson gson = new Gson();
            return gson.fromJson(new FileReader(pathToConfigJson), new TypeToken<ArrayList<LogConfig>>() {
            }.getType());
        } catch (Exception e) {
            logger.error("Failed to get logs from config json file.");
        }
        return new ArrayList<>();
    }

    public void createNewConfig(LogConfig logConfig) {
        logger.info("Attempting to create new log config.");
        try {
            List<LogConfig> configs = getLogModelsFromConfig();
            boolean found = false;
            for (LogConfig logConfig1 : configs) {
                if (logConfig1.getPath().equals(logConfig.getPath())) {
                    found = true;
                    logConfig1.setRegexp(logConfig.getRegexp());
                    logConfig1.setDuration(logConfig.getDuration());
                }
            }
            if (!found) {
                configs.add(logConfig);
            }

            writeToConfigFile(configs);

        } catch (Exception e) {
            logger.error("Failed to create new log config.");
        }
    }

    private void writeToConfigFile(List<LogConfig> configs) {
        try {
            logger.info("Attempting to write to config json file.");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Writer configWriter = new FileWriter(pathToConfigJson);
            gson.toJson(configs, configWriter);
            configWriter.close();
            logger.info("Successfully wrote to config json file.");
        } catch (Exception e) {
            logger.error("Failed to write to config json file.");
        }
    }

    public Report getReport(LocalDateTime dateFrom, LocalDateTime dateTo) {

        logger.info("Attempting to generate report.");

        Report report = new Report();
        report.setCountAll((int) logRepository.countByLogTimeBetween(dateFrom, dateTo));
        report.setCountDebug(logRepository.countByLevelAndLogTimeBetween(LogType.DEBUG, dateFrom, dateTo));
        report.setCountError(logRepository.countByLevelAndLogTimeBetween(LogType.ERROR, dateFrom, dateTo));
        report.setCountInfo(logRepository.countByLevelAndLogTimeBetween(LogType.INFO, dateFrom, dateTo));
        report.setCountTrace(logRepository.countByLevelAndLogTimeBetween(LogType.TRACE, dateFrom, dateTo));
        report.setCountWarn(logRepository.countByLevelAndLogTimeBetween(LogType.WARN, dateFrom, dateTo));
        logger.info("Successfully generated report.");
        return report;
    }

    public List<LogModel> filterLogs(FilterParams filterParams) {

        logger.info("Filtering logs.");

        List<LogModel> filtered = new ArrayList<>();
        List<LogModel> byDate = new ArrayList<>();
        boolean emptyByDate = false;
        if (filterParams.getDateFrom() == null && filterParams.getDateTo() != null) {
            byDate = logRepository.findByLogTimeBefore(filterParams.getDateTo());
        } else if (filterParams.getDateFrom() != null && filterParams.getDateTo() == null) {
            byDate = logRepository.findByLogTimeAfter(filterParams.getDateFrom());
        } else if (filterParams.getDateFrom() == null && filterParams.getDateTo() == null) {
            byDate = findAll();
        } else { // both != null
            byDate = logRepository.findByLogTimeBetween(filterParams.getDateFrom(), filterParams.getDateTo());
        }

        if (filterParams.getLogType().equals("") && filterParams.getLogSource().equals("")) {
            return byDate;
        } else if (filterParams.getLogType().equals("") && !filterParams.getLogSource().equals("")) {
            for (LogModel logModel : byDate) {
                if (logModel.getLogSource().toString().matches(filterParams.getLogSource())) {
                    filtered.add(logModel);
                }
            }
        } else if (!filterParams.getLogType().equals("") && filterParams.getLogSource().equals("")) {
            for (LogModel logModel : byDate) {
                if (logModel.getLevel().toString().matches(filterParams.getLogType())) {
                    filtered.add(logModel);
                }
            }
        } else {
            for (LogModel logModel : byDate) {
                if (logModel.getLevel().toString().matches(filterParams.getLogType()) && logModel.getLogSource().toString().matches(filterParams.getLogSource())) {
                    filtered.add(logModel);
                }
            }
        }
        logger.info("Finished filtering");
        return filtered;
    }

    private void rewriteIPs() {

        Collection<IPAddress> newEvents = (Collection<IPAddress>) kieSession.getObjects(new ClassObjectFilter(IPAddress.class));
        ArrayList<IPAddress> ipAddresses = new ArrayList<>(newEvents);

        File file = new File("src/main/resources/dangerous_ips.txt");

        try (BufferedWriter br = new BufferedWriter(new FileWriter(file))) {
            for (IPAddress ip : ipAddresses) {
                br.write(ip.getIp());
                br.newLine();
            }
        } catch (IOException e) {
            logger.error("Error writing dangerous ip-s to file.");
        }
    }
}
