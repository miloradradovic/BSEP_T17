package bsep.hospital.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class AdminRuleDTO {

    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String ruleName;

    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String levelInput;

    @NotBlank
    @Pattern(regexp="[a-zA-Z0-9 ]+")
    private String messageInput;

    public AdminRuleDTO() {
    }

    public AdminRuleDTO(String ruleName, String levelInput, String messageInput) {
        this.ruleName = ruleName;
        this.levelInput = levelInput;
        this.messageInput = messageInput;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getLevelInput() {
        return levelInput;
    }

    public void setLevelInput(String levelInput) {
        this.levelInput = levelInput;
    }

    public String getMessageInput() {
        return messageInput;
    }

    public void setMessageInput(String messageInput) {
        this.messageInput = messageInput;
    }
}
