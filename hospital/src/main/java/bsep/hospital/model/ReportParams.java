package bsep.hospital.model;

import java.util.Date;

public class ReportParams {

    Date from;
    Date to;

    public ReportParams() {
    }

    public ReportParams(Date from, Date Date) {
        this.from = from;
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
}
