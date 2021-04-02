package bsep.hospital.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UserTokenStateDTO {

    @NotNull
    @NotBlank
    private String accessToken;

    public UserTokenStateDTO() {
        this.accessToken = null;
    }

    public UserTokenStateDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
