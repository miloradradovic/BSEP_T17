package bsep.hospital.model;

import java.time.LocalDateTime;

public class ReportParams {

    LocalDateTime from;
    LocalDateTime to;

    public ReportParams() {
    }

    public ReportParams(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
