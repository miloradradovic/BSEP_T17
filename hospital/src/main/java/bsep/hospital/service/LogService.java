package bsep.hospital.service;

import bsep.hospital.logging.LogModel;
import bsep.hospital.logging.LogParser;
import bsep.hospital.model.LogConfig;
import bsep.hospital.repository.LogRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
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

    @Value("${configuration_json_path1}")
    private String pathToConfigJson1;

    @Value("${configuration_json_path2}")
    private String pathToConfigJson2;

    @Value("${configuration_json_path3}")
    private String pathToConfigJson3;

    @Value("${simulator_path1}")
    private String simulatorPath1;
    @Value("${simulator_path2}")
    private String simulatorPath2;
    @Value("${simulator_path3}")
    private String simulatorPath3;


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
    @Async
    public void logParsing() {
        logger.info("Parsing logs from files.");
        Pair<List<LogModel>, Integer> parsed = logParser.parseAppLogs(rowNumberAppLogs);
        List<LogModel> parsedAppLogs = parsed.getValue0();
        rowNumberAppLogs = parsed.getValue1();

        Pair<List<LogModel>, Integer> parsed2 = logParser.parseKeycloakLogs(rowNumberKeycloakLogs);
        List<LogModel> parsedKeyCloakLogs = parsed2.getValue0();
        rowNumberKeycloakLogs = parsed2.getValue1();

        parsedAppLogs.addAll(parsedKeyCloakLogs);
        logger.info("Successfully parsed logs from files.");
        save(parsedAppLogs);
    }

    @Scheduled(fixedRate = 5000)
    @Async
    public void simulateLogs() {
        try {
            Gson gson = new Gson();
            logger.info("Parsing simulate logs");
            List<LogConfig> configs = new ArrayList<>();
            LogConfig logConfig1 = gson.fromJson(new FileReader(pathToConfigJson1), LogConfig.class);
            LogConfig logConfig2 = gson.fromJson(new FileReader(pathToConfigJson2), LogConfig.class);
            LogConfig logConfig3 = gson.fromJson(new FileReader(pathToConfigJson3), LogConfig.class);
            if (logConfig1 != null) {
                configs.add(logConfig1);
            }
            if (logConfig2 != null) {
                configs.add(logConfig2);
            }
            if (logConfig3 != null) {
                configs.add(logConfig3);
            }

            for (LogConfig logConfig: configs) {
                if (configs.indexOf(logConfig) == 0) {
                    System.out.println("LOG CONFIG 1 PARSIRA");
                } else if (configs.indexOf(logConfig) == 1) {
                    System.out.println("LOG CONFIG 2 PARSIRA");
                } else {
                    System.out.println("LOG CONFIG 3 PARSIRA");
                }
                readSimulatorLogs(logConfig);
                Thread.sleep(logConfig.getDuration() * 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readSimulatorLogs(LogConfig logConfig) {

        List<LogModel> parsedLogs = null;
        try {
            Pair<List<LogModel>, Integer> parsed = logParser.parseSimulatorLogs(logConfig);
            parsedLogs = parsed.getValue0();
            logConfig.setCurrentRow(parsed.getValue1());
            saveNewConfig(logConfig);
            save(parsedLogs);
            logger.info("Finished parsing simulator logs.");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void saveNewConfig(LogConfig logConfig) {
        try {
            Gson gson = new Gson();

            if (logConfig.getPath().equals(simulatorPath1)) {
                LogConfig logConfig1 = gson.fromJson(new FileReader(pathToConfigJson1), LogConfig.class);
                if (logConfig1 == null) {
                    gson = new GsonBuilder().setPrettyPrinting().create();
                    Writer configWriter = new FileWriter(pathToConfigJson1);
                    gson.toJson(logConfig, configWriter);
                    configWriter.close();
                } else {
                    logConfig1.setDuration(logConfig.getDuration());
                    logConfig1.setRegexp(logConfig.getRegexp());
                    // logConfig1.setCurrentRow(logConfig.getCurrentRow());
                    gson = new GsonBuilder().setPrettyPrinting().create();
                    Writer configWriter = new FileWriter(pathToConfigJson1);
                    gson.toJson(logConfig1, configWriter);
                    configWriter.close();
                }
            } else if (logConfig.getPath().equals(simulatorPath2)) {
                LogConfig logConfig1 = gson.fromJson(new FileReader(pathToConfigJson2), LogConfig.class);
                if (logConfig1 == null) {
                    gson = new GsonBuilder().setPrettyPrinting().create();
                    Writer configWriter = new FileWriter(pathToConfigJson2);
                    gson.toJson(logConfig, configWriter);
                    configWriter.close();
                } else {
                    logConfig1.setDuration(logConfig.getDuration());
                    logConfig1.setRegexp(logConfig.getRegexp());
                    // logConfig1.setCurrentRow(logConfig.getCurrentRow());
                    gson = new GsonBuilder().setPrettyPrinting().create();
                    Writer configWriter = new FileWriter(pathToConfigJson2);
                    gson.toJson(logConfig1, configWriter);
                    configWriter.close();
                }
            } else {
                LogConfig logConfig1 = gson.fromJson(new FileReader(pathToConfigJson3), LogConfig.class);
                if (logConfig1 == null) {
                    gson = new GsonBuilder().setPrettyPrinting().create();
                    Writer configWriter = new FileWriter(pathToConfigJson3);
                    gson.toJson(logConfig, configWriter);
                    configWriter.close();
                } else {
                    logConfig1.setDuration(logConfig.getDuration());
                    logConfig1.setRegexp(logConfig.getRegexp());
                    // logConfig1.setCurrentRow(logConfig.getCurrentRow());
                    gson = new GsonBuilder().setPrettyPrinting().create();
                    Writer configWriter = new FileWriter(pathToConfigJson3);
                    gson.toJson(logConfig1, configWriter);
                    configWriter.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
