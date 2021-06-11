package bsep.hospital.dto;

public class AdminRuleDTO {

    private String ruleName;

    public AdminRuleDTO() {
    }

    public AdminRuleDTO(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
