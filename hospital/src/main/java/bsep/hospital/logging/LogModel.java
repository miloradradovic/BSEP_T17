package bsep.hospital.logging;

import org.bson.types.ObjectId;
import org.kie.api.definition.type.Role;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Document
@Role(Role.Type.EVENT)
public class LogModel  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private ObjectId id;

    private LogType level;
    private String message;
    private LocalDateTime logTime;
    private LogSource logSource;
    private String ip;
    private boolean alarm = false;

    public LogModel() {
    }

    public LogModel(LogType level, String message, LocalDateTime logTime, LogSource logSource, String ip) {
        this.level = level;
        this.message = message;
        this.logTime = logTime;
        this.logSource = logSource;
        this.ip = ip;
    }

    public LogModel(ObjectId id, LogType level, String message, LocalDateTime logTime, LogSource logSource, String ip) {
        this.id = id;
        this.level = level;
        this.message = message;
        this.logTime = logTime;
        this.logSource = logSource;
        this.ip = ip;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalDateTime logTime) {
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
