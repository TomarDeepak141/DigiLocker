package Spring.digiLocker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @Email
    @NotBlank(message = "email si required")
    private String email;

    @Email
    @NotBlank(message = "email si required")
    public String getEmail() {

        return email;
    }

    public void setEmail(@Email @NotBlank(message = "email si required") String email) {
        this.email = email;
    }

    public @NotBlank String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank String password) {
        this.password = password;
    }

    @NotBlank
    private String password;
}
