package com.javaweb.model.request;

import jakarta.validation.constraints.NotBlank;

public class PasswordResetConfirmRequest {

    @NotBlank(message = "OTP không được để trống")
    private String otp;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;

    @NotBlank(message = "Vui lòng xác nhận mật khẩu")
    private String confirmPassword;

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword;}
}
