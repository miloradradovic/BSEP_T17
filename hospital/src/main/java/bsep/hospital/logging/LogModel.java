package bsep.hospital.logging;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Document
public class LogModel {

    @Id
    private ObjectId id;

    private LogType level;
    private String message;
    private LocalDateTime logTime;
    private LogSource logSource;

    public LogModel() {
    }

    public LogModel(LogType level, String message, LocalDateTime logTime, LogSource logSource) {
        this.level = level;
        this.message = message;
        this.logTime = logTime;
        this.logSource = logSource;
    }

    public LogModel(ObjectId id, LogType level, String message, LocalDateTime logTime, LogSource logSource) {
        this.id = id;
        this.level = level;
        this.message = message;
        this.logTime = logTime;
        this.logSource = logSource;
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
}
