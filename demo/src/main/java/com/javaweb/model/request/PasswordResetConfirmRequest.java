package com.javaweb.model.request;

import jakarta.validation.constraints.NotBlank;

public class PasswordResetConfirmRequest {

    @NotBlank(message = "OTP must not be empty")
    private String otp;

    @NotBlank(message = "The new password must not be empty")
    private String newPassword;

    @NotBlank(message = "Please confirm the password")
    private String confirmPassword;

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword;}
}
