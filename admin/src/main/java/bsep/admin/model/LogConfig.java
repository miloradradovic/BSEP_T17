package bsep.admin.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class LogConfig {

    @NotBlank
    @Pattern(regexp="|([^;])+")
    private String path;

    private int duration;

    @NotBlank
    @Pattern(regexp="|([^;])+")
    private String regexp;

    private int currentRow;

    public LogConfig() {
    }

    public LogConfig(String path, int duration, String regexp, int currentRow) {
        this.path = path;
        this.duration = duration;
        this.regexp = regexp;
        this.currentRow = currentRow;
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public void setCurrentRow(int currentRow) {
        this.currentRow = currentRow;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRegexp() {
        return regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }
}
