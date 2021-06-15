package bsep.hospital.logging;

import bsep.hospital.keystore.KeyStoreReader;
import bsep.hospital.model.LogConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class LogParser {

    @Value("${app_log_path}")
    private String appLogsPath;

    @Value("${keycloak_log_path}")
    private String keycloakLogsPath;

    private static Logger logger = LogManager.getLogger(LogParser.class);

    public Pair<List<LogModel>, Integer> parseAppLogs(int startingRow)  {
        logger.info("Attempting to parse application logs");
        int currentRow = 1;
        List<LogModel> models = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            FileInputStream fstream = new FileInputStream(appLogsPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            logger.info("Reading app log file.");
            while ((strLine = br.readLine()) != null)   {
                if (currentRow >= startingRow) {
                    String level = "";
                    String message = "";
                    LocalDateTime dateOfLog;
                    try{
                        String dateString = strLine.substring(0, 19);
                        dateOfLog = LocalDateTime.parse(dateString, formatter);
                        String tryLevel = strLine.substring(24, 29);
                        if (tryLevel.equals("ERROR") || tryLevel.equals("TRACE") || tryLevel.equals("DEBUG")) {
                            level = tryLevel;
                            message = strLine.substring(30);
                        } else {
                            level = tryLevel.substring(0, 4);
                            message = strLine.substring(29);
                        }
                        LogModel logModel = new LogModel(LogType.valueOf(level), message, dateOfLog, LogSource.APP, "");
                        models.add(logModel);
                    } catch(Exception e) {
                        if (models.size() != 0) {
                            models.get(models.size()-1).setMessage(models.get(models.size()-1).getMessage() + strLine);
                        }
                    }
                }
                currentRow = currentRow + 1;
            }
            fstream.close();
        } catch (IOException e) {
            logger.error("Couldn't parse logs because file path to app logs is invalid.");
        }
        return new Pair<List<LogModel>, Integer>(models, currentRow);

    }

    public Pair<List<LogModel>, Integer> parseKeycloakLogs(int startingRow) {
        logger.info("Attempting to parse keycloak logs");
        int currentRow = 1;
        List<LogModel> models = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            FileInputStream fstream = new FileInputStream(keycloakLogsPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            logger.info("Reading keycloak log file.");
            while ((strLine = br.readLine()) != null)   {
                if (currentRow >= startingRow) {
                    String level = "";
                    String message = "";
                    LocalDateTime dateOfLog;
                    try{
                        String dateString = strLine.substring(0, 19);
                        dateOfLog = LocalDateTime.parse(dateString, formatter);
                        String tryLevel = strLine.substring(24, 29);
                        if (tryLevel.equals("ERROR") || tryLevel.equals("TRACE") || tryLevel.equals("DEBUG")) {
                            level = tryLevel;
                        } else {
                            level = tryLevel.substring(0, 4);
                        }
                        String ip = "";
                        String[] splitted = strLine.split(" ");
                        message = String.join(" ", Arrays.asList(splitted).subList(4, Arrays.asList(splitted).size()));
                        if (message.contains("isAddress=")) {
                            ip = message.substring(message.indexOf("ipAddress=")+10, message.indexOf(",", message.indexOf("ipAddress=")));
                        }
                        LogModel logModel = new LogModel(LogType.valueOf(level), message, dateOfLog, LogSource.KEYCLOAK, ip);
                        models.add(logModel);
                    } catch (Exception e) {
                        if (models.size() != 0) {
                            models.get(models.size()-1).setMessage(models.get(models.size()-1).getMessage() + strLine);
                        }
                    }
                }
                currentRow = currentRow + 1;
            }
            fstream.close();
        } catch (IOException e) {
            logger.error("Couldn't parse logs because file path to keycloak logs is invalid.");
        }
        return new Pair<List<LogModel>, Integer>(models, currentRow);
    }

    public Pair<List<LogModel>, Integer> parseSimulatorLogs(LogConfig logConfig) {
        logger.info("Attempting to parse simulator logs");
        List<LogModel> logs = new ArrayList<>();
        int currentRow = 1;
        try{
            FileInputStream fstream = new FileInputStream(logConfig.getPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            logger.info("Reading simulator log file.");
            while ((strLine = br.readLine()) != null) {
                if (currentRow >= logConfig.getCurrentRow() && Pattern.matches(logConfig.getRegexp(), strLine) && !strLine.equals("")) {
                    String[] splitted = strLine.split(" ");
                    String message = "";
                    String ip = "";
                    LocalDateTime dateOfLog;
                    String level = "";
                    String dateString = strLine.substring(0, 19);
                    dateOfLog = LocalDateTime.parse(dateString, formatter);
                    String tryLevel = strLine.substring(20, 25);
                    if (tryLevel.equals("ERROR") || tryLevel.equals("TRACE") || tryLevel.equals("DEBUG")) {
                        level = tryLevel;
                    } else {
                        level = tryLevel.substring(0, 4);
                    }
                    ip = splitted[3];
                    for(int i = 4; i < splitted.length; i++) {
                        message = message + splitted[i];
                    }
                    LogModel logModel = new LogModel(LogType.valueOf(level), message, dateOfLog, LogSource.SIMULATOR, ip);
                    logs.add(logModel);
                }
                currentRow = currentRow + 1;
            }
            fstream.close();
        } catch (Exception e) {
            logger.error("Couldn't parse logs because file path to simulator logs is invalid.");
        }
        return new Pair<List<LogModel>, Integer>(logs, currentRow);
    }

}
