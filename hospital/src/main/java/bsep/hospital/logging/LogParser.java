package bsep.hospital.logging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class LogParser {

    @Value("${app_log_path}")
    private String appLogsPath;

    @Value("${keycloak_log_path}")
    private String keycloakLogsPath;


    public List<LogModel> parseAppLogs(int startingRow)  {
        int currentRow = 1;
        List<LogModel> models = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            FileInputStream fstream = new FileInputStream(appLogsPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
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
                        LogModel logModel = new LogModel(LogType.valueOf(level), message, dateOfLog, LogSource.APP);
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
            e.printStackTrace();
        }
        return models;

    }

    public List<LogModel> parseKeycloakLogs(int startingRow) {
        int currentRow = 1;
        List<LogModel> models = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            FileInputStream fstream = new FileInputStream(keycloakLogsPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
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
                        String[] splitted = strLine.split(" ");
                        message = String.join(" ", Arrays.asList(splitted).subList(4, Arrays.asList(splitted).size()));

                        LogModel logModel = new LogModel(LogType.valueOf(level), message, dateOfLog, LogSource.KEYCLOAK);
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
            e.printStackTrace();
        }
        return models;
    }
}
