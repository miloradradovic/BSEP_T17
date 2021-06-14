package bsep.hospital.model;

import java.time.LocalDateTime;

public class FilterParams {

    private String logType;
    private String logSource;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    public FilterParams() {
    }

    public FilterParams(String logType, String logSource, LocalDateTime dateFrom, LocalDateTime dateTo, String regexp) {
        this.logType = logType;
        this.logSource = logSource;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getLogSource() {
        return logSource;
    }

    public void setLogSource(String logSource) {
        this.logSource = logSource;
    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }
}
