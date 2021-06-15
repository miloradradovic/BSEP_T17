package bsep.hospital.dto;

import bsep.hospital.logging.LogSource;
import bsep.hospital.logging.LogType;

import java.time.LocalDateTime;
import java.util.Date;

public class FilterParamsDTO {

    private String logType;
    private String logSource;
    private Date dateFrom;
    private Date dateTo;

    public FilterParamsDTO() {
    }

    public FilterParamsDTO(String logType, String logSource, Date dateFrom, Date dateTo) {
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

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }
}
