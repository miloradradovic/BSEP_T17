package bsep.hospital.dto;
import javax.validation.constraints.NotNull;

public class PersonDTO {

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String surname;


    public PersonDTO(@NotNull String email, @NotNull String name, @NotNull String surname) {
        this.email = email;
        this.name = name;
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

}
