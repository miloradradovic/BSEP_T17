package bsep.hospital.dto;

import bsep.hospital.logging.LogSource;
import bsep.hospital.logging.LogType;

import java.time.LocalDateTime;

public class LogDTO {

    private LogType level;
    private String message;
    private String logTime;
    private String alarmDescription;
    private LogSource logSource;
    private String ip;
    private boolean alarm;

    public LogDTO() {
    }

    public LogDTO(LogType level, String message, String logTime, LogSource logSource, String ip, boolean alarm, String alarmDescription) {
        this.level = level;
        this.message = message;
        this.logTime = logTime;
        this.logSource = logSource;
        this.ip = ip;
        this.alarm = alarm;
        this.alarmDescription = alarmDescription;
    }

    public String getAlarmDescription() {
        return alarmDescription;
    }

    public void setAlarmDescription(String alarmDescription) {
        this.alarmDescription = alarmDescription;
    }

    public LogType getLevel() {
        return level;
    }

    public void setLevel(LogType level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public LogSource getLogSource() {
        return logSource;
    }

    public void setLogSource(LogSource logSource) {
        this.logSource = logSource;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }
}
