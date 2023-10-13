package hexlet.code.dto;

import java.io.Serializable;

public class AuthRequestDto implements Serializable {

    private String email;
    private String password;

    //need default constructor for JSON Parsing
    public AuthRequestDto() {

    }

    public AuthRequestDto(String username, String password) {
        this.setEmail(username);
        this.setPassword(password);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
