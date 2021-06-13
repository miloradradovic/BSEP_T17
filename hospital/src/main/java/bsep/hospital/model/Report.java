package bsep.hospital.model;

public class Report {

    private int countInfo;
    private int countError;
    private int countTrace;
    private int countWarn;
    private int countDebug;
    private int countAll;

    public Report() {
    }

    public Report(int countInfo, int countError, int countTrace, int countWarn, int countDebug, int countAll) {
        this.countInfo = countInfo;
        this.countError = countError;
        this.countTrace = countTrace;
        this.countWarn = countWarn;
        this.countDebug = countDebug;
        this.countAll = countAll;
    }

    public int getCountInfo() {
        return countInfo;
    }

    public void setCountInfo(int countInfo) {
        this.countInfo = countInfo;
    }

    public int getCountError() {
        return countError;
    }

    public void setCountError(int countError) {
        this.countError = countError;
    }

    public int getCountTrace() {
        return countTrace;
    }

    public void setCountTrace(int countTrace) {
        this.countTrace = countTrace;
    }

    public int getCountWarn() {
        return countWarn;
    }

    public void setCountWarn(int countWarn) {
        this.countWarn = countWarn;
    }

    public int getCountDebug() {
        return countDebug;
    }

    public void setCountDebug(int countDebug) {
        this.countDebug = countDebug;
    }

    public int getCountAll() {
        return countAll;
    }

    public void setCountAll(int countAll) {
        this.countAll = countAll;
    }
}
