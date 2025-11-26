package com.javaweb.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetRequest {

    @NotBlank(message = "Email must not be empty")
    @Email(message = "Invalid email")
    private String email;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
