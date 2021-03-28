package bsep.admin.dto;

public class UserTokenStateDTO {
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
